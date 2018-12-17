package go2k.compile.compiler

import go2k.compile.go.GNode
import kastree.ast.Node

fun Context.compileExprCall(v: GNode.Expr.Call): Node.Expr {
    // If the function is a const type but not a function, it's a conversion instead of a function call
    val isConv = v.func.type is GNode.Type.Const &&
        (v.func.type as GNode.Type.Const).type.unnamedType() !is GNode.Type.Signature
    if (isConv) {
        // Must be a singular arg
        val arg = v.args.singleOrNull() ?: error("Expecting single conversion arg")
        return compileExpr(arg, coerceTo = v.func)
    }
    // Handle built-ins elsewhere
    if (v.func.type is GNode.Type.BuiltIn) return compileExprCallBuiltIn(v, (v.func.type as GNode.Type.BuiltIn).name)

    var preStmt: Node.Stmt? = null
    val sigType = v.func.type.unnamedType() as GNode.Type.Signature
    // As a special case, if it's a single arg call w/ multi-return, we break it up in temp vars
    val singleArgCallType = (v.args.singleOrNull() as? GNode.Expr.Call)?.func?.type?.unnamedType() as? GNode.Type.Signature
    var args =
        if (singleArgCallType?.results.orEmpty().size > 1) {
            // Deconstruct to temp vals and make those the new args
            val tempVars = singleArgCallType?.results.orEmpty().map { currFunc.newTempVar() }
            preStmt = property(
                readOnly = true,
                vars = tempVars.map { propVar(it) },
                expr = compileExpr(v.args.single(), byValue = true)
            ).toStmt()
            tempVars.map { it.toName() }
        } else v.args.mapIndexed { index, arg ->
            // Vararg is the type of the slice
            val argType =
                if (!sigType.variadic || index < sigType.params.lastIndex) sigType.params[index]
                else (sigType.params.last().unnamedType() as GNode.Type.Slice).elem
            compileExpr(arg, coerceToType = argType, byValue = true)
        }
    // If this is variadic and the args spill into the varargs, make a slice
    if (sigType.variadic && args.size >= sigType.params.size) {
        args = args.take(sigType.params.size - 1) + args.drop(sigType.params.size - 1).let {
            val cls = (sigType.params.last() as? GNode.Type.Basic)?.kotlinPrimitiveType() ?: Any::class
            call(
                expr = "go2k.runtime.slice".toDottedExpr(),
                args = listOf(valueArg(call(
                    expr = cls.arrayOfQualifiedFunctionName().toDottedExpr(),
                    args = it.map { valueArg(it) }
                )))
            )
        }
    }
    val callExpr = call(
        expr = compileExpr(v.func),
        args = args.map { valueArg(it) }
    )
    // If a pre-stmt exists, this becomes a run expr, otherwise just a call
    return if (preStmt == null) callExpr else call(
        expr = "run".toName(),
        lambda = trailLambda(listOf(preStmt, callExpr.toStmt()))
    )
}

fun Context.compileExprCallBuiltIn(v: GNode.Expr.Call, name: String) = when (name) {
    "append" -> {
        // First arg is the slice
        val sliceType = v.args.first().type.unnamedType() as GNode.Type.Slice
        // If ellipsis is present, only second arg is allowed and it's passed explicitly,
        // otherwise a slice literal is created (TODO: kinda slow to create those unnecessarily)
        val arg =
            if (v.args.last() is GNode.Expr.Ellipsis) compileExpr(v.args.apply { require(size == 2) }.last())
            else compileExprCompositeLitSlice(sliceType, v.args.drop(1))
        call(
            expr = "go2k.runtime.builtin.append".toDottedExpr(),
            args = listOf(valueArg(compileExpr(v.args.first())), valueArg(arg))
        )
    }
    "make" -> when (val argType = (v.args.first().type as GNode.Type.Const).type) {
        is GNode.Type.Chan -> call(
            expr = "go2k.runtime.builtin.makeChan".toDottedExpr(),
            typeArgs = listOf(compileType(argType.elem)),
            args = v.args.getOrNull(1)?.let { listOf(valueArg(compileExpr(it))) } ?: emptyList()
        )
        is GNode.Type.Map -> {
            // Primitive means the first arg is the zero val
            val firstArgs =
                if (argType.elem !is GNode.Type.Basic) emptyList()
                else listOf(valueArg(expr = compileTypeZeroExpr(argType.elem)))
            call(
                expr = "go2k.runtime.builtin.makeMap".toDottedExpr(),
                typeArgs = listOf(compileType(argType.key), compileType(argType.elem)),
                args = firstArgs +
                    if (v.args.size == 1) emptyList()
                    else listOf(valueArg(name = "size", expr = compileExpr(v.args[1])))
            )
        }
        is GNode.Type.Slice -> {
            val elemType = argType.elem as GNode.Type.Basic
            val createSliceFnName = when (elemType.kotlinPrimitiveType()) {
                Char::class -> "makeCharSlice"
                Double::class -> "makeDoubleSlice"
                Float::class -> "makeFloatSlice"
                Int::class -> "makeIntSlice"
                Long::class -> "makeLongSlice"
                Short::class -> "makeShortSlice"
                String::class -> "makeStringSlice"
                UBYTE_CLASS -> "makeUByteSlice"
                UINT_CLASS -> "makeUIntSlice"
                ULONG_CLASS -> "makeULongSlice"
                USHORT_CLASS -> "makeUShortSlice"
                else -> "makeObjectSlice"
            }
            call(
                expr = "go2k.runtime.builtin.$createSliceFnName".toDottedExpr(),
                args = v.args.drop(1).map { valueArg(compileExpr(it)) }
            )
        }
        else -> error("Unrecognized make for $argType")
    }
    "recover" -> call(
        // Use the outside-defer one if not in defer
        expr = if (currFunc.inDefer) "recover".toName() else "go2k.runtime.builtin.recover".toDottedExpr()
    )
    else -> call(
        expr = compileExpr(v.func),
        args = v.args.map { valueArg(compileExpr(it)) }
    )
}