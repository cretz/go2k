package go2k.compile.compiler

import go2k.compile.go.GNode
import kastree.ast.Node
import kotlin.math.max

fun Context.compileExprCompositeLit(v: GNode.Expr.CompositeLit) =
    compileExprCompositeLit(v.type.unnamedType()!!, v.elts)

fun Context.compileExprCompositeLit(
    type: GNode.Type,
    elems: List<GNode.Expr>,
    overrideStructName: String? = null
): Node.Expr = when (type) {
    is GNode.Type.Array -> compileExprCompositeLitArray(type, elems)
    is GNode.Type.Map -> compileExprCompositeLitMap(type, elems)
    is GNode.Type.Named -> compileExprCompositeLitNamed(type, elems)
    is GNode.Type.Pointer -> compileExprCompositeLitPointer(type, elems)
    is GNode.Type.Slice -> compileExprCompositeLitSlice(type, elems)
    is GNode.Type.Struct -> compileExprCompositeLitStruct(overrideStructName, type, elems)
    else -> error("Unknown composite lit type: $type")
}

fun Context.compileExprCompositeLitArray(type: GNode.Type.Array, elems: List<GNode.Expr>) =
    compileExprCompositeLitArray(type.elem, type.len.toInt(), elems)

fun Context.compileExprCompositeLitArray(elemType: GNode.Type, explicitLen: Int?, elems: List<GNode.Expr>): Node.Expr {
    // There are two types of array literals here. One that has no indexes,
    // uses every slot, and the length of literals matches the given length
    // (or there is no given length). The other creates for a certain length
    // and then sets explicit indexes in an "apply" block.
    val simpleArrayLiteral = (explicitLen == null || elems.size == explicitLen) &&
        elems.all { it !is GNode.Expr.KeyValue }
    return if (simpleArrayLiteral) {
        call(
            // TODO: need to be better about compiling types here
            expr = ((elemType as? GNode.Type.Basic)?.kotlinPrimitiveType() ?: Any::class).
                arrayOfQualifiedFunctionName().toDottedExpr(),
            args = elems.map { valueArg(compileExpr(it, coerceToType = elemType)) }
        )
    } else {
        // Get the pairs, in order, and the overall len
        val elemPairs = elems.fold(emptyList<Pair<Int, Node.Expr>>()) { pairs, elem ->
            pairs + if (elem is GNode.Expr.KeyValue) {
                val key = ((elem.key.type as GNode.Type.Const).value as GNode.Const.Int).v.toInt()
                key to compileExpr(elem.value)
            } else {
                val key = pairs.lastOrNull()?.first?.plus(1) ?: 0
                key to compileExpr(elem)
            }
        }
        val len = explicitLen ?: elemPairs.fold(0) { len, (index, _) -> max(len, index + 1) }
        // Create the array and then set the specific indices in the apply block
        call(
            expr = compileExprCompositeLitArrayCreate(elemType, len).dot("apply".toName()),
            lambda = trailLambda(stmts = elemPairs.map { (index, elem) ->
                call(
                    expr = "set".toName(),
                    args = listOf(valueArg(expr = index.toConst()), valueArg(expr = elem))
                ).toStmt()
            })
        )
    }
}

fun Context.compileExprCompositeLitArrayCreate(elemType: GNode.Type, len: Int) = when {
    // Primitive is PrimArray(size), string is Array(size) { "" }, other is arrayOfNulls<Type>(size)
    elemType !is GNode.Type.Basic -> call(
        expr = "kotlin.collection.arrayOfNulls".toDottedExpr(),
        typeArgs = listOf(compileType(elemType)),
        args = listOf(valueArg(len.toConst()))
    )
    elemType.kind == GNode.Type.Basic.Kind.STRING -> call(
        expr = "kotlin.Array".toDottedExpr(),
        args = listOf(valueArg(len.toConst())),
        lambda = trailLambda(listOf(call("go2k.runtime.GoString".toDottedExpr()).toStmt()))
    )
    else -> call(
        expr = elemType.kotlinPrimitiveType().primitiveArrayClass()!!.qualifiedName!!.toDottedExpr(),
        args = listOf(valueArg(len.toConst()))
    )
}

fun Context.compileExprCompositeLitMap(type: GNode.Type.Map, elems: List<GNode.Expr>): Node.Expr.Call {
    // Primitive types have defaults
    val firstArg = (type.elem as? GNode.Type.Basic)?.let { valueArg(compileTypeZeroExpr(it)) }
    return call(
        expr = "go2k.runtime.mapOf".toDottedExpr(),
        typeArgs = listOf(compileType(type.key), compileType(type.elem)),
        args = listOfNotNull(firstArg) + elems.map { elem ->
            elem as GNode.Expr.KeyValue
            valueArg(call(
                expr = "kotlin.Pair".toDottedExpr(),
                args = listOf(valueArg(compileExpr(elem.key)), valueArg(compileExpr(elem.value)))
            ))
        }
    )
}

fun Context.compileExprCompositeLitPointer(type: GNode.Type.Pointer, elems: List<GNode.Expr>) = call(
    expr = GO_PTR_CLASS.ref().dot("lit"),
    args = listOf(valueArg(compileExprCompositeLit(type.elem, elems)))
)

fun Context.compileExprCompositeLitNamed(type: GNode.Type.Named, elems: List<GNode.Expr>) =
    // Structs are just named, everything else is wrapped
    compileExprToNamed(compileExprCompositeLit(type.underlying, elems, type.name().name), type)

fun Context.compileExprCompositeLitSlice(type: GNode.Type.Slice, elems: List<GNode.Expr>) = call(
    expr = "go2k.runtime.slice".toDottedExpr(),
    args = listOf(valueArg(compileExprCompositeLitArray(type.elem, null, elems)))
)

fun Context.compileExprCompositeLitStruct(dottedName: String?, type: GNode.Type.Struct, elems: List<GNode.Expr>) = call(
    expr = run {
        // Anon name if not given
        dottedName ?: anonStructTypes[type.toAnonType()] ?: error("Unable to find anon struct for $type")
    }.toDottedExpr(),
    args = elems.map { elem ->
        if (elem !is GNode.Expr.KeyValue) valueArg(compileExpr(elem))
        else valueArg(name = (elem.key as GNode.Expr.Ident).name, expr = compileExpr(elem.value))
    }
)