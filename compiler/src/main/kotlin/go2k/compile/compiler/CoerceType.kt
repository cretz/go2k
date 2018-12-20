package go2k.compile.compiler

import go2k.compile.go.GNode
import kastree.ast.Node
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KClass

fun Context.coerceType(v: GNode.Expr, expr: Node.Expr, to: GNode.Expr?, toType: GNode.Type?) =
    coerceType(expr, v.type, to?.type ?: toType)

fun Context.coerceType(expr: Node.Expr, from: GNode.Type?, to: GNode.Type?): Node.Expr {
    if (from == null || to == null) return expr
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
        is GNode.Type.Named -> when (fromUt) {
            // Anon struct assign to named struct
            is GNode.Type.Struct -> {
                require(toUt.underlying is GNode.Type.Struct)
                call(
                    expr = expr.dot("run"),
                    lambda = trailLambda(listOf(call(
                        expr = toUt.name().name.toName(),
                        args = toUt.underlying.fields.map { valueArg(it.name.toName(), it.name) }
                    ).toStmt()))
                )
            }
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
        is GNode.Type.Struct -> {
            val anonType = toUt.toAnonType()
            val anonTypeName = anonStructTypes[anonType] ?: error("Missing struct for $anonType")
            // If from is the same, we anon type, we don't need to do any convert
            if (fromUt is GNode.Type.Struct && anonType == fromUt.toAnonType()) expr
            else when (fromUt) {
                // Anon to anon or to regular struct
                is GNode.Type.Named, is GNode.Type.Struct -> call(
                    expr = expr.dot("run"),
                    lambda = trailLambda(listOf(call(
                        expr = anonTypeName.toDottedExpr(),
                        args = toUt.fields.map { valueArg(it.name.toName(), it.name) }
                    ).toStmt()))
                )
                else -> error("Unable to convert $from to $to")
            }
        }
        else -> error("Unable to convert $from to $to")
    }
}

fun Context.coerceTypeForByValueCopy(v: GNode.Expr, expr: Node.Expr) =
    coerceTypeForByValueCopy(v.type, expr)

fun Context.coerceTypeForByValueCopy(t: GNode.Type?, expr: Node.Expr): Node.Expr {
    // If the type is a struct and the expr is not a call instantiating it, we have to copy it
    val type = t.unnamedType()
    val structName = when {
        // Anon is just struct
        type is GNode.Type.Struct -> anonStructTypes[type.toAnonType()] ?: error("Can't find anon for $type")
        // Otherwise, named struct
        type is GNode.Type.Named && type.underlying is GNode.Type.Struct -> type.name().name
        else -> null
    }
    // Only a copy if there is a name and it's not an instantiation
    return if (structName == null || (expr is Node.Expr.Call && expr.expr == structName.toDottedExpr())) expr
        else call(expr.dot("\$copy"))
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