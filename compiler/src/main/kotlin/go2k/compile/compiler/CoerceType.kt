package go2k.compile.compiler

import go2k.compile.go.GNode
import kastree.ast.Node
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KClass

fun Context.coerceType(v: GNode.Expr, expr: Node.Expr, to: GNode.Expr?, toType: GNode.Type?): Node.Expr {
    return coerceType(
        expr = expr,
        from = v.type ?: return expr,
        to = to?.type ?: toType ?: return expr
    )
}

fun Context.coerceType(expr: Node.Expr, from: GNode.Type, to: GNode.Type): Node.Expr {
    // If they both have primitive types, coerce that
    val fromPrim = from.kotlinPrimitiveType()
    val toPrim = to.kotlinPrimitiveType()
    if (fromPrim != null && toPrim != null) return coerceTypePrimitive(expr, fromPrim, toPrim)
    // Vars are fed back through for their basic type
    if (from is GNode.Type.Var) return coerceType(expr, from.type, to)
    if (to is GNode.Type.Var) return coerceType(expr, from, to.type)
    // Use unnamed types to compare with
    val fromUt = from.unnamedType()
    val toUt = to.unnamedType()
    return when (toUt) {
        fromUt -> expr
        is GNode.Type.Basic -> when (fromUt) {
            is GNode.Type.Interface -> typeOp(
                lhs = binaryOp(
                    lhs = expr.paren().dot("v", safe = true),
                    op = Node.Expr.BinaryOp.Token.ELVIS,
                    rhs = NullConst
                ).paren(),
                op = Node.Expr.TypeOp.Token.AS,
                rhs = compileType(to)
            )
            else -> error("Unable to convert $from to $to")
        }
        is GNode.Type.Chan -> when (fromUt) {
            is GNode.Type.Chan -> expr
            else -> TODO()
        }
        is GNode.Type.Interface -> when (fromUt) {
            is GNode.Type.Interface, GNode.Type.Nil -> typeOp(
                lhs = expr,
                op = Node.Expr.TypeOp.Token.AS,
                rhs = compileType(to)
            )
            is GNode.Type.Basic, is GNode.Type.Pointer -> call(
                expr = compileTypeRefExpr(toUt).dot("impl"),
                args = listOf(valueArg(expr))
            )
            else -> error("Unable to convert $from to $to")
        }
        is GNode.Type.Nil -> expr
        is GNode.Type.Pointer -> {
            // For interfaces - expr?.v ?: null
            val lhs = if (fromUt !is GNode.Type.Interface) expr else binaryOp(
                lhs = expr.paren().dot("v", safe = true),
                op = Node.Expr.BinaryOp.Token.ELVIS,
                rhs = NullConst
            )
            typeOp(
                lhs = lhs,
                op = Node.Expr.TypeOp.Token.AS,
                rhs = compileType(to)
            )
        }
        is GNode.Type.Slice -> when (fromUt) {
            GNode.Type.Nil -> typeOp(
                lhs = expr,
                op = Node.Expr.TypeOp.Token.AS,
                rhs = compileType(to)
            )
            else -> TODO()
        }
        else -> error("Unable to convert $from to $to")
    }
}

fun Context.coerceTypePrimitive(expr: Node.Expr, from: KClass<*>, to: KClass<*>) = when (to) {
    from -> expr
    Byte::class -> call(expr.dot("toByte"))
    Short::class -> call(expr.dot("toShort"))
    Char::class -> call(expr.dot("toChar"))
    Int::class -> call(expr.dot("toInt"))
    Long::class -> call(expr.dot("toLong"))
    UBYTE_CLASS -> call(expr.dot("toUByte"))
    USHORT_CLASS -> call(expr.dot("toUShort"))
    UINT_CLASS -> call(expr.dot("toUInt"))
    ULONG_CLASS -> call(expr.dot("toULong"))
    BigInteger::class -> call(expr.dot("toBigInteger"))
    Float::class -> call(expr.dot("toFloat"))
    Double::class -> call(expr.dot("toDouble"))
    BigDecimal::class -> call(expr.dot("toBigDecimal"))
    else -> error("Unable to convert to $to")
}