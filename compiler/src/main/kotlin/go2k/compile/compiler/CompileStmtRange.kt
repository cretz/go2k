package go2k.compile.compiler

import go2k.compile.go.GNode
import kastree.ast.Node

fun Context.compileStmtRange(
    v: GNode.Stmt.Range,
    label: String? = null
): Node.Stmt = withVarDefSet(v.childVarDefsNeedingRefs()) {
    // Range loops are first surrounded by a run for breaking and then labeled for continuing.
    // They are iterated via forEach and forEachIndexed depending upon what is ranged on.
    val hasKeyParam = v.key != null && (v.key !is GNode.Expr.Ident || v.key.name != "_")
    val hasValParam = v.value != null && (v.value !is GNode.Expr.Ident || v.value.name != "_")
    var keyParamName = (v.key as? GNode.Expr.Ident)?.name?.takeIf { it != "_" }
    var valParamName = (v.value as? GNode.Expr.Ident)?.name?.takeIf { it != "_" }
    val keyValDestructured: Boolean
    val forEachFnName: String
    val forEachFnIsMember: Boolean
    when (v.x.type.unnamedType()) {
        is GNode.Type.Array, is GNode.Type.Basic -> {
            forEachFnName = if (hasKeyParam) "forEachIndexed" else "forEach"
            forEachFnIsMember = true
            keyValDestructured = false
        }
        is GNode.Type.Slice -> {
            forEachFnName = "go2k.runtime." + if (hasKeyParam) "forEachIndexed" else "forEach"
            forEachFnIsMember = false
            keyValDestructured = false
        }
        is GNode.Type.Map -> {
            forEachFnName = "forEach"
            forEachFnIsMember = true
            keyValDestructured = true
        }
        else -> error("Unknown range type ${v.x.type}")
    }
    // Build lambda params
    var initStmts = emptyList<Node.Stmt>()
    var preStmts = emptyList<Node.Stmt>()
    // Define uses lambda params, otherwise we set from temp vars
    if (!v.define) {
        if (hasKeyParam) {
            initStmts += binaryOp(compileExpr(v.key!!), Node.Expr.BinaryOp.Token.ASSN, "\$tempKey".toName()).toStmt()
            keyParamName = "\$tempKey"
        }
        if (hasValParam) {
            initStmts += binaryOp(compileExpr(v.value!!), Node.Expr.BinaryOp.Token.ASSN, "\$tempVal".toName()).toStmt()
            valParamName = "\$tempVal"
        }
    } else {
        // Wait, if the define are refs, we also have to use temps
        if (keyParamName != null && markVarDef(keyParamName)) {
            val type = (v.key as GNode.Expr.Ident).let { it.type ?: it.defType }!!
            preStmts += property(
                vars = listOf(propVar(keyParamName)),
                expr = call(
                    expr = "go2k.runtime.GoRef".toDottedExpr(),
                    args = listOf(valueArg(compileTypeZeroExpr(type)))
                )
            ).toStmt()
            initStmts += binaryOp(compileExpr(v.key), Node.Expr.BinaryOp.Token.ASSN, "\$tempKey".toName()).toStmt()
            keyParamName = "\$tempKey"
        }
        if (valParamName != null && markVarDef(valParamName)) {
            val type = (v.value as GNode.Expr.Ident).let { it.type ?: it.defType }!!
            preStmts += property(
                vars = listOf(propVar(valParamName)),
                expr = call(
                    expr = "go2k.runtime.GoRef".toDottedExpr(),
                    args = listOf(valueArg(compileTypeZeroExpr(type)))
                )
            ).toStmt()
            initStmts += binaryOp(compileExpr(v.value), Node.Expr.BinaryOp.Token.ASSN, "\$tempVal".toName()).toStmt()
            valParamName = "\$tempVal"
        }
    }
    val params = when {
        keyValDestructured -> listOf(listOf(keyParamName, valParamName))
        keyParamName != null -> listOf(listOf(keyParamName), listOf(valParamName))
        valParamName != null -> listOf(listOf(valParamName))
        else -> emptyList()
    }

    // Check body for any break/continue calls
    currFunc.breakables.push(label)
    currFunc.continuables.push(label)
    val bodyStmts = compileStmtBlock(v.body!!).stmts
    val (breakLabel, breakCalled) = currFunc.breakables.pop()
    val (continueLabel, continueCalled) = currFunc.continuables.pop()

    var stmt = call(
        expr = forEachFnName.toDottedExpr().let { if (forEachFnIsMember) compileExpr(v.x).dot(it) else it },
        args = if (forEachFnIsMember) emptyList() else listOf(valueArg(compileExpr(v.x))),
        lambda = trailLambda(
            label = continueLabel.takeIf { continueCalled },
            params = params,
            stmts = initStmts + bodyStmts
        )
    ).toStmt()
    if (breakCalled || preStmts.isNotEmpty()) stmt = call(
        expr = "run".toName(),
        lambda = trailLambda(label = breakLabel.takeIf { breakCalled }, stmts = preStmts + listOf(stmt))
    ).toStmt()
    stmt
}