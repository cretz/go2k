package go2k.compile.compiler

import go2k.compile.go.GNode
import kastree.ast.Node
import kotlin.math.max

fun Context.compileExprCompositeLit(v: GNode.Expr.CompositeLit): Node.Expr = when (v.litType) {
    is GNode.Expr.ArrayType -> when (val ut = v.type.unnamedType()) {
        is GNode.Type.Array -> compileExprCompositeLitArray(ut, v.elts)
        is GNode.Type.Slice -> compileExprCompositeLitSlice(ut, v.elts)
        else -> error("Unknown array type ${v.type}")
    }
    is GNode.Expr.MapType -> {
        val type = v.type.unnamedType() as GNode.Type.Map
        // Primitive types have defaults
        val firstArg = (type.elem as? GNode.Type.Basic)?.let { valueArg(compileTypeZeroExpr(it)) }
        call(
            expr = "go2k.runtime.builtin.mapOf".toDottedExpr(),
            typeArgs = listOf(compileType(type.key), compileType(type.elem)),
            args = listOfNotNull(firstArg) + v.elts.map { elt ->
                elt as GNode.Expr.KeyValue
                valueArg(call(
                    expr = "kotlin.Pair".toDottedExpr(),
                    args = listOf(valueArg(compileExpr(elt.key)), valueArg(compileExpr(elt.value)))
                ))
            }
        )
    }
    // Ident is struct lit
    is GNode.Expr.Ident -> {
        val type = v.type.unnamedType() as GNode.Type.Named
        call(
            expr = type.name.name.toName(),
            args = v.elts.map { elt ->
                if (elt !is GNode.Expr.KeyValue) valueArg(compileExpr(elt))
                else valueArg(name = (elt.key as GNode.Expr.Ident).name, expr = compileExpr(elt.value))
            }
        )
    }
    else -> error("Unknown composite lit type: ${v.type}")
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
        lambda = trailLambda(listOf("".toStringTmpl().toStmt()))
    )
    else -> call(
        expr = elemType.kotlinPrimitiveType().primitiveArrayClass()!!.qualifiedName!!.toDottedExpr(),
        args = listOf(valueArg(len.toConst()))
    )
}

fun Context.compileExprCompositeLitSlice(type: GNode.Type.Slice, elems: List<GNode.Expr>) = call(
    expr = "go2k.runtime.builtin.slice".toDottedExpr(),
    args = listOf(valueArg(compileExprCompositeLitArray(type.elem, null, elems)))
)