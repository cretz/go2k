package go2k.compile2

import kastree.ast.Node

fun Context.compileStmt(v: GNode.Stmt): List<Node.Stmt> = when (v) {
    is GNode.Stmt.Assign -> compileStmtAssign(v)
    is GNode.Stmt.Block -> TODO()
    is GNode.Stmt.Branch -> TODO()
    is GNode.Stmt.Decl -> TODO()
    is GNode.Stmt.Defer -> TODO()
    is GNode.Stmt.Empty -> TODO()
    is GNode.Stmt.Expr -> TODO()
    is GNode.Stmt.For -> TODO()
    is GNode.Stmt.Go -> TODO()
    is GNode.Stmt.If -> TODO()
    is GNode.Stmt.IncDec -> TODO()
    is GNode.Stmt.Labeled -> TODO()
    is GNode.Stmt.Range -> TODO()
    is GNode.Stmt.Return -> TODO()
    is GNode.Stmt.Select -> TODO()
    is GNode.Stmt.Send -> TODO()
    is GNode.Stmt.Switch -> TODO()
}