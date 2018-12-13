package go2k.compile

import go2k.compile.dumppb.*
import go2k.runtime.GoStruct
import go2k.runtime.Ops
import go2k.runtime.Slice
import go2k.runtime.runMain
import kastree.ast.Node
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.max

@ExperimentalUnsignedTypes
open class Compiler {

    // All functions herein start with "compile" and are alphabetically ordered

    fun Context.compileArrayCreate(elemType: TypeConverter.Type, len: Int) = when {
        // Primitive is PrimArray(size), string is Array(size) { "" }, other is arrayOfNulls<Type>(size)
        elemType !is TypeConverter.Type.Primitive -> call(
            expr = "kotlin.collection.arrayOfNulls".toDottedExpr(),
            typeArgs = listOf(compileType(elemType.type)),
            args = listOf(valueArg(expr = len.toConst()))
        )
        elemType.cls == String::class -> call(
            expr = "kotlin.Array".toDottedExpr(),
            args = listOf(valueArg(expr = len.toConst())),
            lambda = trailLambda(stmts = listOf("".toStringTmpl().toStmt()))
        )
        else -> call(
            expr = elemType.cls.primitiveArrayClass()!!.qualifiedName!!.toDottedExpr(),
            args = listOf(valueArg(expr = len.toConst()))
        )
    }

    fun Context.compileArrayLiteral(type: TypeConverter.Type.Array, elems: List<Expr_>) =
        compileArrayLiteral(type.elemType, type.len.toInt(), elems)

    fun Context.compileArrayLiteral(
        elemType: TypeConverter.Type,
        explicitLen: Int?,
        elems: List<Expr_>
    ): Node.Expr {
        // There are two types of array literals here. One that has no indexes,
        // uses every slot, and the length of literals matches the given length
        // (or there is no given length). The other creates for a certain length
        // and then sets explicit indexes in an "apply" block.
        val simpleArrayLiteral = (explicitLen == null || elems.size == explicitLen) &&
            elems.all { it.expr !is Expr_.Expr.KeyValueExpr }
        return if (simpleArrayLiteral) {
            call(
                expr = ((elemType as? TypeConverter.Type.Primitive)?.cls ?: Any::class).
                    arrayOfQualifiedFunctionName().funcRef(),
                args = elems.map { valueArg(expr = compileExpr(it).convertType(it, elemType)) }
            )
        } else {
            // Get the pairs, in order, and the overall len
            val elemPairs = elems.fold(emptyList<Pair<Int, Node.Expr>>()) { pairs, elem ->
                pairs + if (elem.expr is Expr_.Expr.KeyValueExpr) {
                    val key = elem.expr.keyValueExpr.key?.expr?.typeRef?.typeConst?.constInt ?: error("Missing key")
                    key to compileExpr(elem.expr.keyValueExpr.value!!)
                } else {
                    val key = pairs.lastOrNull()?.first?.plus(1) ?: 0
                    key to compileExpr(elem)
                }
            }
            val len = explicitLen ?: elemPairs.fold(0) { len, (index, _) -> max(len, index + 1) }
            // Create the array and then set the specific indices in the apply block
            call(
                expr = compileArrayCreate(elemType, len).dot("apply".toName()),
                lambda = trailLambda(stmts = elemPairs.map { (index, elem) ->
                    call(
                        expr = "set".toName(),
                        args = listOf(valueArg(expr = index.toConst()), valueArg(expr = elem))
                    ).toStmt()
                })
            )
        }
    }

    fun Context.compileAssignDefineStmt(v: AssignStmt): List<Node.Stmt> {
        // Single defines are normal properties. Multi-defines as results of
        // functions are destructurings. Multi-defines with multi-rhs are just
        // one at a time.
        // Identifiers already defined and as a result of a function use temps
        val multiDefineSingleRhs = v.lhs.size > 1 && v.rhs.size == 1
        // Key is var name, val is temp name
        var identsUsingTemps = emptyList<Pair<String, String>>()
        val idents = v.lhs.map {
            val ident = (it.expr as Expr_.Expr.Ident).ident
            if (multiDefineSingleRhs && ident.defTypeRef == null) {
                currFunc.newTempVar(ident.name.javaIdent).also { identsUsingTemps += ident.name.javaIdent to it }
            } else ident.name.javaIdent
        }
        val stmts = v.rhs.mapIndexed { index, expr ->
            when {
                // If the ident is an underscore, we only do the RHS
                idents.singleOrNull() == "_" -> compileExpr(expr).toStmt()
                // If we're not a multi-on-single and we didn't define it here, just assign it
                !multiDefineSingleRhs && (v.lhs[index].expr as Expr_.Expr.Ident).ident.defTypeRef == null -> binaryOp(
                    lhs = idents[index].javaIdent.toName(),
                    op = Node.Expr.BinaryOp.Token.ASSN,
                    rhs = compileExpr(expr).convertType(expr, v.lhs[index]).byValue(expr)
                ).toStmt()
                // Otherwise, just a property
                else -> Node.Stmt.Decl(property(
                    vars =
                        if (multiDefineSingleRhs) idents.map { if (it == "_") null else propVar(it) }
                        else listOf(propVar(idents[index])),
                    expr = compileExpr(expr).let {
                        if (multiDefineSingleRhs) it else it.convertType(expr, v.lhs[index]).byValue(expr)
                    }
                ))
            }
        }
        // Now assign the temps
        return stmts + identsUsingTemps.map { (ident, temp) ->
            binaryOp(
                lhs = ident.toName(),
                op = Node.Expr.BinaryOp.Token.ASSN,
                rhs = temp.toName()
            ).toStmt()
        }
    }

    fun Context.compileAssignMultiStmt(v: AssignStmt): List<Node.Stmt> {
        // If the multiple assignment is a result of a function, we make a temp var later
        // and do the destructuring ourselves
        val rhsExprs = if (v.rhs.size == 1) v.lhs.indices.map {
            call("\$temp".toName().dot("component${it + 1}".toName()))
        } else v.rhs.map { compileExpr(it).byValue(it) }
        // For multi-assign we use our helpers. They are based on whether there needs to be
        // an eager LHS and what it is.
        val multiAssignCall = call(
            expr = "go2k.runtime.Assign.multi".toDottedExpr(),
            args = v.lhs.zip(rhsExprs) zip@ { lhs, rhsExpr ->
                val eagerLhsExpr: Node.Expr?
                val assignLambdaParams: List<List<String>>
                val assignLambdaLhsExpr: Node.Expr
                when (lhs.expr) {
                    // For LHS selects, we eagerly eval the LHS
                    is Expr_.Expr.SelectorExpr -> {
                        eagerLhsExpr = compileExpr(lhs.expr.selectorExpr.x!!)
                        assignLambdaParams = listOf(listOf("\$lhs"), listOf("\$rhs"))
                        assignLambdaLhsExpr = "\$lhs".toName().dot(lhs.expr.selectorExpr.sel!!.name.javaIdent.toName())
                    }
                    // For indexes, we eagerly eval the LHS and the index and then reference
                    is Expr_.Expr.IndexExpr -> {
                        eagerLhsExpr = binaryOp(
                            lhs = compileExpr(lhs.expr.indexExpr.x!!),
                            op = Node.Expr.BinaryOp.Oper.Infix("to"),
                            rhs = compileExpr(lhs.expr.indexExpr.index!!)
                        )
                        assignLambdaParams = listOf(listOf("\$lhs", "\$index"), listOf("\$rhs"))
                        assignLambdaLhsExpr = "\$lhs".toName().index("\$index".toName())
                    }
                    // For the rest, there is no eager LHS
                    else -> {
                        eagerLhsExpr = null
                        assignLambdaParams = emptyList()
                        // If this is an underscore, there is no assignment
                        assignLambdaLhsExpr =
                            if ((lhs.expr as? Expr_.Expr.Ident)?.ident?.name == "_") return@zip null
                            else compileExpr(lhs.expr!!)
                    }
                }
                // Now that we have configured the assignment, call it
                var assignParams = emptyList<Node.Expr>()
                if (eagerLhsExpr != null) assignParams += eagerLhsExpr
                assignParams += Node.Expr.Brace(
                    params = assignLambdaParams.map {
                        Node.Expr.Brace.Param(it.map { Node.Decl.Property.Var(it, null) }, null)
                    },
                    block = Node.Block(listOf(
                        binaryOp(
                            lhs = assignLambdaLhsExpr,
                            op = Node.Expr.BinaryOp.Token.ASSN,
                            rhs = (if (assignLambdaParams.isEmpty()) "it" else "\$rhs").toName()
                        ).toStmt()
                    ))
                )
                assignParams += Node.Expr.Brace(emptyList(), Node.Block(listOf(rhsExpr.toStmt())))
                valueArg(expr = call(
                    expr = "go2k.runtime.Assign.assign".toDottedExpr(),
                    args = assignParams.map { valueArg(expr = it) }
                ))
            }.filterNotNull()
        )
        // Wrap in a also if it's a function result instead of normal multi assign
        return if (v.rhs.size > 1) listOf(multiAssignCall.toStmt()) else listOf(call(
            expr = compileExpr(v.rhs.single()).byValue(v.rhs.single()).dot("also".toName()),
            lambda = trailLambda(
                params = listOf(Node.Expr.Brace.Param(listOf(Node.Decl.Property.Var("\$temp", null)), null)),
                stmts = listOf(multiAssignCall.toStmt())
            )
        ).toStmt())
    }

