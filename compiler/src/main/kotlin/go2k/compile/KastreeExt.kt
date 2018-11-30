package go2k.compile

import kastree.ast.Node
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KClass

fun arrayType(of: KClass<*>) = "kotlin.Array".toDottedType(of.toType())

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

fun constExpr(value: String, form: Node.Expr.Const.Form) = Node.Expr.Const(value, form)

fun Double.toConst() = Node.Expr.Const(toString(), Node.Expr.Const.Form.FLOAT)

fun Node.Expr.BinaryOp.Token.toOper() = Node.Expr.BinaryOp.Oper.Token(this)

fun Node.Expr.dot(rhs: Node.Expr, safe: Boolean = false) =
    binaryOp(this, if (safe) Node.Expr.BinaryOp.Token.DOT_SAFE else Node.Expr.BinaryOp.Token.DOT, rhs)

fun Node.Expr.nullDeref() = unaryOp(this, Node.Expr.UnaryOp.Token.NULL_DEREF, false)

fun Node.Expr.paren() = Node.Expr.Paren(this)

fun Node.Expr.toStmt() = Node.Stmt.Expr(this)

fun func(
    mods: List<Node.Modifier> = emptyList(),
    typeParams: List<Node.TypeParam> = emptyList(),
    receiverType: Node.Type? = null,
    name: String? = null,
    paramTypeParams: List<Node.TypeParam> = emptyList(),
    params: List<Node.Decl.Func.Param> = emptyList(),
    type: Node.Type? = null,
    typeConstraints: List<Node.TypeConstraint> = emptyList(),
    body: Node.Decl.Func.Body? = null
) = Node.Decl.Func(mods, typeParams, receiverType, name, paramTypeParams, params, type, typeConstraints, body)

fun Int.toConst() = toString().toIntConst()

fun KClass<*>.toType(typeParams: List<Node.Type?> = emptyList()) = Node.Type(
    mods = emptyList(),
    ref = Node.TypeRef.Simple(
        pieces = qualifiedName!!.split('.').let { names ->
            names.mapIndexed { index, name ->
                Node.TypeRef.Simple.Piece(name, if (index == names.size - 1) typeParams else emptyList())
            }
        }
    )
)

fun Long.toConst() = toString().toLongConst()

val NullConst = Node.Expr.Const("null", Node.Expr.Const.Form.NULL)

fun Node.Modifier.Keyword.toMod() = Node.Modifier.Lit(this)

fun param(
    mods: List<Node.Modifier> = emptyList(),
    readOnly: Boolean? = null,
    name: String,
    type: Node.Type? = null,
    default: Node.Expr? = null
) = Node.Decl.Func.Param(mods, readOnly, name, type, default)

fun primaryConstructor(
    mods: List<Node.Modifier> = emptyList(),
    params: List<Node.Decl.Func.Param> = emptyList()
) = Node.Decl.Structured.PrimaryConstructor(mods, params)

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

fun propVar(name: String, type: Node.Type? = null) = Node.Decl.Property.Var(name, type)

// TODO: escaping and stuff
fun String.toDottedExpr() = split('.').let {
    it.drop(1).fold(Node.Expr.Name(it.first()) as Node.Expr) { expr, piece ->
        binaryOp(expr, Node.Expr.BinaryOp.Token.DOT, piece.toName())
    }
}
fun String.toDottedType(trailingTypeParam: Node.Type? = null) = Node.Type(
    mods = emptyList(),
    ref = Node.TypeRef.Simple(
        pieces = split('.').let {
            it.mapIndexed { index, s ->
                // Last index has type param
                Node.TypeRef.Simple.Piece(
                    name = s,
                    typeParams =
                        if (index == it.size - 1 && trailingTypeParam != null) listOf(trailingTypeParam)
                        else emptyList()
                )
            }
        }
    )
)

fun String.toFloatConst() = Node.Expr.Const(this, Node.Expr.Const.Form.FLOAT)
fun String.toInfix() = Node.Expr.BinaryOp.Oper.Infix(this)
fun String.toIntConst() = Node.Expr.Const(this, Node.Expr.Const.Form.INT)
fun String.toLongConst() = Node.Expr.Const(this, Node.Expr.Const.Form.INT)
fun String.toName() = Node.Expr.Name(this)
fun String.toStringTmpl(raw: Boolean = false) = Node.Expr.StringTmpl(
    elems = listOf(Node.Expr.StringTmpl.Elem.Regular(this)),
    raw = raw
)
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

fun structured(
    mods: List<Node.Modifier> = emptyList(),
    form: Node.Decl.Structured.Form = Node.Decl.Structured.Form.CLASS,
    name: String = "",
    typeParams: List<Node.TypeParam> = emptyList(),
    primaryConstructor: Node.Decl.Structured.PrimaryConstructor? = null,
    parentAnns: List<Node.Modifier.AnnotationSet> = emptyList(),
    parents: List<Node.Decl.Structured.Parent> = emptyList(),
    typeConstraints: List<Node.TypeConstraint> = emptyList(),
    members: List<Node.Decl> = emptyList()
) = Node.Decl.Structured(mods, form, name, typeParams, primaryConstructor,
    parentAnns, parents, typeConstraints, members)

fun Node.Type.nullable() = copy(ref = Node.TypeRef.Nullable(ref))

fun Node.TypeRef.toDottedExpr(): Node.Expr {
    if (this !is Node.TypeRef.Simple) error("Expected simple type")
    require(pieces.first().typeParams.isEmpty())
    return pieces.drop(1).fold(pieces.first().name.toName() as Node.Expr) { expr, piece ->
        require(piece.typeParams.isEmpty())
        expr.dot(piece.name.toName())
    }
}

fun trailLambda(
    params: List<Node.Expr.Brace.Param> = emptyList(),
    stmts: List<Node.Stmt> = emptyList()
) = Node.Expr.Call.TrailLambda(
    anns = emptyList(),
    label = null,
    func = Node.Expr.Brace(params = params, block = Node.Block(stmts))
)

fun typeOp(lhs: Node.Expr, op: Node.Expr.TypeOp.Token, rhs: Node.Type) =
    Node.Expr.TypeOp(lhs, Node.Expr.TypeOp.Oper(op), rhs)

fun unaryOp(expr: Node.Expr, op: Node.Expr.UnaryOp.Token, prefix: Boolean = true) =
    Node.Expr.UnaryOp(expr = expr, oper = Node.Expr.UnaryOp.Oper(op), prefix = prefix)

fun valueArg(
    name: String? = null,
    asterisk: Boolean = false,
    expr: Node.Expr
) = Node.ValueArg(name, asterisk, expr)