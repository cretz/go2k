package go2k.compile.compiler

import go2k.compile.go.GNode
import kastree.ast.Node

fun Context.compileStmt(v: GNode.Stmt): List<Node.Stmt> = when (v) {
    is GNode.Stmt.Assign -> compileStmtAssign(v)
    is GNode.Stmt.Block -> listOf(compileStmtBlockStandalone(v))
    is GNode.Stmt.Branch -> listOf(compileStmtBranch(v))
    is GNode.Stmt.Decl -> compileStmtDecl(v)
    is GNode.Stmt.Defer -> listOf(compileStmtDefer(v))
    is GNode.Stmt.Empty -> emptyList()
    is GNode.Stmt.Expr -> listOf(compileStmtExpr(v))
    is GNode.Stmt.For -> listOf(compileStmtFor(v))
    is GNode.Stmt.Go -> listOf(compileStmtGo(v))
    is GNode.Stmt.If -> listOf(compileStmtIf(v))
    is GNode.Stmt.IncDec -> listOf(compileStmtIncDec(v))
    is GNode.Stmt.Labeled -> compileStmtLabeled(v)
    is GNode.Stmt.Range -> listOf(compileStmtRange(v))
    is GNode.Stmt.Return -> listOf(compileStmtReturn(v))
    is GNode.Stmt.Select -> listOf(compileStmtSelect(v))
    is GNode.Stmt.Send -> listOf(compileStmtSend(v))
    is GNode.Stmt.Switch -> listOf(compileStmtSwitch(v))
}

fun Context.compileStmtBranch(v: GNode.Stmt.Branch) = when (v.tok) {
    GNode.Stmt.Branch.Token.BREAK -> Node.Expr.Return(currFunc.breakables.mark(v.label?.name), null).toStmt()
    GNode.Stmt.Branch.Token.CONTINUE -> Node.Expr.Return(currFunc.continuables.mark(v.label?.name), null).toStmt()
    GNode.Stmt.Branch.Token.FALLTHROUGH -> error("Fallthrough not handled here")
    GNode.Stmt.Branch.Token.GOTO -> v.label!!.name.let { label ->
        currFunc.seenGotos += label
        Node.Expr.Return(
            label = currFunc.returnLabelStack.lastOrNull()?.labelIdent(),
            expr = call(label.labelIdent().toName())
        ).toStmt()
    }
}

fun Context.compileStmtDecl(v: GNode.Stmt.Decl) = compileDecl(v.decl, topLevel = false).map {
    // If it's a single property with the blank identifier, just extract the init expr
    val blank = (it as? Node.Decl.Property)?.takeIf { it.vars.singleOrNull()?.name == "_" }?.expr
    blank?.toStmt() ?: it.toStmt()
}

fun Context.compileStmtDefer(v: GNode.Stmt.Defer): Node.Stmt {
    // Evaluate all the args then call the lhs of the call w/ them in the lambda
    val argExprs = v.call.args.map { compileExpr(it) }
    val (combinedArgs, uncombinedArgs) = when {
        argExprs.size == 1 -> argExprs.single() to listOf(currFunc.newTempVar("p"))
        argExprs.isEmpty() -> "kotlin.Unit".toDottedExpr() to emptyList()
        else -> call(
            expr = "go2k.runtime.Tuple${argExprs.size}".toDottedExpr(),
            args = argExprs.map { valueArg(it) }
        ) to argExprs.map { currFunc.newTempVar("p") }
    }
    // Defer is just a call of defer w/ the combined args then the lambda uncombining them
    return call(
        expr = "defer".toName(),
        args = listOf(valueArg(combinedArgs)),
        lambda = trailLambda(
            params =
                if (uncombinedArgs.isEmpty()) emptyList()
                else listOf(uncombinedArgs),
            stmts = listOf(call(
                expr = currFunc.pushDefer().let { compileExpr(v.call.func).also { currFunc.popDefer() } },
                args = uncombinedArgs.map { valueArg(it.toName()) }
            ).toStmt())
        )
    ).toStmt()
}

fun Context.compileStmtExpr(v: GNode.Stmt.Expr) = compileExpr(v.x).toStmt()

fun Context.compileStmtFor(
    v: GNode.Stmt.For,
    label: String? = null
): Node.Stmt = withVarDefSet(v.childVarDefsNeedingRefs()) {
    // Non-range for loops, due to their requirement to support break/continue and that break/continue
    // may be in another run clause or something, we have to use our own style akin to what is explained
    // at https://stackoverflow.com/a/34642869/547546. So we have a run around everything that is where
    // a break breaks out of. Then we have a forLoop fn where a continue returns.

    // Need to call init first because it might def a var
    val initStmts = v.init?.let { compileStmt(it) }.orEmpty()
    // Compile the block and see if anything had break/continue
    currFunc.breakables.push(label)
    currFunc.continuables.push(label)
    val bodyStmts = compileStmtBlock(v.body).stmts
    val (breakLabel, breakCalled) = currFunc.breakables.pop()
    val (continueLabel, continueCalled) = currFunc.continuables.pop()

    val stmts = initStmts + call(
        expr = "go2k.runtime.forLoop".toDottedExpr(),
        args = listOf(
            // Condition or "true"
            valueArg(brace(listOf((v.cond?.let { compileExpr(it) } ?: true.toConst()).toStmt()))),
            // Post or nothing
            valueArg(brace(if (v.post == null) emptyList() else compileStmt(v.post)))
        ),
        lambda = trailLambda(
            label = continueLabel.takeIf { continueCalled },
            stmts = bodyStmts
        )
    ).toStmt()
    // If there is an init or a break label, we have to wrap in a run
    if (stmts.size == 1 && !breakCalled) stmts.single() else call(
        expr = "run".toName(),
        lambda = trailLambda(
            label = breakLabel.takeIf { breakCalled },
            stmts = stmts
        )
    ).toStmt()
}