    fun Context.compileAssignStmt(v: AssignStmt): List<Node.Stmt> {
        // Handle decls elsewhere
        if (v.tok == Token.DEFINE) return compileAssignDefineStmt(v)
        // Handle multi-assign elsewhere
        if (v.lhs.size > 1) return compileAssignMultiStmt(v)

        // For cases where we have non-arith-binary-op assigns, just unwrap it to regular assign
        fun unwrapRhs(newOp: Token) = Token.ASSIGN to v.lhs.zip(v.rhs) { lhs, rhs ->
            Expr_(Expr_.Expr.BinaryExpr(BinaryExpr(x = lhs, op = newOp, y = rhs)))
        }
        val (tok, rhs) = when (v.tok) {
            Token.AND_ASSIGN -> unwrapRhs(Token.AND)
            Token.OR_ASSIGN -> unwrapRhs(Token.OR)
            Token.XOR_ASSIGN -> unwrapRhs(Token.XOR)
            Token.SHL_ASSIGN -> unwrapRhs(Token.SHL)
            Token.SHR_ASSIGN -> unwrapRhs(Token.SHR)
            Token.AND_NOT_ASSIGN -> unwrapRhs(Token.AND_NOT)
            else -> v.tok to v.rhs
        }

        return listOf(Node.Stmt.Expr(binaryOp(
            lhs = compileExpr(v.lhs.single()),
            op = when (tok) {
                Token.ASSIGN -> Node.Expr.BinaryOp.Token.ASSN
                Token.ADD_ASSIGN -> Node.Expr.BinaryOp.Token.ADD_ASSN
                Token.SUB_ASSIGN -> Node.Expr.BinaryOp.Token.SUB_ASSN
                Token.MUL_ASSIGN -> Node.Expr.BinaryOp.Token.MUL_ASSN
                Token.QUO_ASSIGN -> Node.Expr.BinaryOp.Token.DIV_ASSN
                Token.REM_ASSIGN -> Node.Expr.BinaryOp.Token.MOD_ASSN
                else -> error("Unrecognized token: $tok")
            },
            rhs = compileExpr(rhs.single()).convertType(rhs.single(), v.lhs.single()).byValue(rhs.single())
        )))
    }

    fun Context.compileBasicLit(v: BasicLit) =  when (v.kind) {
        Token.INT, Token.FLOAT -> compileConstantValue(v.typeRef?.typeConst!!)
        Token.IMAG -> TODO()
        Token.CHAR -> v.typeRef!!.namedType.convType().let { expectedType ->
            // Sometimes a char is used as an int (e.g. an array index), so if the type
            // is int, we have to treat it as such.
            if (expectedType is TypeConverter.Type.Primitive && expectedType.cls == Int::class)
                compileConstantValue(v.typeRef.typeConst!!)
            else (v.typeRef.typeConst?.constInt ?: error("Invalid const int")).let { int ->
                constExpr("'" + compileBasicLitChar(int.toChar()) + "'", Node.Expr.Const.Form.CHAR)
            }
        }
        Token.STRING -> (v.typeRef?.typeConst?.constString ?: error("Invalid const string")).let { str ->
            val raw = v.value.startsWith('`')
            str.fold("") { str, char ->
                str + compileBasicLitChar(char, str = true, raw = raw)
            }.toStringTmpl(raw)
        }
        else -> error("Unrecognized lit kind: ${v.kind}")
    }

    fun Context.compileBasicLitChar(v: Char, str: Boolean = false, raw: Boolean = false) =
        if (raw) { if (v == '$') "\${'\$'}" else v.toString() }
        else when (v) {
            '\\' -> "\\\\"
            '\t' -> "\\t"
            '\b' -> "\\b"
            '\n' -> "\\n"
            '\r' -> "\\r"
            '"' -> if (str) "\\\"" else v.toString()
            '\'' -> if (str) v.toString() else "\\'"
            '$' -> "\\\$"
            // Escape unprintables
            else ->
                if (raw || v >= ' ') v.toString()
                else "\\u00" + v.toInt().toString(16).toUpperCase().let { if (it.length == 2) it else "0$it" }
        }

    fun Context.compileBinaryExpr(v: BinaryExpr) = when (v.op) {
        Token.EQL -> call(
            expr = Ops::class.ref().dot("eql".toName()),
            args = listOf(
                valueArg(expr = compileExpr(v.x!!)),
                valueArg(expr = compileExpr(v.y!!).convertType(v.y, v.x))
            )
        )
        Token.NEQ -> call(
            expr = Ops::class.ref().dot("neq".toName()),
            args = listOf(
                valueArg(expr = compileExpr(v.x!!)),
                valueArg(expr = compileExpr(v.y!!).convertType(v.y, v.x))
            )
        )
        Token.AND_NOT -> binaryOp(
            lhs = compileExpr(v.x!!),
            op = "and".toInfix(),
            rhs = call(compileExpr(v.y!!).dot("inv".toName())).convertType(v.y, v.x)
        )
        Token.AND_ASSIGN -> TODO()
        Token.OR_ASSIGN -> TODO()
        Token.XOR_ASSIGN -> TODO()
        Token.SHL_ASSIGN -> TODO()
        Token.SHR_ASSIGN -> TODO()
        Token.AND_NOT_ASSIGN -> TODO()
        Token.DEFINE -> TODO()
        else -> binaryOp(
            lhs = compileExpr(v.x!!),
            op = when (v.op) {
                Token.ADD -> Node.Expr.BinaryOp.Token.ADD.toOper()
                Token.SUB -> Node.Expr.BinaryOp.Token.SUB.toOper()
                Token.MUL -> Node.Expr.BinaryOp.Token.MUL.toOper()
                Token.QUO -> Node.Expr.BinaryOp.Token.DIV.toOper()
                Token.REM -> Node.Expr.BinaryOp.Token.MOD.toOper()
                Token.AND -> "and".toInfix()
                Token.OR -> "or".toInfix()
                Token.XOR -> "xor".toInfix()
                Token.SHL -> "shl".toInfix()
                Token.SHR -> "shr".toInfix()
                Token.ADD_ASSIGN -> Node.Expr.BinaryOp.Token.ADD_ASSN.toOper()
                Token.SUB_ASSIGN -> Node.Expr.BinaryOp.Token.SUB_ASSN.toOper()
                Token.MUL_ASSIGN -> Node.Expr.BinaryOp.Token.MUL_ASSN.toOper()
                Token.QUO_ASSIGN -> Node.Expr.BinaryOp.Token.DIV_ASSN.toOper()
                Token.REM_ASSIGN -> Node.Expr.BinaryOp.Token.MOD_ASSN.toOper()
                Token.LAND -> Node.Expr.BinaryOp.Token.AND.toOper()
                Token.LOR -> Node.Expr.BinaryOp.Token.OR.toOper()
                Token.LSS -> Node.Expr.BinaryOp.Token.LT.toOper()
                Token.GTR -> Node.Expr.BinaryOp.Token.GT.toOper()
                Token.ASSIGN -> Node.Expr.BinaryOp.Token.ASSN.toOper()
                Token.LEQ -> Node.Expr.BinaryOp.Token.LTE.toOper()
                Token.GEQ -> Node.Expr.BinaryOp.Token.GTE.toOper()
                else -> error("Unrecognized op ${v.op}")
            },
            rhs = compileExpr(v.y!!).convertType(v.y, v.x).byValue(v.y)
        )
    }

    fun Context.compileBlockStmt(v: BlockStmt): Node.Block {
        // Due to how labels work, we have to do some special work here. Goto labels become just a
        // huge lambda (with next labels inside them). Basically, we first find out whether any label
        // we might have is invoked via goto (as opposed to just break or continue). Then, we compile
        // all the statements putting them in statement sets of either the top level or as a child of
        // the last seen goto label. Then, for each nested statement set we split the label definitions
        // from where they are first called and if a goto needs the label before it appears, move its
        // definition above that goto (but still leave it's first invocation down where it was
        // originally defined).

        // We need to know all labels that are goto asked for ahead of time to skip ones that are
        // never asked for (i.e. are only for break/continue).
        val gotoRefLabels = mutableSetOf<String>()
        v.visitStmts {
            if (it is Stmt_.Stmt.BranchStmt && it.branchStmt.tok == Token.GOTO)
                gotoRefLabels += it.branchStmt.label!!.name
        }

        val multi = Context.LabelNode.Multi(null, mutableListOf())
        v.list.fold(multi) { multi, stmt ->
            // Compile the stmts
            val prevGotoLabels = currFunc.seenGotos.toSet()
            if (multi.label != null) currFunc.returnLabelStack += multi.label
            val stmts = compileStmt(stmt).map { Context.LabelNode.Stmt(it) }
            if (multi.label != null) currFunc.returnLabelStack.removeAt(currFunc.returnLabelStack.lastIndex)
            // Mark which labels are needed before this set
            multi.children += currFunc.seenGotos.mapNotNull {
                if (prevGotoLabels.contains(it)) null
                else Context.LabelNode.LabelNeeded(it)
            }
            // If the statement is labeled in use for goto, change it to its own multi
            val newLabel = (stmt.stmt as? Stmt_.Stmt.LabeledStmt)?.labeledStmt?.label?.name
            if (newLabel == null || !gotoRefLabels.contains(newLabel)) multi.apply { children += stmts }
            else Context.LabelNode.Multi(newLabel, stmts.toMutableList()).also { multi.children += it }
        }

        // There will only be one no-label multi and one label multi. We just need to make sure that one label multi
        // is before the first time it is needed.
        fun labelsNeeded(n: Context.LabelNode): Set<String> = when (n) {
            is Context.LabelNode.Stmt -> emptySet()
            is Context.LabelNode.LabelNeeded -> setOf(n.label)
            is Context.LabelNode.Multi -> n.children.fold(emptySet()) { set, n -> set + labelsNeeded(n) }
        }

        // For multis, replace the first needed label that we have with the defined one and put it's definition
        // as a call.
        fun reorderChildLabels(multi: Context.LabelNode.Multi) {
            // Break the label up into define and call
            val labelsInMulti = multi.children.mapNotNull { (it as? Context.LabelNode.Multi)?.label }
            val indexOfFirstNeeded = multi.children.indexOfFirst { labelsNeeded(it).any(labelsInMulti::contains) }
            if (indexOfFirstNeeded >= 0) {
                val last = multi.children.removeAt(multi.children.lastIndex)
                if (last !is Context.LabelNode.Multi || last.label == null) error("Expected to end with labeled stuff")
                multi.children.add(indexOfFirstNeeded, last.copy(callLabelToo = false))
                multi.children += Context.LabelNode.Stmt(call(expr = "\$${last.label}\$label".toName()).toStmt())
            } else multi.children.lastOrNull()?.also { last ->
                // Not needed, just used for break/continue, just embed the contents
                if (last is Context.LabelNode.Multi && last.label != null) {
                    reorderChildLabels(last)
                    multi.children.apply { removeAt(multi.children.lastIndex) }.addAll(last.children)
                }
            }
            // Recurse into child multis
            multi.children.forEach { if (it is Context.LabelNode.Multi) reorderChildLabels(it) }
        }
        reorderChildLabels(multi)

        fun stmts(n: Context.LabelNode): List<Node.Stmt> = when (n) {
            is Context.LabelNode.Stmt -> listOf(n.stmt)
            is Context.LabelNode.LabelNeeded -> emptyList()
            is Context.LabelNode.Multi -> n.children.flatMap(::stmts).let { children ->
                // If it was needed via a goto, define it, otherwise just use the stmts
                if (n.label == null) children else {
                    // Define it
                    var stmts: List<Node.Stmt> = listOf(Node.Stmt.Decl(property(
                        readOnly = true,
                        vars = listOf(propVar(
                            name = labelIdent(n.label),
                            type = Node.Type(
                                mods = listOf(Node.Modifier.Lit(Node.Modifier.Keyword.SUSPEND)),
                                ref = Node.TypeRef.Func(
                                    receiverType = null,
                                    params = emptyList(),
                                    // TODO: multi return type
                                    type = compileTypeRefMultiResult(currFunc.type.results) ?: Unit::class.toType()
                                )
                            )
                        )),
                        expr = Node.Expr.Labeled(
                            label = labelIdent(n.label),
                            expr = Node.Expr.Brace(emptyList(), Node.Block(children))
                        )
                    )))
                    // Call if necessary
                    if (n.callLabelToo) stmts += call(expr = labelIdent(n.label).toName()).toStmt()
                    stmts

                }
            }
        }

        return Node.Block(stmts(multi))
    }

