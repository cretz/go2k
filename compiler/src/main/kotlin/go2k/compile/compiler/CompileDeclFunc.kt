package go2k.compile.compiler

import go2k.compile.go.GNode
import go2k.runtime.Slice
import kastree.ast.Node

fun Context.compileDeclFunc(v: GNode.Decl.Func) = withFunc(v.type) {
    compileDeclFuncBody(v.type, v.body).let { (params, returnType, stmts) ->
        func(
            mods = listOfNotNull(
                Node.Modifier.Keyword.SUSPEND.toMod(),
                Node.Modifier.Keyword.INTERNAL?.takeIf { v.name.first().isLowerCase() }?.toMod()
            ),
            name = v.name,
            params = params,
            type = returnType,
            body = block(stmts).toFuncBody()
        )
    }
}

fun Context.compileDeclFuncBody(type: GNode.Expr.FuncType, block: GNode.Stmt.Block): DeclFuncBody {
    // Named return idents need to be declared with zero vals up front
    val preStmts = type.results.flatMap { field ->
        field.names.map { ident ->
            property(
                vars = listOf(propVar(ident.name)),
                expr = compileTypeZeroExpr(ident.type ?: ident.defType ?: error("No ident type"))
            ).toStmt()
        }
    }
    return DeclFuncBody(
        params = type.params.flatMap { field ->
            field.names.map { name ->
                // An ellipsis expr means a slice vararg
                if (field.type is GNode.Expr.Ellipsis) param(
                    name = name.name,
                    type = Slice::class.toType(compileType(field.type.elt!!.type!!)).nullable(),
                    default = NullConst
                ) else param(name = name.name, type = compileType(field.type.type!!))
            }
        },
        resultType = compileTypeMultiResult(type.results),
        // If there are defer statements, we need to wrap the body in a withDefers
        stmts = preStmts +
            if (!currFunc.hasDefer) compileStmtBlock(block).stmts
            else listOf(call(
                expr = "go2k.runtime.withDefers".toDottedExpr(),
                lambda = trailLambda(compileStmtBlock(block).stmts)
            ).toStmt())
    )
}

data class DeclFuncBody(
    val params: List<Node.Decl.Func.Param>,
    val resultType: Node.Type?,
    val stmts: List<Node.Stmt>
)