fun Context.compileStmtGo(v: GNode.Stmt.Go): Node.Stmt {
    // TODO: Until https://youtrack.jetbrains.com/issue/KT-28752 is fixed, we have to inline the launch call
    // Ug, launch is only an expression on the coroutine scope now
    imports += "kotlinx.coroutines.launch" to "go"
    return call(
        expr = "go2k.runtime.goroutineScope.go".toDottedExpr(),
        lambda = trailLambda(listOf(compileExprCall(v.call).toStmt()))
    ).toStmt()
}

fun Context.compileStmtIf(v: GNode.Stmt.If): Node.Stmt.Expr = withVarDefSet(v.childVarDefsNeedingRefs()) {
    // Compile init first to capture var defs
    val initStmts = v.init?.let { compileStmt(it) }.orEmpty()
    var expr: Node.Expr = Node.Expr.If(
        expr = compileExpr(v.cond),
        body = Node.Expr.Brace(emptyList(), compileStmtBlock(v.body)),
        elseBody = v.elseStmt?.let {
            when (it) {
                is GNode.Stmt.Block -> brace(compileStmtBlock(it).stmts)
                is GNode.Stmt.If -> compileStmtIf(it).expr
                else -> error("Unknown else statement type: $it")
            }
        }
    )
    // If there is an init, we are going to do it inside of a run block and then do the if
    if (initStmts.isNotEmpty()) expr = call(
        expr = "run".toName(),
        lambda = trailLambda(initStmts + expr.toStmt())
    )
    expr.toStmt()
}

fun Context.compileStmtIncDec(v: GNode.Stmt.IncDec): Node.Stmt {
    // Named or deref'd does a +/- 1 and may have to convert at the end
    val expr =
        if (v.x is GNode.Expr.Star || v.x.type.nonEntityType() is GNode.Type.Named) {
            var op: Node.Expr = binaryOp(
                lhs = compileExpr(v.x, unfurl = true),
                op = if (v.inc) Node.Expr.BinaryOp.Token.ADD else Node.Expr.BinaryOp.Token.SUB,
                rhs = coerceType(1.toConst(), GNode.Type.Basic("", GNode.Type.Basic.Kind.UNTYPED_INT), v.x.type!!)
            )
            // In Kotlin's case, something like short++ = int so we need that back as a short
            op = compileExprBinaryNarrowByteOrShort(op, v.x.type)
            binaryOp(
                lhs = compileExpr(v.x),
                op = Node.Expr.BinaryOp.Token.ASSN,
                rhs = compileExprToNamed(op, v.x.type)
            )
        } else unaryOp(
            expr = compileExpr(v.x),
            op = if (v.inc) Node.Expr.UnaryOp.Token.INC else Node.Expr.UnaryOp.Token.DEC,
            prefix = false
        )
    return expr.toStmt()
}

fun Context.compileStmtLabeled(v: GNode.Stmt.Labeled): List<Node.Stmt> = when (v.stmt) {
    // Labels on some constructs mean certain things. Otherwise, the labels are handled in other areas.
    is GNode.Stmt.For -> listOf(compileStmtFor(v.stmt, v.label.name))
    is GNode.Stmt.Range -> listOf(compileStmtRange(v.stmt, v.label.name))
    is GNode.Stmt.Select -> listOf(compileStmtSelect(v.stmt, v.label.name))
    is GNode.Stmt.Switch -> listOf(compileStmtSwitch(v.stmt, v.label.name))
    else -> compileStmt(v.stmt)
}

fun Context.compileStmtReturn(v: GNode.Stmt.Return) = Node.Expr.Return(
    label = currFunc.returnLabelStack.lastOrNull()?.labelIdent(),
    expr = v.results.mapIndexed { index, expr ->
        compileExpr(expr, coerceToType = currFunc.type.results[index].type.type)
    }.let {
        var results = it
        // If it's a naked return but there are named returns, return those
        if (results.isEmpty() && currFunc.type.results.isNotEmpty())
            results = currFunc.type.results.flatMap { it.names.map { it.name.toName() } }
        // Return single val or create tuple
        if (results.size <= 1) results.singleOrNull() else call(
            expr = "go2k.runtime.Tuple${results.size}".toDottedExpr(),
            args = results.map { valueArg(it) }
        )
    }
).toStmt()

fun Context.compileStmtSend(v: GNode.Stmt.Send) = call(
    expr = "go2k.runtime.send".toDottedExpr(),
    args = listOf(valueArg(compileExpr(v.chan, unfurl = true)), valueArg(compileExpr(v.value)))
).toStmt()