    fun Context.compileBlockStmtStandalone(v: BlockStmt): Node.Stmt = call(
        expr = "run".toName(),
        lambda = trailLambda(stmts = compileBlockStmt(v).stmts)
    ).toStmt()

    fun Context.compileBranchStmt(v: BranchStmt): Node.Stmt {
        return when (v.tok) {
            Token.BREAK -> Node.Expr.Return(currFunc.breakables.mark(v.label?.name), null).toStmt()
            Token.CONTINUE -> Node.Expr.Return(currFunc.continuables.mark(v.label?.name), null).toStmt()
            Token.GOTO -> v.label!!.name.let { label ->
                currFunc.seenGotos += label
                Node.Expr.Return(
                    label = currFunc.returnLabelStack.lastOrNull()?.let { labelIdent(it) },
                    expr = call(expr = labelIdent(label).toName())
                ).toStmt()
            }
            else -> error("Unrecognized branch: $v")
        }
    }

    fun Context.compileCallExpr(v: CallExpr): Node.Expr {
        val funType = v.`fun`!!.expr!!.typeRef!!.namedType
        // If the function is a const type but not a function, it's a conversion instead of a function call
        if (funType.type is Type_.Type.TypeConst && funType.convType() !is TypeConverter.Type.Func) {
            // Must be a singular arg
            val arg = v.args.singleOrNull() ?: error("Expecting single conversion arg")
            return compileExpr(arg).convertType(arg, funType.type.typeConst.type!!.namedType)
        }
        // Handle built-ins elsewhere
        if (funType.type is Type_.Type.TypeBuiltin) return compileCallExprBuiltIn(v, funType.name)

        var preStmt: Node.Stmt? = null
        val funConvType = funType.convType() as TypeConverter.Type.Func
        // As a special case, if it's a single arg call w/ multi-return, we break it up in temp vars
        val singleArgCallType = (v.args.singleOrNull()?.expr as? Expr_.Expr.CallExpr)?.callExpr?.
            `fun`?.expr?.typeRef?.namedType?.convType() as? TypeConverter.Type.Func
        var args =
            if (singleArgCallType?.results != null && singleArgCallType.results.size > 1) {
                // Deconstruct to temp vals and make those the new args
                val tempVars = singleArgCallType.results.map {
                    currFunc.newTempVar() to it
                }
                preStmt = Node.Stmt.Decl(property(
                    readOnly = true,
                    vars = tempVars.map { (varName, _) -> propVar(varName) },
                    expr = compileExpr(v.args.single())
                ))
                tempVars.map { (varName, varType) -> varName.toName().byValue(varType.type) }
            } else v.args.mapIndexed { index, arg ->
                // Vararg is the type of the slice
                val argType =
                    if (!funConvType.vararg || index < funConvType.params.lastIndex) funConvType.params[index]
                    else (funConvType.params.last() as TypeConverter.Type.Slice).elemType
                compileExpr(arg).convertType(arg, argType).byValue(arg)
            }
        // If this is variadic and the args spill into the varargs, make a slice
        if (funConvType.vararg && args.size >= funConvType.params.size) {
            args = args.take(funConvType.params.size - 1) + args.drop(funConvType.params.size - 1).let {
                val cls = (funConvType.params.last() as? TypeConverter.Type.Primitive)?.cls ?: Any::class
                call(
                    expr = "go2k.runtime.builtin.slice".funcRef(),
                    args = listOf(valueArg(expr = call(
                        expr = cls.arrayOfQualifiedFunctionName().funcRef(),
                        args = it.map { valueArg(expr = it) }
                    )))
                )
            }
        }
        val callExpr = call(
            expr = compileExpr(v.`fun`),
            args = args.map { valueArg(expr = it) }
        )
        // If a pre-stmt exists, this becomes a run expr, otherwise just a call
        return if (preStmt == null) callExpr else call(
            expr = "run".toName(),
            lambda = trailLambda(stmts = listOf(preStmt, callExpr.toStmt()))
        )
    }

    fun Context.compileCallExprBuiltIn(v: CallExpr, name: String) = when (name) {
        "append" -> {
            // First arg is the slice
            val sliceType = v.args.first().expr!!.typeRef!!.namedType.convType() as TypeConverter.Type.Slice
            // If ellipsis is present, only second arg is allowed and it's passed explicitly,
            // otherwise a slice literal is created (TODO: kinda slow to create those unnecessarily)
            val arg =
                if (v.ellipsis > 0) compileExpr(v.args.apply { require(size == 2) }.last())
                else compileSliceLiteral(sliceType, v.args.drop(1))
            call(
                expr = "go2k.runtime.builtin.append".funcRef(),
                args = listOf(valueArg(expr = compileExpr(v.args.first())), valueArg(expr = arg))
            )
        }
        "make" -> when (val argType = v.args.first().expr!!.typeRef!!.typeConst!!.type!!.namedType.convType()) {
            is TypeConverter.Type.Chan -> call(
                expr = "go2k.runtime.builtin.makeChan".toDottedExpr(),
                typeArgs = listOf(compileType(argType.elemType.type)),
                args = v.args.getOrNull(1)?.let { listOf(valueArg(expr = compileExpr(it))) } ?: emptyList()
            )
            is TypeConverter.Type.Map -> {
                // Primitive means the first arg is the zero val
                val firstArgs =
                    if (argType.valType !is TypeConverter.Type.Primitive) emptyList()
                    else listOf(valueArg(expr = compileTypeZeroExpr(argType.valType.type)))
                call(
                    expr = "go2k.runtime.builtin.makeMap".toDottedExpr(),
                    typeArgs = listOf(
                        compileType(argType.keyType.type),
                        compileType(argType.valType.type)
                    ),
                    args = firstArgs +
                        if (v.args.size == 1) emptyList()
                        else listOf(valueArg(name = "size", expr = compileExpr(v.args[1])))
                )
            }
            is TypeConverter.Type.Slice -> {
                val elemType = (argType.elemType as? TypeConverter.Type.Primitive)?.cls
                val createSliceFnName = when (elemType) {
                    Byte::class -> "makeByteSlice"
                    Char::class -> "makeCharSlice"
                    Double::class -> "makeDoubleSlice"
                    Float::class -> "makeFloatSlice"
                    Int::class -> "makeIntSlice"
                    Long::class -> "makeLongSlice"
                    Short::class -> "makeShortSlice"
                    String::class -> "makeStringSlice"
                    UBYTE_CLASS -> "makeUByteSlice"
                    UINT_CLASS -> "makeUIntSlice"
                    ULONG_CLASS -> "makeULongSlice"
                    USHORT_CLASS -> "makeUShortSlice"
                    else -> "makeObjectSlice"
                }
                call(
                    expr = "go2k.runtime.builtin.$createSliceFnName".funcRef(),
                    args = v.args.drop(1).map { valueArg(expr = compileExpr(it)) }
                )
            }
            else -> error("Unrecognized make for $argType")
        }
        "recover" -> call(
            // Use the outside-defer one if not in defer
            expr = if (currFunc.inDefer) "recover".toName() else "go2k.runtime.builtin.recover".toDottedExpr()
        )
        else -> call(
            expr = compileExpr(v.`fun`!!),
            args = v.args.map { valueArg(expr = compileExpr(it)) }
        )
    }

