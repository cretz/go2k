package go2k.compile

import kastree.ast.Node

fun binaryOp(lhs: Node.Expr, op: Node.Expr.BinaryOp.Token, rhs: Node.Expr) =
    Node.Expr.BinaryOp(lhs, Node.Expr.BinaryOp.Oper.Token(op), rhs)

fun Boolean.toConst() = Node.Expr.Const(if (this) "true" else "false", Node.Expr.Const.Form.BOOLEAN)

fun call(
    expr: Node.Expr,
    typeArgs: List<Node.Type?> = emptyList(),
    args: List<Node.ValueArg> = emptyList(),
    lambda: Node.Expr.Call.TrailLambda? = null
) = Node.Expr.Call(expr, typeArgs, args, lambda)

fun Char.toConst() = Node.Expr.Const(toString(), Node.Expr.Const.Form.CHAR)

fun Double.toConst() = Node.Expr.Const(toString(), Node.Expr.Const.Form.FLOAT)

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

fun Int.toConst() = Node.Expr.Const(toString(), Node.Expr.Const.Form.INT)

val NullConst = Node.Expr.Const("null", Node.Expr.Const.Form.NULL)

fun Node.Modifier.Keyword.toMod() = Node.Modifier.Lit(this)

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
fun String.toName() = Node.Expr.Name(this)
fun String.toStringTmpl() = Node.Expr.StringTmpl(elems = listOf(Node.Expr.StringTmpl.Elem.Regular(this)), raw = false)