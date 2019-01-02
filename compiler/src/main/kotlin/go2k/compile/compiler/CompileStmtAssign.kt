package go2k.compile.compiler

import go2k.compile.go.GNode
import kastree.ast.Node

fun Context.compileStmtAssign(v: GNode.Stmt.Assign) = when {
    v.tok == GNode.Stmt.Assign.Token.DEFINE -> compileStmtAssignDefine(v)
    v.lhs.size > 1 -> compileStmtAssignMulti(v)
    else -> compileStmtAssignSingle(v)
}

fun Context.compileStmtAssignBinaryOp(
    origLhsType: GNode.Type?,
    lhs: Node.Expr,
    op: Node.Expr.BinaryOp.Token,
    origRhsType: GNode.Type?,
    rhs: Node.Expr
): Node.Expr {
    var properRhs = rhs
    // Coerce it
    properRhs = coerceType(properRhs, origRhsType, origLhsType)
    // Copy for by-value needs
    properRhs = coerceTypeForByValueCopy(origRhsType, properRhs)
    // Do the op
    return binaryOp(lhs, op, properRhs)
}

fun Context.compileStmtAssignDefine(v: GNode.Stmt.Assign): List<Node.Stmt> {
    // Single defines are normal properties. Multi-defines as results of
    // functions are destructurings. Multi-defines with multi-rhs are just
    // one at a time.
    // Identifiers already defined and as a result of a function use temps
    val multiDefineSingleRhs = v.lhs.size > 1 && v.rhs.size == 1
    // Sometimes, depending on LHS type, we have to wrap the RHS in a ref)
    fun rhsAsRef(origLhs: String, rhsExpr: Node.Expr) =
        if (!markVarDef(origLhs)) rhsExpr
        else call(expr = "go2k.runtime.GoRef".toDottedExpr(), args = listOf(valueArg(rhsExpr)))
    fun rhsAsRef(origLhs: GNode.Expr, rhsExpr: Node.Expr) = rhsAsRef((origLhs as GNode.Expr.Ident).name, rhsExpr)
    // Key is var name, val is temp name
    var identsUsingTemps = emptyList<Pair<String, String>>()
    val idents = v.lhs.map { ident ->
        ident as GNode.Expr.Ident
        // Needs temp if multi-define and it's either really defined before or it's a ref
        if (multiDefineSingleRhs && (ident.defType == null || varDefWillBeRef(ident.name))) {
            currFunc.newTempVar(ident.name).also { identsUsingTemps += ident.name to it }
        } else ident.name
    }
    val neededIdentTypes = v.lhs.map { ident ->
        ident as GNode.Expr.Ident
        // We only need types if not multi-define-single-rhs and they're nullable
        if (multiDefineSingleRhs || ident.defType.nonEntityType()?.isNullable != true) null else {
            val type = compileType(ident.defType!!)
            // Could need to be a ref
            if (!varDefWillBeRef(ident.name)) type else "go2k.runtime.GoRef".toDottedType(type)
        }
    }
    val stmts = v.rhs.mapIndexed { index, expr ->
        when {
            // If the ident is an underscore, we only do the RHS
            idents.singleOrNull() == "_" -> compileExpr(expr).toStmt()
            // If we're not a multi-on-single and we didn't define it here, just assign it
            !multiDefineSingleRhs && (v.lhs[index] as GNode.Expr.Ident).defType == null -> compileStmtAssignBinaryOp(
                origLhsType = v.lhs[index].type,
                lhs = idents[index].toName(),
                op = Node.Expr.BinaryOp.Token.ASSN,
                origRhsType = expr.type,
                rhs = rhsAsRef(v.lhs[index], compileExpr(expr))
            ).toStmt()
            // Otherwise, just a property
            else -> property(
                vars =
                    if (multiDefineSingleRhs) idents.mapIndexed { lhsIndex, ident ->
                        if (ident == "_") null
                        else propVar(ident, neededIdentTypes[lhsIndex])
                    }
                    else listOf(propVar(idents[index], neededIdentTypes[index])),
                expr =
                    if (multiDefineSingleRhs) compileExpr(expr)
                    else rhsAsRef(v.lhs[index], compileExpr(expr, coerceTo = v.lhs[index], byValue = true))
            ).toStmt()
        }
    }
    // Now assign the temps
    return stmts + identsUsingTemps.map { (ident, temp) ->
        binaryOp(
            lhs = ident.toName(),
            op = Node.Expr.BinaryOp.Token.ASSN,
            rhs = rhsAsRef(ident, temp.toName())
        ).toStmt()
    }
}