    fun Context.compileCompositeLit(v: CompositeLit) = when (val type = v.type?.expr) {
        is Expr_.Expr.ArrayType -> when (val convType = v.typeRef!!.namedType.convType()) {
            is TypeConverter.Type.Array -> compileArrayLiteral(convType, v.elts)
            is TypeConverter.Type.Slice -> compileSliceLiteral(convType, v.elts)
            else -> error("Unknown array type $convType")
        }
        is Expr_.Expr.MapType -> {
            val mapType = type.typeRef?.namedType?.convType() as? TypeConverter.Type.Map ?: error("Unknown value type")
            // Primitive types have defaults
            val firstArgs =
                if (mapType.valType !is TypeConverter.Type.Primitive) emptyList()
                else listOf(valueArg(expr = compileTypeZeroExpr(mapType.valType.type)))
            call(
                expr = "go2k.runtime.builtin.mapOf".toDottedExpr(),
                typeArgs = listOf(compileType(mapType.keyType.type), compileType(mapType.valType.type)),
                args = firstArgs + v.elts.map { elt ->
                    val kv = (elt.expr as Expr_.Expr.KeyValueExpr).keyValueExpr
                    valueArg(expr = call(
                        expr = "kotlin.Pair".toDottedExpr(),
                        args = listOf(valueArg(expr = compileExpr(kv.key!!)), valueArg(expr = compileExpr(kv.value!!)))
                    ))
                }
            )
        }
        is Expr_.Expr.Ident -> {
            val structType = type.typeRef?.namedType?.convType() as? TypeConverter.Type.Struct ?: TODO()
            call(
                expr = structType.name!!.toName(),
                args = v.elts.map {
                    if (it.expr !is Expr_.Expr.KeyValueExpr) valueArg(expr = compileExpr(it)) else valueArg(
                        name = (it.expr.keyValueExpr.key!!.expr as Expr_.Expr.Ident).ident.name,
                        expr = compileExpr(it.expr.keyValueExpr.value!!)
                    )
                }
            )
        }
        else -> error("Unknown composite lit type: ${v.type}")
    }

    fun Context.compileConstantValue(v: TypeConst): Node.Expr {
        var constVal = v.value?.value ?: error("Missing const value")
        val basicType = v.type?.type as? Type_.Type.TypeBasic ?: error("Expected basic type, got ${v.type?.type}")
        // As a special case, if it's a const float masquerading as an int, treat as a float
        if (constVal is ConstantValue.Value.Int_ && basicType.typeBasic.kind == TypeBasic.Kind.UNTYPED_FLOAT) {
            constVal = ConstantValue.Value.Float_(float = constVal.int)
        }
        return compileConstantValue(basicType.typeBasic.kind, constVal)
    }

    fun Context.compileConstantValue(kind: TypeBasic.Kind, v: ConstantValue.Value) = when (v) {
        is ConstantValue.Value.Bool ->
            Node.Expr.Const(if (v.bool) "true" else "false", Node.Expr.Const.Form.BOOLEAN)
        is ConstantValue.Value.Int_ -> {
            when (kind) {
                TypeBasic.Kind.INT, TypeBasic.Kind.INT_32 -> v.int.toIntConst()
                TypeBasic.Kind.INT_8 -> call(v.int.toIntConst().dot("toByte".toName()))
                TypeBasic.Kind.INT_16 -> call(v.int.toIntConst().dot("toShort".toName()))
                TypeBasic.Kind.INT_64 -> (v.int + "L").toIntConst()
                TypeBasic.Kind.UINT, TypeBasic.Kind.UINT_32 -> (v.int + "u").toIntConst()
                TypeBasic.Kind.UINT_8 -> call((v.int + "u").toIntConst().dot("toUByte".toName()))
                TypeBasic.Kind.UINT_16 -> call((v.int + "u").toIntConst().dot("toUShort".toName()))
                TypeBasic.Kind.UINT_64 -> (v.int + "uL").toIntConst()
                TypeBasic.Kind.UNTYPED_INT ->
                    if (v.int.untypedIntClass() != BigInteger::class) v.int.toIntConst()
                    else call(
                        expr = "go2k.runtime.BigInt".classRef(),
                        args = listOf(valueArg(expr = v.int.toStringTmpl()))
                    )
                else -> error("Unrecognized basic int kind of $kind")
            }
        }
        is ConstantValue.Value.Float_ -> {
            // Due to representability rules, we have to sometimes round down if it's too big
            val reprClass = v.float.untypedFloatClass(includeFloatClass = true)
            val bigDecCall = if (reprClass != BigDecimal::class) null else call(
                expr = "go2k.runtime.BigDec".classRef(),
                args = listOf(valueArg(expr = v.float.toStringTmpl()))
            )
            val withDec = if (v.float.contains('.')) v.float else v.float + ".0"
            when (kind) {
                TypeBasic.Kind.FLOAT_32 -> when (reprClass) {
                    Float::class -> (withDec + "f").toFloatConst()
                    Double::class -> call(withDec.toFloatConst().dot("toFloat".toName()))
                    else -> call(bigDecCall!!.dot("toFloat".toName()))
                }
                TypeBasic.Kind.FLOAT_64 -> when (reprClass) {
                    Float::class, Double::class -> withDec.toFloatConst()
                    else -> call(bigDecCall!!.dot("toDouble".toName()))
                }
                TypeBasic.Kind.UNTYPED_FLOAT -> when (reprClass) {
                    Float::class, Double::class -> withDec.toFloatConst()
                    else -> bigDecCall!!
                }
                else -> error("Unrecognized basic float kind of $kind")
            }
        }
        is ConstantValue.Value.String_ -> v.string.toStringTmpl()
        else -> TODO("$v")
    }

    fun Context.compileDecl(v: Decl_.Decl, topLevel: Boolean) = when (v) {
        is Decl_.Decl.BadDecl -> error("Bad decl: $v")
        is Decl_.Decl.GenDecl -> compileGenDecl(v.genDecl, topLevel)
        is Decl_.Decl.FuncDecl -> listOf(compileFuncDecl(v.funcDecl, topLevel))
    }

    fun Context.compileDeclStmt(v: DeclStmt) = compileDecl(v.decl!!.decl!!, topLevel = false).map {
        // If it's a single property with the blank identifier, just extract the init expr
        val blank = (it as? Node.Decl.Property)?.takeIf { it.vars.singleOrNull()?.name == "_" }?.expr
        if (blank != null) Node.Stmt.Expr(blank) else Node.Stmt.Decl(it)
    }

    fun Context.compileDeferStmt(v: DeferStmt): Node.Stmt {
        // Evaluate all the args then call the lhs of the call w/ them in the lambda
        val argExprs = v.call!!.args.map { compileExpr(it) }
        val (combinedArgs, uncombinedArgs) = when {
            argExprs.size == 1 -> argExprs.single() to listOf(currFunc.newTempVar("p").javaIdent)
            argExprs.isEmpty() -> "kotlin.Unit".toDottedExpr() to emptyList()
            else -> call(
                expr = "go2k.runtime.Tuple${argExprs.size}".toDottedExpr(),
                args = argExprs.map { valueArg(expr = it) }
            ) to argExprs.map { currFunc.newTempVar("p").javaIdent }
        }
        // Defer is just a call of defer w/ the combined args then the lambda uncombining them
        return call(
            expr = "defer".toName(),
            args = listOf(valueArg(expr = combinedArgs)),
            lambda = trailLambda(
                params =
                    if (uncombinedArgs.isEmpty()) emptyList()
                    else listOf(Node.Expr.Brace.Param(uncombinedArgs.map { propVar(it) }, null)),
                stmts = listOf(call(
                    expr = currFunc.pushDefer().let { compileExpr(v.call.`fun`!!).also { currFunc.popDefer() } },
                    args = uncombinedArgs.map { valueArg(expr = it.toName()) }
                ).toStmt())
            )
        ).toStmt()
    }

    fun Context.compileExpr(v: Expr_) = compileExpr(v.expr!!)
    fun Context.compileExpr(v: Expr_.Expr): Node.Expr = when (v) {
        is Expr_.Expr.BadExpr -> TODO()
        is Expr_.Expr.Ident -> compileIdent(v.ident)
        is Expr_.Expr.Ellipsis -> TODO()
        is Expr_.Expr.BasicLit -> compileBasicLit(v.basicLit)
        is Expr_.Expr.FuncLit -> compileFuncLit(v.funcLit)
        is Expr_.Expr.CompositeLit -> compileCompositeLit(v.compositeLit)
        is Expr_.Expr.ParenExpr -> compileParenExpr(v.parenExpr)
        is Expr_.Expr.SelectorExpr -> compileSelectorExpr(v.selectorExpr)
        is Expr_.Expr.IndexExpr -> compileIndexExpr(v.indexExpr)
        is Expr_.Expr.SliceExpr -> compileSliceExpr(v.sliceExpr)
        is Expr_.Expr.TypeAssertExpr -> TODO()
        is Expr_.Expr.CallExpr -> compileCallExpr(v.callExpr)
        is Expr_.Expr.StarExpr -> TODO()
        is Expr_.Expr.UnaryExpr -> compileUnaryExpr(v.unaryExpr)
        is Expr_.Expr.BinaryExpr -> compileBinaryExpr(v.binaryExpr)
        is Expr_.Expr.KeyValueExpr -> TODO()
        is Expr_.Expr.ArrayType -> TODO()
        is Expr_.Expr.StructType -> TODO()
        is Expr_.Expr.FuncType -> TODO()
        is Expr_.Expr.InterfaceType -> TODO()
        is Expr_.Expr.MapType -> TODO()
        is Expr_.Expr.ChanType -> TODO()
    }

    fun Context.compileExprStmt(v: ExprStmt) = Node.Stmt.Expr(compileExpr(v.x!!))

