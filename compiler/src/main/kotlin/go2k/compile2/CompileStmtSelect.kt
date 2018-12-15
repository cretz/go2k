package go2k.compile2

import kastree.ast.Node

fun Context.compileStmtSelect(v: GNode.Stmt.Select, label: String? = null): Node.Stmt {
    // We would use Kotlin's select, but there are two problems: 1) it does have a good "default"
    // approach (could use onTimeout(0) but I fear it's not the same) and 2) the functions accepted
    // by e.g. onReceiveOrNull are not inline so you cannot break/return out of them several levels
    // up. So what we do is for each case, we use a generic when and each one returns true if handled
    // in order.

    // Put the "default" case last if present
    val cases = v.cases.toMutableList()
    val defaultCaseIndex = cases.indexOfFirst { it.comm == null }
    if (defaultCaseIndex >= 0) cases.add(cases.removeAt(defaultCaseIndex))
    // Compile all the when expressions
    currFunc.breakables.push(label ?: "select")
    val whenCondsAndBodies = cases.map { case ->
        // Either a receive (assign or just receive expr) or a send or default
        when (case.comm) {
            // Assign or define vars for receive
            is GNode.Stmt.Assign -> {
                val recv = case.comm.rhs.single() as GNode.Expr.Unary
                require(recv.token == GNode.Expr.Unary.Token.ARROW)
                val chanType = recv.x.type as GNode.Type.Chan
                var lambdaPreStmts = emptyList<Node.Stmt>()
                // Assignment needs temps, define can just use the var names
                val lambdaParamNames =
                    if (case.comm.tok == GNode.Stmt.Assign.Token.DEFINE)
                        case.comm.lhs.map { (it as GNode.Expr.Ident).name.takeIf { it != "_" } }
                    else case.comm.lhs.map { lhsVar ->
                        if (lhsVar is GNode.Expr.Ident && lhsVar.name == "_") null
                        else currFunc.newTempVar((lhsVar as? GNode.Expr.Ident)?.name ?: "temp").also { tempVar ->
                            lambdaPreStmts += binaryOp(
                                lhs = compileExpr(lhsVar),
                                op = Node.Expr.BinaryOp.Token.ASSN,
                                rhs = tempVar.toName()
                            ).toStmt()
                        }
                    }
                // Different call depending on whether there are two assignments or one
                call(
                    expr =
                        if (case.comm.lhs.size == 1) "go2k.runtime.builtin.selectRecv".toDottedExpr()
                        else "go2k.runtime.builtin.selectRecvWithOk".toDottedExpr(),
                    args = listOf(valueArg(compileExpr(recv.x)), valueArg(compileTypeZeroExpr(chanType.elem))),
                    lambda = trailLambda(
                        params = lambdaParamNames.map { listOf(it) },
                        stmts = lambdaPreStmts + case.body.flatMap { compileStmt(it) }
                    )
                ) to emptyList<Node.Stmt>()
            }
            // Receive without vars
            is GNode.Stmt.Expr -> {
                val recv = case.comm.x as GNode.Expr.Unary
                require(recv.token == GNode.Expr.Unary.Token.ARROW)
                val chanType = recv.x.type as GNode.Type.Chan
                call(
                    expr = "go2k.runtime.builtin.selectRecv".toDottedExpr(),
                    args = listOf(valueArg(compileExpr(recv.x)), valueArg(compileTypeZeroExpr(chanType.elem))),
                    lambda = trailLambda(case.body.flatMap { compileStmt(it) })
                ) to emptyList<Node.Stmt>()
            }
            // Single send
            is GNode.Stmt.Send -> {
                call(
                    expr = "go2k.runtime.builtin.selectSend".toDottedExpr(),
                    args = listOf(valueArg(compileExpr(case.comm.chan)), valueArg(compileExpr(case.comm.value))),
                    lambda = trailLambda(case.body.flatMap { compileStmt(it) })
                ) to emptyList<Node.Stmt>()
            }
            null -> null to case.body.flatMap { compileStmt(it) }
            else -> error("Unknown case ${case.comm}")
        }
    }
    // Have to wrap in a run because we break on it
    val (breakLabel, _) = currFunc.breakables.pop()
    return call(
        expr = "run".toDottedExpr(),
        lambda = trailLambda(label = breakLabel, stmts = listOf(call(
            expr = "go2k.runtime.builtin.select".toDottedExpr(),
            lambda = trailLambda(listOf(Node.Expr.When(
                expr = null,
                entries = whenCondsAndBodies.map { (whenCond, additionalBodyStmts) ->
                    Node.Expr.When.Entry(
                        conds = if (whenCond == null) emptyList() else listOf(Node.Expr.When.Cond.Expr(whenCond)),
                        body = Node.Expr.Return(breakLabel, null).let { returnExpr ->
                            if (additionalBodyStmts.isEmpty()) returnExpr
                            else brace(additionalBodyStmts + returnExpr.toStmt())
                        }
                    )
                }
            ).toStmt()))
        ).toStmt()))
    ).toStmt()
}