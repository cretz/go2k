package go2k.compile2

import kastree.ast.Node
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.javaMethod

// Order by type depth then alphabetical

fun arrayType(of: KClass<*>) = "kotlin.Array".toDottedType(of.toType())

fun binaryOp(lhs: Node.Expr, op: Node.Expr.BinaryOp.Token, rhs: Node.Expr) =
    Node.Expr.BinaryOp(lhs, op.toOper(), rhs)

fun binaryOp(lhs: Node.Expr, op: Node.Expr.BinaryOp.Oper, rhs: Node.Expr) =
    Node.Expr.BinaryOp(lhs, op, rhs)

fun brace(stmts: List<Node.Stmt>) = brace(emptyList(), stmts)
fun brace(params: List<List<Node.Decl.Property.Var?>>, stmts: List<Node.Stmt>) =
    Node.Expr.Brace(params.map { Node.Expr.Brace.Param(it, null) }, block(stmts))

fun block(stmts: List<Node.Stmt>) = Node.Block(stmts)

fun call(
    expr: Node.Expr,
    typeArgs: List<Node.Type?> = emptyList(),
    args: List<Node.ValueArg> = emptyList(),
    lambda: Node.Expr.Call.TrailLambda? = null
) = Node.Expr.Call(expr, typeArgs, args, lambda)

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

fun param(
    mods: List<Node.Modifier> = emptyList(),
    readOnly: Boolean? = null,
    name: String,
    type: Node.Type? = null,
    default: Node.Expr? = null
) = Node.Decl.Func.Param(mods, readOnly, name, type, default)

fun valueArg(
    expr: Node.Expr,
    name: String? = null,
    asterisk: Boolean = false
) = Node.ValueArg(name, asterisk, expr)

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

fun KFunction<*>.ref() = (javaMethod!!.declaringClass.`package`.name + ".$name").toDottedExpr()

fun Node.Block.toFuncBody() = Node.Decl.Func.Body.Block(this)

fun Node.Expr.toFuncBody() = Node.Decl.Func.Body.Expr(this)

fun Node.Expr.toStmt() = Node.Stmt.Expr(this)

fun Node.Expr.BinaryOp.Token.toOper() = Node.Expr.BinaryOp.Oper.Token(this)

fun Node.Modifier.Keyword.toMod() = Node.Modifier.Lit(this)

// TODO: escaping and stuff
fun String.toDottedExpr() = split('.').let {
    it.drop(1).fold(Node.Expr.Name(it.first()) as Node.Expr) { expr, piece ->
        go2k.compile.binaryOp(expr, Node.Expr.BinaryOp.Token.DOT, piece.toName())
    }
}

fun String.toDottedType(vararg trailingTypeParams: Node.Type?) = Node.Type(
    mods = emptyList(),
    ref = Node.TypeRef.Simple(
        pieces = split('.').let {
            it.mapIndexed { index, s ->
                // Last index has type param
                Node.TypeRef.Simple.Piece(
                    name = s,
                    typeParams = if (index == it.size - 1) trailingTypeParams.toList() else emptyList()
                )
            }
        }
    )
)

fun String.toName() = Node.Expr.Name(this)