    fun Context.compileFile(v: File) = v.decls.flatMap { compileDecl(it.decl!!, topLevel = true) }.let { decls ->
        Node.File(
            anns = emptyList(),
            // Package and imports are set at a higher level
            pkg = null,
            imports = emptyList(),
            decls = decls
        )
    }

    fun compileFile(pkg: Package, v: File) = Context(pkg).run { this to compileFile(v)  }

    fun Context.compileForStmt(v: ForStmt, label: String? = null): Node.Stmt {
        // Non-range for loops, due to their requirement to support break/continue and that break/continue
        // may be in another run clause or something, we have to use our own style akin to what is explained
        // at https://stackoverflow.com/a/34642869/547546. So we have a run around everything that is where
        // a break breaks out of. Then we have a forLoop fn where a continue returns.

        // Compile the block and see if anything had break/continue
        currFunc.breakables.push(label)
        currFunc.continuables.push(label)
        val bodyStmts = compileBlockStmt(v.body!!).stmts
        val (breakLabel, breakCalled) = currFunc.breakables.pop()
        val (continueLabel, continueCalled) = currFunc.continuables.pop()

        var stmts = emptyList<Node.Stmt>()
        if (v.init != null) stmts += compileStmt(v.init)
        stmts += call(
            expr = "go2k.runtime.forLoop".toDottedExpr(),
            args = listOf(
                // Condition or "true"
                valueArg(expr = Node.Expr.Brace(emptyList(), Node.Block(
                    listOf((v.cond?.let { compileExpr(it) } ?: true.toConst()).toStmt())
                ))),
                // Post or nothing
                valueArg(expr = Node.Expr.Brace(emptyList(), Node.Block(
                    if (v.post == null) emptyList() else compileStmt(v.post)
                )))
            ),
            lambda = trailLambda(
                label = continueLabel.takeIf { continueCalled },
                stmts = bodyStmts
            )
        ).toStmt()
        // If there is an init or a break label, we have to wrap in a run
        return if (stmts.size == 1 && !breakCalled) stmts.single() else call(
            expr = "run".toName(),
            lambda = trailLambda(
                label = breakLabel.takeIf { breakCalled },
                stmts = stmts
            )
        ).toStmt()
    }

    fun Context.compileFuncDecl(v: FuncDecl, topLevel: Boolean): Node.Decl.Func {
        if (!topLevel) TODO()
        pushFunc(v.type!!)
        return compileFuncDecl(v.name?.name, v.type!!, v.body).also { popFunc() }
    }

    fun Context.compileFuncDecl(name: String?, type: FuncType, body: BlockStmt?): Node.Decl.Func {
        var mods = emptyList<Node.Modifier>()
        // TODO: Anon functions sadly can't be suspended yet, ref: https://youtrack.jetbrains.com/issue/KT-18346
        if (name != null) mods += Node.Modifier.Keyword.SUSPEND.toMod()
        if (name != null && name.first().isLowerCase()) mods += Node.Modifier.Keyword.INTERNAL.toMod()
        // Named return idents need to be declared with zero vals up front
        val returnIdents = type.results?.list?.flatMap { it.names } ?: emptyList()
        val preStmts: List<Node.Stmt> = returnIdents.map { ident ->
            Node.Stmt.Decl(property(
                vars = listOf(propVar(ident.name.javaIdent)),
                expr = compileTypeRefZeroExpr(ident.typeRef ?: ident.defTypeRef ?: error("No ident type"))
            ))
        }
        var bodyStmts = if (body == null) emptyList() else compileBlockStmt(body).stmts
        // If there are defer statements, we need to wrap the body in a withDefers
        if (currFunc.hasDefer) bodyStmts = listOf(call(
            expr = "go2k.runtime.builtin.withDefers".toDottedExpr(),
            lambda = trailLambda(stmts = bodyStmts)
        ).toStmt())
        return func(
            mods = mods,
            name = name?.javaIdent,
            params = (type.params?.list ?: emptyList()).flatMap { field ->
                field.names.map { name ->
                    // An ellipsis expr means a slice vararg
                    val vararg: Boolean
                    val type = when (val expr = field.type!!.expr!!) {
                        is Expr_.Expr.Ellipsis -> {
                            vararg = true
                            Slice::class.toType(listOf(compileTypeRef(expr.ellipsis.elt!!.expr!!.typeRef!!))).nullable()
                        }
                        else -> {
                            vararg = false
                            compileTypeRef(expr.typeRef!!)
                        }
                    }
                    param(
                        name = name.name.javaIdent,
                        type = type,
                        default = if (vararg) NullConst else null
                    )
                }
            },
            type = compileTypeRefMultiResult(type.results),
            body = Node.Decl.Func.Body.Block(Node.Block(preStmts + bodyStmts))
        )
    }

    fun Context.compileFuncLit(v: FuncLit): Node.Expr {
        // TODO: once https://youtrack.jetbrains.com/issue/KT-18346 is fixed, return Node.Expr.AnonFunc(decl)
        // In the meantime, we have to turn the function into a suspended lambda sadly
        val tempVar = currFunc.newTempVar("anonFunc")
        val inDefer = currFunc.deferDepth > 0
        pushFunc(v.type!!)
        // TODO: track returns to see if the return label is even used
        currFunc.returnLabelStack += tempVar
        currFunc.inDefer = inDefer
        val decl = compileFuncDecl(null, v.type!!, v.body)
        popFunc()
        return call(
            expr = "go2k.runtime.anonFunc".toDottedExpr(),
            typeArgs = listOf(Node.Type(
                mods = listOf(Node.Modifier.Lit(Node.Modifier.Keyword.SUSPEND)),
                ref = Node.TypeRef.Func(
                    receiverType = null,
                    params = decl.params.map {
                        Node.TypeRef.Func.Param(null, it.type!!)
                    },
                    type = decl.type ?: Unit::class.toType()
                )
            )),
            lambda = trailLambda(
                params = decl.params.map { Node.Expr.Brace.Param(listOf(propVar(it.name)), null) },
                stmts = (decl.body as Node.Decl.Func.Body.Block).block.stmts,
                label = labelIdent(tempVar)
            )
        )
    }

    fun Context.compileGenDecl(v: GenDecl, topLevel: Boolean) = v.specs.flatMap {
        compileSpec(it.spec!!, v.tok == Token.CONST, topLevel)
    }

    fun Context.compileGoStmt(v: GoStmt): Node.Stmt {
        // TODO: Until https://youtrack.jetbrains.com/issue/KT-28752 is fixed, we have to inline the launch call
        // Ug, launch is only an expression on the coroutine scope now
        imports += "kotlinx.coroutines.launch" to "go"
        return call(
            expr = "go2k.runtime.goroutineScope.go".toDottedExpr(),
            lambda = trailLambda(stmts = listOf(compileCallExpr(v.call!!).toStmt()))
        ).toStmt()
    }

    fun Context.compileIdent(v: Ident) = when {
        v.name == "nil" -> NullConst
        v.name == "true" -> Node.Expr.Const("true", Node.Expr.Const.Form.BOOLEAN)
        v.name == "false" -> Node.Expr.Const("false", Node.Expr.Const.Form.BOOLEAN)
        v.typeRef?.type is Type_.Type.TypeBuiltin -> "go2k.runtime.builtin.${v.name}".funcRef()
        else -> v.name.javaName
    }

    fun Context.compileIfStmt(v: IfStmt): Node.Stmt.Expr {
        var expr: Node.Expr = Node.Expr.If(
            expr = compileExpr(v.cond!!),
            body = Node.Expr.Brace(emptyList(), compileBlockStmt(v.body!!)),
            elseBody = v.`else`?.let {
                when (it.stmt) {
                    is Stmt_.Stmt.BlockStmt -> Node.Expr.Brace(emptyList(), compileBlockStmt(it.stmt.blockStmt))
                    is Stmt_.Stmt.IfStmt -> compileIfStmt(it.stmt.ifStmt).expr
                    else -> error("Unknown else statement type: ${it.stmt}")
                }
            }
        )
        // If there is an init, we are going to do it inside of a run block and then do the if
        if (v.init != null) expr = call(
            expr = "run".toName(),
            lambda = trailLambda(stmts = compileStmt(v.init) + expr.toStmt())
        )
        return expr.toStmt()
    }

    fun Context.compileIncDecStmt(v: IncDecStmt) = Node.Stmt.Expr(
        Node.Expr.UnaryOp(
            expr = compileExpr(v.x!!),
            oper = Node.Expr.UnaryOp.Oper(
                if (v.tok == Token.INC) Node.Expr.UnaryOp.Token.INC else Node.Expr.UnaryOp.Token.DEC
            ),
            prefix = false
        )
    )

    fun Context.compileIndexExpr(v: IndexExpr) = Node.Expr.ArrayAccess(
        expr = compileExpr(v.x!!).nullDeref(),
        indices = listOf(compileExpr(v.index!!))
    )

    fun Context.compileLabeledStmt(v: LabeledStmt): List<Node.Stmt> {
        // Labels on some constructs mean certain things. Otherwise, the labels are handled
        // in other areas.
        return when (val stmt = v.stmt!!.stmt) {
            is Stmt_.Stmt.ForStmt -> listOf(compileForStmt(stmt.forStmt, v.label!!.name))
            is Stmt_.Stmt.RangeStmt -> listOf(compileRangeStmt(stmt.rangeStmt, v.label!!.name))
            is Stmt_.Stmt.SelectStmt -> listOf(compileSelectStmt(stmt.selectStmt, v.label!!.name))
            is Stmt_.Stmt.SwitchStmt -> listOf(compileSwitchStmt(stmt.switchStmt, v.label!!.name))
            else -> stmt?.let { compileStmt(it) } ?: emptyList()
        }
    }

