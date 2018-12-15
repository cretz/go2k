package go2k.compile2

import kastree.ast.Node

fun Context.compileStmtSwitch(v: GNode.Stmt.Switch, label: String? = null) =
    if (v.type) compileStmtSwitchType(v, label) else compileStmtSwitchExpr(v, label)

fun Context.compileStmtSwitchExpr(v: GNode.Stmt.Switch, label: String? = null): Node.Stmt {
    // Track break usage
    currFunc.breakables.push(label)
    val entries = v.cases.mapIndexed { index, case ->
        // Body includes all fallthroughs as duplicate code in run blocks
        var body = listOf(case.body)
        for (i in index + 1 until v.cases.size) {
            val lastStmt = body.last().lastOrNull()
            if ((lastStmt as? GNode.Stmt.Branch)?.tok != GNode.Stmt.Branch.Token.FALLTHROUGH) break
            body = body.dropLast(1).plusElement(body.last().dropLast(1)).plusElement(v.cases[i].body)
        }
        Node.Expr.When.Entry(
            conds = case.list.map { compileExpr(it) }.let { conds ->
                // If there is no expr, the expressions are separated with || in a single cond
                if (v.tag != null || conds.isEmpty()) conds.map { Node.Expr.When.Cond.Expr(it) }
                else listOf(Node.Expr.When.Cond.Expr(
                    conds.drop(1).fold(conds.first()) { lhs, rhs ->
                        binaryOp(lhs, Node.Expr.BinaryOp.Token.OR, rhs)
                    }
                ))
            },
            body = brace(body.first().flatMap { compileStmt(it) } + body.drop(1).map {
                // Fallthrough
                call(expr = "run".toName(), lambda = trailLambda(it.flatMap { compileStmt(it) })).toStmt()
            })
        )
    }.toMutableList()
    val (breakLabel, breakCalled) = currFunc.breakables.pop()
    // Put default at the end
    entries.indexOfFirst { it.conds.isEmpty() }.also { if (it >= 0) entries += entries.removeAt(it) }
    // Create the when
    var stmt = Node.Expr.When(
        expr = (v.tag as? GNode.Stmt.Expr)?.let { compileExpr(it.x) },
        entries = entries
    ).toStmt()
    // If there is an init or break, we do it in a run clause. Note, we cannot use when-with-subject-decl
    // here when it applies because Kotlin only allows "val" whereas Go allows mutable var.
    if (v.init != null || breakCalled) stmt = call(
        expr = "run".toName(),
        lambda = trailLambda(
            label = breakLabel.takeIf { breakCalled },
            stmts = (v.init?.let { compileStmt(it) } ?: emptyList()) + stmt
        )
    ).toStmt()
    return stmt
}

fun Context.compileStmtSwitchType(v: GNode.Stmt.Switch, label: String? = null): Node.Stmt = TODO()