fun Context.compileStmtAssignMulti(v: GNode.Stmt.Assign): List<Node.Stmt> {
    // If the multiple assignment is a result of a function, we make a temp var later
    // and do the destructuring ourselves
    val rhsExprsAndTypes =
        if (v.rhs.size != 1) v.rhs.map { compileExpr(it) to it.type }
        else v.lhs.indices.map {
            call("\$temp".toName().dot("component${it + 1}")) to
                (v.rhs.single().type as? GNode.Type.Tuple)?.vars?.getOrNull(it)?.type
        }
    // For multi-assign we use our helpers. They are based on whether there needs to be
    // an eager LHS and what it is.
    val multiAssignCall = call(
        expr = "go2k.runtime.Assign.multi".toDottedExpr(),
        args = v.lhs.zip(rhsExprsAndTypes) zip@ { lhs, (rhsExpr, rhsType) ->
            val eagerLhsExpr: Node.Expr?
            val assignLambdaParams: List<List<String>>
            val assignLambdaLhsExpr: Node.Expr
            when (lhs) {
                // For LHS selects, we eagerly eval the LHS
                is GNode.Expr.Selector -> {
                    eagerLhsExpr = compileExpr(lhs.x)
                    assignLambdaParams = listOf(listOf("\$lhs"), listOf("\$rhs"))
                    assignLambdaLhsExpr = "\$lhs".toName().dot(lhs.sel.name)
                }
                // For indexes, we eagerly eval the LHS and the index and then reference
                is GNode.Expr.Index -> {
                    eagerLhsExpr = binaryOp(
                        lhs = compileExpr(lhs.x),
                        op = "to".toInfix(),
                        rhs = compileExpr(lhs.index)
                    )
                    assignLambdaParams = listOf(listOf("\$lhs", "\$index"), listOf("\$rhs"))
                    var properLhs: Node.Expr = "\$lhs".toName()
                    if (lhs.x.type.nonEntityType()?.isNullable == true) properLhs = properLhs.nullDeref()
                    assignLambdaLhsExpr = properLhs.index("\$index".toName())
                }
                // For the rest, there is no eager LHS
                else -> {
                    eagerLhsExpr = null
                    assignLambdaParams = emptyList()
                    // If this is an underscore, there is no assignment
                    assignLambdaLhsExpr =
                        if ((lhs as? GNode.Expr.Ident)?.name == "_") return@zip null
                        else compileExpr(lhs)
                }
            }
            // Now that we have configured the assignment, call it
            val assignParams = listOfNotNull(
                eagerLhsExpr,
                brace(
                    params = assignLambdaParams.map { it.map { propVar(it) } },
                    stmts = listOf(compileStmtAssignBinaryOp(
                        origLhsType = lhs.type,
                        lhs = assignLambdaLhsExpr,
                        op = Node.Expr.BinaryOp.Token.ASSN,
                        origRhsType = rhsType,
                        rhs = (if (assignLambdaParams.isEmpty()) "it" else "\$rhs").toName()
                    ).toStmt())
                ),
                brace(listOf(rhsExpr.toStmt()))
            )
            valueArg(call(
                expr = "go2k.runtime.Assign.assign".toDottedExpr(),
                args = assignParams.map { valueArg(it) }
            ))
        }.filterNotNull()
    )
    // Wrap in a also if it's a function result instead of normal multi assign
    return if (v.rhs.size > 1) listOf(multiAssignCall.toStmt()) else listOf(call(
        expr = compileExpr(v.rhs.single(), byValue = true).dot("also"),
        lambda = trailLambda(
            params = listOf(listOf("\$temp")),
            stmts = listOf(multiAssignCall.toStmt())
        )
    ).toStmt())
}

fun Context.compileStmtAssignSingle(v: GNode.Stmt.Assign): List<Node.Stmt> {
    // For cases where we have non-arith-binary-op assigns, just unwrap it to regular assign
    fun unwrapRhs(newOp: GNode.Expr.Binary.Token) =
        GNode.Stmt.Assign.Token.ASSIGN to v.lhs.zip(v.rhs) { lhs, rhs -> GNode.Expr.Binary(null, lhs, newOp, rhs) }
    val (tok, rhs) = when (v.tok) {
        GNode.Stmt.Assign.Token.AND -> unwrapRhs(GNode.Expr.Binary.Token.AND)
        GNode.Stmt.Assign.Token.OR -> unwrapRhs(GNode.Expr.Binary.Token.OR)
        GNode.Stmt.Assign.Token.XOR -> unwrapRhs(GNode.Expr.Binary.Token.XOR)
        GNode.Stmt.Assign.Token.SHL -> unwrapRhs(GNode.Expr.Binary.Token.SHL)
        GNode.Stmt.Assign.Token.SHR -> unwrapRhs(GNode.Expr.Binary.Token.SHR)
        GNode.Stmt.Assign.Token.AND_NOT -> unwrapRhs(GNode.Expr.Binary.Token.AND_NOT)
        else -> v.tok to v.rhs
    }

    return listOf(compileStmtAssignBinaryOp(
        origLhsType = v.lhs.single().type,
        lhs = compileExpr(v.lhs.single()),
        op = when (tok) {
            GNode.Stmt.Assign.Token.ASSIGN -> Node.Expr.BinaryOp.Token.ASSN
            GNode.Stmt.Assign.Token.ADD -> Node.Expr.BinaryOp.Token.ADD_ASSN
            GNode.Stmt.Assign.Token.SUB -> Node.Expr.BinaryOp.Token.SUB_ASSN
            GNode.Stmt.Assign.Token.MUL -> Node.Expr.BinaryOp.Token.MUL_ASSN
            GNode.Stmt.Assign.Token.QUO -> Node.Expr.BinaryOp.Token.DIV_ASSN
            GNode.Stmt.Assign.Token.REM -> Node.Expr.BinaryOp.Token.MOD_ASSN
            else -> error("Unrecognized token: $tok")
        },
        origRhsType = v.rhs.single().type,
        rhs = compileExpr(rhs.single())
    ).toStmt())
}