    fun compilePackage(pkg: Package, overrideName: String? = null): KotlinPackage {
        // Compile all files...
        var initCount = 0
        var files = pkg.files.map { pkgFile ->
            compileFile(pkg, pkgFile).let { (ctx, file) ->
                Triple(
                    pkgFile.fileName + ".kt",
                    ctx,
                    file.copy(
                        pkg = Node.Package(
                            mods = emptyList(),
                            names = (overrideName ?: ctx.namer.packageName(pkg.path, pkg.name)).split('.')
                        ),
                        // Change all init functions to start with dollar sign and numbered
                        decls = file.decls.map { decl ->
                            (decl as? Node.Decl.Func)?.takeIf { it.name == "init" }?.copy(
                                mods = listOf(Node.Modifier.Keyword.PRIVATE.toMod()),
                                name = "\$init${++initCount}"
                            ) ?: decl
                        }
                    )
                )
            }
        }

        // Add package initializer and main in last file if necessary
        files = files.dropLast(1) + files.last().let { (name, ctx, file) ->
            var extraDecls = emptyList<Node.Decl>()
            extraDecls += ctx.compilePackageInitializer(initCount)
            ctx.compilePackageMain()?.also { extraDecls += it }
            Triple(name, ctx, file.copy(decls = file.decls + extraDecls))
        }

        // Map by name and update imports
        return KotlinPackage(files.map { (name, ctx, file) ->
            name to file.copy(
                imports = ctx.imports.map { (importPath, alias) ->
                    Node.Import(names = importPath.split('.'), wildcard = false, alias = alias)
                }
            )
        }.toMap())
    }

    fun Context.compilePackageInitializer(initCount: Int): Node.Decl.Func {
        val topLevelValueSpecs = pkg.files.flatMap {
            it.decls.flatMap {
                (it.decl as? Decl_.Decl.GenDecl)?.genDecl?.takeIf { it.tok != Token.CONST }?.specs?.mapNotNull {
                    (it.spec as? Spec_.Spec.ValueSpec)?.valueSpec
                } ?: emptyList()
            }
        }
        val varInitStmtsByName = topLevelValueSpecs.flatMap {
            it.names.zip(it.values) { name, value ->
                name.name to Node.Stmt.Expr(
                    binaryOp(
                        lhs = compileIdent(name),
                        op = Node.Expr.BinaryOp.Token.ASSN,
                        rhs = compileExpr(value!!)
                    )
                )
            }
        }.toMap()

        // Make a suspendable public package init that does var inits and calls init funcs
        return func(
            mods = listOf(Node.Modifier.Keyword.SUSPEND.toMod()),
            name = "init",
            body = Node.Decl.Func.Body.Block(Node.Block(
                pkg.varInitOrder.mapNotNull {
                    varInitStmtsByName[it]
                } + (1..initCount).map { initNum -> Node.Stmt.Expr(call("\$init$initNum".javaName)) }
            ))
        )
    }

    fun Context.compilePackageMain(): Node.Decl.Func? {
        val hasMain = pkg.name == "main" && pkg.files.any {
            it.decls.any {
                (it.decl as? Decl_.Decl.FuncDecl)?.funcDecl.let {
                    it?.name?.name == "main" && it.recv == null
                }
            }
        }
        return if (!hasMain) null else func(
            name = "main",
            params = listOf(param(
                name = "args",
                type = arrayType(String::class)
            )),
            body = Node.Decl.Func.Body.Expr(call(
                expr = ::runMain.ref(),
                args = listOf(
                    valueArg(expr = "args".javaName),
                    valueArg(expr = Node.Expr.DoubleColonRef.Callable(recv = null, name = "init")),
                    valueArg(expr = Node.Expr.Brace(emptyList(), Node.Block(listOf(
                        Node.Stmt.Expr(call("main".javaName))
                    ))))
                )
            ))
        )
    }

    fun Context.compileParenExpr(v: ParenExpr) = Node.Expr.Paren(compileExpr(v.x!!))

    fun Context.compileRangeStmt(v: RangeStmt, label: String? = null): Node.Stmt {
        // Range loops are first surrounded by a run for breaking and then labeled for continuing.
        // They are iterated via forEach and forEachIndexed depending upon what is ranged on.
        var keyParam = (v.key?.expr as? Expr_.Expr.Ident)?.ident?.name?.takeIf { it != "_" }
        var valParam = (v.value?.expr as? Expr_.Expr.Ident)?.ident?.name?.takeIf { it != "_" }
        val keyValDestructured: Boolean
        val forEachFnName: String
        val forEachFnIsMember: Boolean
        val rangeExprType = v.x!!.expr!!.typeRef!!.namedType.convType()
        when (rangeExprType) {
            is TypeConverter.Type.Array, is TypeConverter.Type.Primitive -> {
                forEachFnName = if (keyParam != null) "forEachIndexed" else "forEach"
                forEachFnIsMember = true
                keyValDestructured = false
            }
            is TypeConverter.Type.Slice -> {
                forEachFnName = "go2k.runtime." + if (keyParam != null) "forEachIndexed" else "forEach"
                forEachFnIsMember = false
                keyValDestructured = false
            }
            is TypeConverter.Type.Map -> {
                forEachFnName = "forEach"
                forEachFnIsMember = true
                keyValDestructured = true
            }
            else -> error("Unknown range type $rangeExprType")
        }
        // Build lambda params
        var initStmts = emptyList<Node.Stmt>()
        // Define uses lambda params, otherwise we set from temp vars
        if (v.tok == Token.ASSIGN) {
            if (keyParam != null) {
                initStmts += binaryOp(keyParam.toName(), Node.Expr.BinaryOp.Token.ASSN, "\$tempKey".toName()).toStmt()
                keyParam = "\$tempKey"
            }
            if (valParam != null) {
                initStmts += binaryOp(valParam.toName(), Node.Expr.BinaryOp.Token.ASSN, "\$tempVal".toName()).toStmt()
                valParam = "\$tempVal"
            }
        }
        var params = emptyList<Node.Expr.Brace.Param>()
        if (keyValDestructured) {
            params += Node.Expr.Brace.Param(listOf(
                keyParam?.let { Node.Decl.Property.Var(it, null) },
                valParam?.let { Node.Decl.Property.Var(it, null) }
            ), null)
        } else {
            if (keyParam != null)
                params += Node.Expr.Brace.Param(listOf(Node.Decl.Property.Var(keyParam, null)), null)
            if (keyParam != null || valParam != null)
                params += Node.Expr.Brace.Param(listOf(valParam?.let { Node.Decl.Property.Var(it, null) }), null)
        }

        // Check body for any break/continue calls
        currFunc.breakables.push(label)
        currFunc.continuables.push(label)
        val bodyStmts = compileBlockStmt(v.body!!).stmts
        val (breakLabel, breakCalled) = currFunc.breakables.pop()
        val (continueLabel, continueCalled) = currFunc.continuables.pop()

        var stmt = call(
            expr = forEachFnName.toDottedExpr().let {
                if (forEachFnIsMember) compileExpr(v.x).dot(it) else it
            },
            args = if (forEachFnIsMember) emptyList() else listOf(valueArg(expr = compileExpr(v.x))),
            lambda = trailLambda(
                label = continueLabel.takeIf { continueCalled },
                params = params,
                stmts = initStmts + bodyStmts
            )
        ).toStmt()
        if (breakCalled) stmt = call(
            expr = "run".toName(),
            lambda = trailLambda(
                label = breakLabel,
                stmts = listOf(stmt)
            )
        ).toStmt()
        return stmt
    }

    fun Context.compileReturnStmt(v: ReturnStmt) = Node.Stmt.Expr(
        Node.Expr.Return(
            label = currFunc.returnLabelStack.lastOrNull()?.let { labelIdent(it) },
            expr = v.results.map { compileExpr(it) }.let {
                var results = it
                // If it's a naked return but there are named returns, return those
                if (it.isEmpty() && currFunc.type.results != null)
                    results = currFunc.type.results!!.list.flatMap { it.names.map { it.name.javaIdent.toName() } }
                if (results.size <= 1) results.singleOrNull() else call(
                    expr = "go2k.runtime.Tuple${results.size}".toDottedExpr(),
                    args = results.map { valueArg(expr = it) }
                )
            }
        )
    )

    fun Context.compileSelectorExpr(v: SelectorExpr) = compileExpr(v.x!!).dot(compileIdent(v.sel!!))

