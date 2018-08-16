package go2k.compile

import kastree.ast.Node
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KClass

fun arrayType(of: KClass<*>) = Node.Type(
    mods = emptyList(),
    ref = Node.TypeRef.Simple(
        pieces = listOf(
            Node.TypeRef.Simple.Piece("kotlin", emptyList()),
            Node.TypeRef.Simple.Piece("Array", listOf(of.toType()))
        )
    )
)

fun binaryOp(lhs: Node.Expr, op: Node.Expr.BinaryOp.Token, rhs: Node.Expr) =
    Node.Expr.BinaryOp(lhs, op.toOper(), rhs)

fun binaryOp(lhs: Node.Expr, op: Node.Expr.BinaryOp.Oper, rhs: Node.Expr) =
    Node.Expr.BinaryOp(lhs, op, rhs)

fun Boolean.toConst() = Node.Expr.Const(if (this) "true" else "false", Node.Expr.Const.Form.BOOLEAN)

fun call(
    expr: Node.Expr,
    typeArgs: List<Node.Type?> = emptyList(),
    args: List<Node.ValueArg> = emptyList(),
    lambda: Node.Expr.Call.TrailLambda? = null
) = Node.Expr.Call(expr, typeArgs, args, lambda)

fun Char.toConst() = Node.Expr.Const(toString(), Node.Expr.Const.Form.CHAR)

fun Double.toConst() = Node.Expr.Const(toString(), Node.Expr.Const.Form.FLOAT)

fun Node.Expr.BinaryOp.Token.toOper() = Node.Expr.BinaryOp.Oper.Token(this)

fun Node.Expr.dot(rhs: Node.Expr) = binaryOp(this, Node.Expr.BinaryOp.Token.DOT, rhs)

fun func(
    mods: List<Node.Modifier> = emptyList(),
    typeParams: List<Node.TypeParam> = emptyList(),
    receiverType: Node.Type? = null,
    name: String,
    paramTypeParams: List<Node.TypeParam> = emptyList(),
    params: List<Node.Decl.Func.Param> = emptyList(),
    type: Node.Type? = null,
    typeConstraints: List<Node.TypeConstraint> = emptyList(),
    body: Node.Decl.Func.Body? = null
) = Node.Decl.Func(mods, typeParams, receiverType, name, paramTypeParams, params, type, typeConstraints, body)

fun Int.toConst() = toString().toIntConst()

fun KClass<*>.toType() = Node.Type(
    mods = emptyList(),
    ref = Node.TypeRef.Simple(
        pieces = qualifiedName!!.split('.').map { Node.TypeRef.Simple.Piece(it, emptyList()) }
    )
)

val NullConst = Node.Expr.Const("null", Node.Expr.Const.Form.NULL)

fun Node.Modifier.Keyword.toMod() = Node.Modifier.Lit(this)

fun param(
    mods: List<Node.Modifier> = emptyList(),
    readOnly: Boolean? = null,
    name: String,
    type: Node.Type,
    default: Node.Expr? = null
) = Node.Decl.Func.Param(mods, readOnly, name, type, default)

fun property(
    mods: List<Node.Modifier> = emptyList(),
    readOnly: Boolean = false,
    typeParams: List<Node.TypeParam> = emptyList(),
    receiverType: Node.Type? = null,
    vars: List<Node.Decl.Property.Var?> = emptyList(),
    typeConstraints: List<Node.TypeConstraint> = emptyList(),
    delegated: Boolean = false,
    expr: Node.Expr? = null,
    accessors: Node.Decl.Property.Accessors? = null
) = Node.Decl.Property(mods, readOnly, typeParams, receiverType, vars, typeConstraints, delegated, expr, accessors)

// TODO: escaping and stuff
fun String.toDottedExpr() = split('.').let {
    it.drop(1).fold(Node.Expr.Name(it.first()) as Node.Expr) { expr, piece ->
        binaryOp(expr, Node.Expr.BinaryOp.Token.DOT, piece.toName())
    }
}
fun String.toFloatConst() = Node.Expr.Const(this, Node.Expr.Const.Form.FLOAT)
fun String.toInfix() = Node.Expr.BinaryOp.Oper.Infix(this)
fun String.toIntConst() = Node.Expr.Const(this, Node.Expr.Const.Form.INT)
fun String.toName() = Node.Expr.Name(this)
fun String.toStringTmpl() = Node.Expr.StringTmpl(elems = listOf(Node.Expr.StringTmpl.Elem.Regular(this)), raw = false)
fun String.untypedFloatClass(includeFloatClass: Boolean = false): KClass<out Number> = toBigDecimal().let { bigDec ->
    if (includeFloatClass && bigDec.compareTo(bigDec.toFloat().toBigDecimal()) == 0) Float::class
    else if (bigDec.compareTo(bigDec.toDouble().toBigDecimal()) == 0) Double::class
    else BigDecimal::class
}
fun String.untypedIntClass(): KClass<out Number> = toBigInteger().let { bigInt ->
    if (bigInt >= Int.MIN_VALUE.toBigInteger() && bigInt <= Int.MAX_VALUE.toBigInteger()) Int::class
    else if (bigInt >= Long.MIN_VALUE.toBigInteger() && bigInt <= Long.MAX_VALUE.toBigInteger()) Long::class
    else BigInteger::class
}

fun unaryOp(expr: Node.Expr, op: Node.Expr.UnaryOp.Token, prefix: Boolean = true) =
    Node.Expr.UnaryOp(expr = expr, oper = Node.Expr.UnaryOp.Oper(op), prefix = prefix)

fun valueArg(
    name: String? = null,
    asterisk: Boolean = false,
    expr: Node.Expr
) = Node.ValueArg(name, asterisk, expr)