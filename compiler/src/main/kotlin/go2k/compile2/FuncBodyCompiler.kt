package go2k.compile2

import go2k.runtime.Slice
import go2k.runtime.builtin.withDefers
import kastree.ast.Node

fun Context.compileFuncBody(type: GNode.Expr.FuncType, block: GNode.Stmt.Block): FuncBody {
    // Named return idents need to be declared with zero vals up front
    val preStmts = type.results.flatMap { field ->
        field.names.map { ident ->
            property(
                vars = listOf(propVar(ident.name)),
                expr = compileTypeZeroExpr(ident.type ?: ident.defType ?: error("No ident type"))
            ).toStmt()
        }
    }
    return FuncBody(
        params = type.params.flatMap { field ->
            field.names.map { name ->
                // An ellipsis expr means a slice vararg
                if (field.type is GNode.Expr.Ellipsis) param(
                    name = name.name,
                    type = Slice::class.toType(listOf(compileType(field.type.elt!!.type!!))).nullable(),
                    default = NullConst
                ) else param(name = name.name, type = compileType(field.type.type!!))
            }
        },
        resultType = compileTypeMultiResult(type.results),
        // If there are defer statements, we need to wrap the body in a withDefers
        stmts = preStmts +
            if (!currFunc.hasDefer) compileStmtBlock(block).stmts
            else listOf(call(
                expr = ::withDefers.ref(),
                lambda = trailLambda(compileStmtBlock(block).stmts)
            ).toStmt())
    )
}

data class FuncBody(
    val params: List<Node.Decl.Func.Param>,
    val resultType: Node.Type?,
    val stmts: List<Node.Stmt>
)