    fun Context.compileSelectStmt(v: SelectStmt, label: String? = null): Node.Stmt {
        // We would use Kotlin's select, but there are two problems: 1) it does have a good "default"
        // approach (could use onTimeout(0) but I fear it's not the same) and 2) the functions accepted
        // by e.g. onReceiveOrNull are not inline so you cannot break/return out of them several levels
        // up. So what we do is for each case, we use a generic when and each one returns true if handled
        // in order.

        // Put the "default" case last if present
        val cases = v.body!!.list.map { (it.stmt as Stmt_.Stmt.CommClause).commClause }.toMutableList()
        val defaultCaseIndex = cases.indexOfFirst { it.comm == null }
        if (defaultCaseIndex >= 0) cases.add(cases.removeAt(defaultCaseIndex))
        // Compile all the when expressions
        currFunc.breakables.push(label ?: "select")
        // Note, this
        val whenCondsAndBodies = cases.map { case ->
            // Either a receive (assign or just receive expr) or a send or default
            when (case.comm?.stmt) {
                // Assign or define vars for receive
                is Stmt_.Stmt.AssignStmt -> {
                    val assignIdents = case.comm.stmt.assignStmt.lhs.map { (it.expr as Expr_.Expr.Ident).ident.name }
                    val recv = (case.comm.stmt.assignStmt.rhs.single().expr as Expr_.Expr.UnaryExpr).unaryExpr
                    require(recv.op == Token.ARROW)
                    val chanType = recv.x!!.expr!!.typeRef!!.namedType.convType() as TypeConverter.Type.Chan
                    var lambdaPreStmts = emptyList<Node.Stmt>()
                    // Assignment needs temps, define can just use the var names
                    val lambdaParamNames =
                        if (case.comm.stmt.assignStmt.tok == Token.DEFINE) assignIdents
                        else assignIdents.map { varName ->
                            if (varName == "_") varName else currFunc.newTempVar(varName).also { tempVar ->
                                lambdaPreStmts += binaryOp(
                                    lhs = varName.javaIdent.toName(),
                                    op = Node.Expr.BinaryOp.Token.ASSN,
                                    rhs = tempVar.javaIdent.toName()
                                ).toStmt()
                            }
                        }
                    // Different call depending on whether there are two assignments or one
                    call(
                        expr =
                            if (assignIdents.size == 1) "go2k.runtime.builtin.selectRecv".toDottedExpr()
                            else "go2k.runtime.builtin.selectRecvWithOk".toDottedExpr(),
                        args = listOf(
                            valueArg(expr = compileExpr(recv.x.expr!!)),
                            valueArg(expr = compileTypeZeroExpr(chanType.elemType.type))
                        ),
                        lambda = trailLambda(
                            params = lambdaParamNames.map {
                                Node.Expr.Brace.Param(listOf(if (it == "_") null else propVar(it)), null)
                            },
                            stmts = lambdaPreStmts + case.body.flatMap { compileStmt(it) }
                        )
                    ) to emptyList<Node.Stmt>()
                }
                // Receive without vars
                is Stmt_.Stmt.ExprStmt -> {
                    val recv = (case.comm.stmt.exprStmt.x!!.expr as Expr_.Expr.UnaryExpr).unaryExpr
                    require(recv.op == Token.ARROW)
                    val chanType = recv.x!!.expr!!.typeRef!!.namedType.convType() as TypeConverter.Type.Chan
                    call(
                        expr = "go2k.runtime.builtin.selectRecv".toDottedExpr(),
                        args = listOf(
                            valueArg(expr = compileExpr(recv.x.expr!!)),
                            valueArg(expr = compileTypeZeroExpr(chanType.elemType.type))
                        ),
                        lambda = trailLambda(stmts = case.body.flatMap { compileStmt(it) })
                    ) to emptyList<Node.Stmt>()
                }
                // Single send
                is Stmt_.Stmt.SendStmt -> {
                    call(
                        expr = "go2k.runtime.builtin.selectSend".toDottedExpr(),
                        args = listOf(
                            valueArg(expr = compileExpr(case.comm.stmt.sendStmt.chan!!)),
                            valueArg(expr = compileExpr(case.comm.stmt.sendStmt.value!!))
                        ),
                        lambda = trailLambda(stmts = case.body.flatMap { compileStmt(it) })
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
                lambda = trailLambda(stmts = listOf(Node.Expr.When(
                    expr = null,
                    entries = whenCondsAndBodies.map { (whenCond, additionalBodyStmts) ->
                        Node.Expr.When.Entry(
                            conds = if (whenCond == null) emptyList() else listOf(Node.Expr.When.Cond.Expr(whenCond)),
                            body = Node.Expr.Return(breakLabel, null).let { returnExpr ->
                                if (additionalBodyStmts.isEmpty()) returnExpr
                                else Node.Expr.Brace(emptyList(), Node.Block(additionalBodyStmts + returnExpr.toStmt()))
                            }
                        )
                    }
                ).toStmt()))
            ).toStmt()))
        ).toStmt()
    }

    fun Context.compileSendStmt(v: SendStmt) = call(
        expr = "go2k.runtime.builtin.send".toDottedExpr(),
        args = listOf(
            valueArg(expr = compileExpr(v.chan!!)),
            valueArg(expr = compileExpr(v.value!!))
        )
    ).toStmt()

    fun Context.compileSliceExpr(v: SliceExpr) = when (val type = v.x!!.expr!!.typeRef!!.namedType.convType()) {
        is TypeConverter.Type.Slice, is TypeConverter.Type.Array, is TypeConverter.Type.Primitive -> {
            var subject = compileExpr(v.x)
            if (type is TypeConverter.Type.Slice) subject = subject.nullDeref()
            var args = listOf(valueArg(expr = subject))
            if (v.low != null) args += valueArg(name = "low", expr = compileExpr(v.low))
            if (v.high != null) args += valueArg(name = "high", expr = compileExpr(v.high))
            if (v.max != null) args += valueArg(name = "max", expr = compileExpr(v.max))
            call(expr = "go2k.runtime.builtin.slice".funcRef(), args = args)
        }
        else -> TODO()
    }

    fun Context.compileSliceLiteral(type: TypeConverter.Type.Slice, elems: List<Expr_>) = call(
        expr = "go2k.runtime.builtin.slice".funcRef(),
        args = listOf(valueArg(expr = compileArrayLiteral(type.elemType, null, elems)))
    )

    fun Context.compileSpec(v: Spec_.Spec, const: Boolean, topLevel: Boolean) = when (v) {
        is Spec_.Spec.ImportSpec -> TODO()
        is Spec_.Spec.ValueSpec ->
            if (const) compileValueSpecConst(v.valueSpec, topLevel) else compileValueSpecVar(v.valueSpec, topLevel)
        is Spec_.Spec.TypeSpec -> listOf(compileTypeSpec(v.typeSpec))
    }

    fun Context.compileStmt(v: Stmt_) = compileStmt(v.stmt!!)
    fun Context.compileStmt(v: Stmt_.Stmt): List<Node.Stmt> = when (v) {
        is Stmt_.Stmt.BadStmt -> TODO()
        is Stmt_.Stmt.DeclStmt -> compileDeclStmt(v.declStmt)
        is Stmt_.Stmt.EmptyStmt -> TODO()
        is Stmt_.Stmt.LabeledStmt -> compileLabeledStmt(v.labeledStmt)
        is Stmt_.Stmt.ExprStmt -> listOf(compileExprStmt(v.exprStmt))
        is Stmt_.Stmt.SendStmt -> listOf(compileSendStmt(v.sendStmt))
        is Stmt_.Stmt.IncDecStmt -> listOf(compileIncDecStmt(v.incDecStmt))
        is Stmt_.Stmt.AssignStmt -> compileAssignStmt(v.assignStmt)
        is Stmt_.Stmt.GoStmt -> listOf(compileGoStmt(v.goStmt))
        is Stmt_.Stmt.DeferStmt -> listOf(compileDeferStmt(v.deferStmt))
        is Stmt_.Stmt.ReturnStmt -> listOf(compileReturnStmt(v.returnStmt))
        is Stmt_.Stmt.BranchStmt -> listOf(compileBranchStmt(v.branchStmt))
        is Stmt_.Stmt.BlockStmt -> listOf(compileBlockStmtStandalone(v.blockStmt))
        is Stmt_.Stmt.IfStmt -> listOf(compileIfStmt(v.ifStmt))
        is Stmt_.Stmt.CaseClause -> TODO()
        is Stmt_.Stmt.SwitchStmt -> listOf(compileSwitchStmt(v.switchStmt))
        is Stmt_.Stmt.TypeSwitchStmt -> TODO()
        is Stmt_.Stmt.CommClause -> TODO()
        is Stmt_.Stmt.SelectStmt -> listOf(compileSelectStmt(v.selectStmt))
        is Stmt_.Stmt.ForStmt -> listOf(compileForStmt(v.forStmt))
        is Stmt_.Stmt.RangeStmt -> listOf(compileRangeStmt(v.rangeStmt))
    }

    // Name is empty string, is not given any visibility modifier one way or another
    fun Context.compileStructType(name: String, v: StructType): Node.Decl.Structured {
        val type = v.typeRef!!.namedType.convType() as TypeConverter.Type.Struct
        // TODO: more detail
        val fields = (v.fields?.list ?: emptyList()).flatMap { field ->
            val typeRef = field.type!!.expr!!.typeRef!!
            field.names.map { it.name to typeRef }
        }
        return structured(
            primaryConstructor = primaryConstructor(
                params = fields.map { (name, typeRef) ->
                    param(
                        mods =
                            if (name.first().isUpperCase()) emptyList()
                            else listOf(Node.Modifier.Keyword.INTERNAL.toMod()),
                        readOnly = false,
                        name = name.javaIdent,
                        type = compileTypeRef(typeRef),
                        default = compileTypeRefZeroExpr(typeRef)
                    )
                }
            ),
            parents = listOf(Node.Decl.Structured.Parent.Type(
                type = GoStruct::class.toType().ref as Node.TypeRef.Simple,
                by = null
            )),
            members = listOf(compileStructTypeCopyMethod(name, type))
        )
    }

    fun Context.compileStructTypeCopyMethod(name: String, type: TypeConverter.Type.Struct) =
        // The copy method is a deep copy
        func(
            name = "\$copy",
            body = Node.Decl.Func.Body.Expr(call(
                expr = name.toName(),
                args = type.fields.map { (name, type) ->
                    valueArg(expr = name.toName().let {
                        if (type !is TypeConverter.Type.Struct) it
                        else call(expr = it.dot("\$copy".toName()))
                    })
                }
            ))
        )

    fun Context.compileSwitchStmt(v: SwitchStmt, label: String? = null): Node.Stmt {
        // All cases are when entries
        val cases = (v.body?.list ?: emptyList()).map { (it.stmt as Stmt_.Stmt.CaseClause).caseClause }
        // Track break usage
        currFunc.breakables.push(label)
        val entries = cases.mapIndexed { index, case ->
            // Body includes all fallthroughs as duplicate code in run blocks
            var body = listOf(case.body)
            for (i in index + 1 until cases.size) {
                val lastStmt = body.last().lastOrNull()?.stmt
                if ((lastStmt as? Stmt_.Stmt.BranchStmt)?.branchStmt?.tok != Token.FALLTHROUGH) break
                body = body.dropLast(1).plusElement(body.last().dropLast(1)).plusElement(cases[i].body)
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
                body = Node.Expr.Brace(emptyList(), Node.Block(
                    body.first().flatMap { compileStmt(it) } + body.drop(1).map {
                        // Fallthrough
                        call(
                            expr = "run".toName(),
                            lambda = trailLambda(stmts = it.flatMap { compileStmt(it) })
                        ).toStmt()
                    }
                ))
            )
        }.toMutableList()
        val (breakLabel, breakCalled) = currFunc.breakables.pop()
        // Put default at the end
        entries.indexOfFirst { it.conds.isEmpty() }.also { if (it >= 0) entries += entries.removeAt(it) }
        // Create the when
        var stmt = Node.Expr.When(
            expr = v.tag?.let { compileExpr(it) },
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

    fun Context.compileTypeRefZeroExpr(v: TypeRef) = compileTypeZeroExpr(v.namedType)

    fun Context.compileTypeSpec(v: TypeSpec): Node.Decl {
        if (v.assign != 0) TODO("Aliases")
        return compileTypeSpecExpr(v.name!!, v.type!!.expr!!)
    }

    fun Context.compileTypeSpecExpr(name: Ident, v: Expr_.Expr) = when (v) {
        is Expr_.Expr.StructType -> compileTypeSpecStructType(name, v.structType)
        else -> error("Unknown type spec type $v")
    }

    fun Context.compileTypeSpecStructType(name: Ident, v: StructType) = compileStructType(name.name, v).copy(
        mods = if (name.isExposed) emptyList() else listOf(Node.Modifier.Keyword.INTERNAL.toMod()),
        name = name.name.javaIdent
    )

    fun Context.compileTypeZeroExpr(v: Type_): Node.Expr = when (v.type) {
        null -> error("No type")
        is Type_.Type.TypeArray ->
            compileArrayCreate(v.type.typeArray.elem!!.namedType.convType(), v.type.typeArray.len.toInt())
        is Type_.Type.TypeBasic -> when (v.type.typeBasic.kind) {
            TypeBasic.Kind.BOOL, TypeBasic.Kind.UNTYPED_BOOL ->
                compileConstantValue(v.type.typeBasic.kind, ConstantValue.Value.Bool(false))
            TypeBasic.Kind.INT, TypeBasic.Kind.INT_8, TypeBasic.Kind.INT_16, TypeBasic.Kind.INT_32,
                TypeBasic.Kind.INT_64, TypeBasic.Kind.UINT, TypeBasic.Kind.UINT_8, TypeBasic.Kind.UINT_16,
                TypeBasic.Kind.UINT_32, TypeBasic.Kind.UINT_64, TypeBasic.Kind.UINT_PTR,
                TypeBasic.Kind.UNTYPED_INT, TypeBasic.Kind.UNTYPED_RUNE ->
                compileConstantValue(v.type.typeBasic.kind, ConstantValue.Value.Int_("0"))
            TypeBasic.Kind.FLOAT_32, TypeBasic.Kind.FLOAT_64, TypeBasic.Kind.UNTYPED_FLOAT ->
                compileConstantValue(v.type.typeBasic.kind, ConstantValue.Value.Float_("0.0"))
            TypeBasic.Kind.STRING, TypeBasic.Kind.UNTYPED_STRING ->
                compileConstantValue(v.type.typeBasic.kind, ConstantValue.Value.String_(""))
            TypeBasic.Kind.UNTYPED_NIL ->
                NullConst
            else ->
                error("Unrecognized type kind: ${v.type.typeBasic.kind}")
        }
        is Type_.Type.TypeBuiltin -> TODO()
        is Type_.Type.TypeChan, is Type_.Type.TypeFunc, is Type_.Type.TypeInterface,
            is Type_.Type.TypeMap, is Type_.Type.TypeNil, is Type_.Type.TypePointer,
            is Type_.Type.TypeSlice ->
            NullConst
        is Type_.Type.TypeConst -> compileTypeRefZeroExpr(v.type.typeConst.type!!)
        is Type_.Type.TypeLabel -> compileTypeRefZeroExpr(v.type.typeLabel)
        is Type_.Type.TypeName -> compileTypeRefZeroExpr(v.type.typeName)
        is Type_.Type.TypeNamed -> call(expr = v.type.typeNamed.typeName!!.name.javaIdent.toName())
        is Type_.Type.TypePackage -> TODO()
        is Type_.Type.TypeSignature -> TODO()
        is Type_.Type.TypeStruct -> TODO()
        is Type_.Type.TypeTuple -> TODO()
        is Type_.Type.TypeVar -> compileTypeRefZeroExpr(v.type.typeVar.type!!)
    }

    fun Context.compileUnaryExpr(v: UnaryExpr) = compileExpr(v.x!!).let { xExpr ->
        when (v.op) {
            // An "AND" op is a pointer deref
            Token.AND -> {
                // Compile type of expr, and if it's not nullable, a simple "as" nullable form works.
                // Otherwise, it's a NestedPtr
                val xType = compileTypeRef(v.x.expr!!.typeRef!!)
                if (xType.ref !is Node.TypeRef.Nullable) typeOp(
                    lhs = xExpr,
                    op = Node.Expr.TypeOp.Token.AS,
                    rhs = xType.nullable()
                ) else call(
                    expr = NESTED_PTR_CLASS.ref(),
                    args = listOf(valueArg(expr = xExpr))
                )
            }
            // Receive from chan
            Token.ARROW -> call(
                // If the type is a tuple, it's the or-ok version
                expr = (v.typeRef!!.namedType.convType() is TypeConverter.Type.Tuple).let { withOk ->
                    if (withOk) "go2k.runtime.builtin.recvWithOk" else "go2k.runtime.builtin.recv"
                }.toDottedExpr(),
                args = listOf(
                    valueArg(expr = compileExpr(v.x.expr!!)),
                    // Needs zero value of chan element type
                    valueArg(expr = (v.x.expr.typeRef!!.namedType.convType() as TypeConverter.Type.Chan).let {
                        compileTypeZeroExpr(it.elemType.type)
                    })
                )
            )
            // ^ is a bitwise complement
            Token.XOR -> call(expr = xExpr.dot("inv".toName()))
            else -> unaryOp(
                expr = xExpr,
                op = when (v.op) {
                    Token.ADD -> Node.Expr.UnaryOp.Token.POS
                    Token.SUB -> Node.Expr.UnaryOp.Token.NEG
                    Token.INC -> Node.Expr.UnaryOp.Token.INC
                    Token.DEC -> Node.Expr.UnaryOp.Token.DEC
                    Token.NOT -> Node.Expr.UnaryOp.Token.NOT
                    else -> error("Unrecognized op: ${v.op}")
                },
                prefix = v.op != Token.INC && v.op != Token.DEC
            )
        }
    }

    fun Context.compileValueSpecConst(v: ValueSpec, topLevel: Boolean) = v.names.zip(v.values) { id, value ->
        // Consts are always declared inline, and use a const val if possible
        val typeConst = (value.expr?.typeRef?.type as? Type_.Type.TypeConst)?.typeConst
        typeConst ?: error("Unable to find type const")
        val basicKind = (typeConst.type?.type as? Type_.Type.TypeBasic)?.typeBasic?.kind
        basicKind ?: error("Unable to find basic kind")
        val constVal = when (basicKind) {
            TypeBasic.Kind.UNTYPED_FLOAT, TypeBasic.Kind.FLOAT_32, TypeBasic.Kind.FLOAT_64 ->
                (typeConst.value?.value as ConstantValue.Value.Float_).
                    float.untypedFloatClass() != BigDecimal::class
            TypeBasic.Kind.UNTYPED_INT ->
                (typeConst.value?.value as ConstantValue.Value.Int_).int.untypedIntClass() != BigInteger::class
            // TODO: others
            else ->
                true
        }
        property(
            mods = if (constVal) listOf(Node.Modifier.Keyword.CONST.toMod()) else emptyList(),
            readOnly = true,
            vars = listOf(Node.Decl.Property.Var(id.name.javaIdent, null)),
            expr = compileConstantValue(typeConst)
        )
    }

    fun Context.compileValueSpecVar(v: ValueSpec, topLevel: Boolean): List<Node.Decl> {
        return v.names.mapIndexed { index, id ->
            val type = (id.defTypeRef?.type as? Type_.Type.TypeVar)?.
                typeVar?.type?.namedType ?: error("Can't find var type")
            // Top level vars are never inited on their own. Instead they are inited in a separate init area. Therefore,
            // we must mark 'em lateinit. But lateinit is only for non-primitive, non-null types. Otherwise we just init
            // to the 0 value.
            var needsLateinit = topLevel && !type.isJavaPrimitive && !type.isNullable
            val value = v.values.getOrNull(index)
            // It also doesn't need late init if it's an array w/ no value
            if (needsLateinit && type.isArray && value == null) needsLateinit = false
            property(
                mods = if (needsLateinit) listOf(Node.Modifier.Keyword.LATEINIT.toMod()) else emptyList(),
                // We only put the type if it's top level or explicitly specified
                vars = listOf(Node.Decl.Property.Var(
                    name = id.name.javaIdent,
                    type = if (topLevel || v.type != null) compileType(type) else null
                )),
                expr =
                    if (needsLateinit) null
                    else if (topLevel || value == null) compileTypeZeroExpr(type)
                    else compileExpr(value)
            )
        }
    }

    data class KotlinPackage(
        val files: Map<String, Node.File>
    )

    companion object : Compiler()
}