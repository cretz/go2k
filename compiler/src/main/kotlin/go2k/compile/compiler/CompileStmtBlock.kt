package go2k.compile.compiler

import go2k.compile.go.GNode
import go2k.compile.go.GNodeVisitor
import kastree.ast.Node

fun Context.compileStmtBlock(v: GNode.Stmt.Block): Node.Block = withVarDefSet(v.childVarDefsNeedingRefs()) {
    // Due to how local variable pointers work, we have to do some special work. Kotlin does not
    // have a way to have references to local properties (yet: https://youtrack.jetbrains.com/issue/KT-16303),
    // so if we need to support them we need to know ahead of time that their addresses will be taken, hence
    // the "with" this function is wrapped in.

    // Also due to how labels work, we have to do some special work here. Goto labels become just a
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
    GNodeVisitor.visit(v) { n, _ ->
        if (n is GNode.Stmt.Branch && n.tok == GNode.Stmt.Branch.Token.GOTO) gotoRefLabels += n.label!!.name
        // Do not count nested functions
        n !is GNode.Expr.FuncLit
    }

    // Fold the set of labels marking each one as a child of the last seen and compile the stmts
    val multi = StmtBlockLabelNode.Multi(null, mutableListOf())
    v.stmts.fold(multi) { multi, stmt ->
        // Compile the stmts
        val prevGotoLabels = currFunc.seenGotos.toSet()
        if (multi.label != null) currFunc.returnLabelStack += multi.label
        val stmts = compileStmt(stmt).map { StmtBlockLabelNode.Stmt(it) }
        if (multi.label != null) currFunc.returnLabelStack.removeAt(currFunc.returnLabelStack.lastIndex)
        // Mark which labels are needed before this set
        multi.children += currFunc.seenGotos.mapNotNull {
            if (prevGotoLabels.contains(it)) null
            else StmtBlockLabelNode.LabelNeeded(it)
        }
        // If the statement is labeled in use for goto, change it to its own multi
        val newLabel = (stmt as? GNode.Stmt.Labeled)?.label?.name
        if (newLabel == null || !gotoRefLabels.contains(newLabel)) multi.apply { children += stmts }
        else StmtBlockLabelNode.Multi(newLabel, stmts.toMutableList()).also { multi.children += it }
    }

    // For multis, replace the first needed label that we have with the defined one and put it's definition
    // as a call.
    fun reorderChildLabels(multi: StmtBlockLabelNode.Multi) {
        // Break the label up into define and call
        val labelsInMulti = multi.children.mapNotNull { (it as? StmtBlockLabelNode.Multi)?.label }
        val indexOfFirstNeeded = multi.children.indexOfFirst { it.labelsNeeded().any(labelsInMulti::contains) }
        if (indexOfFirstNeeded >= 0) {
            val last = multi.children.removeAt(multi.children.lastIndex)
            if (last !is StmtBlockLabelNode.Multi || last.label == null) error("Expected to end with labeled stuff")
            multi.children.add(indexOfFirstNeeded, last.copy(callLabelToo = false))
            multi.children += StmtBlockLabelNode.Stmt(call(expr = "\$${last.label}\$label".toName()).toStmt())
        } else multi.children.lastOrNull()?.also { last ->
            // Not needed, just used for break/continue, just embed the contents
            if (last is StmtBlockLabelNode.Multi && last.label != null) {
                reorderChildLabels(last)
                multi.children.apply { removeAt(multi.children.lastIndex) }.addAll(last.children)
            }
        }
        // Recurse into child multis
        multi.children.forEach { if (it is StmtBlockLabelNode.Multi) reorderChildLabels(it) }
    }
    reorderChildLabels(multi)

    fun stmts(n: StmtBlockLabelNode): List<Node.Stmt> = when (n) {
        is StmtBlockLabelNode.Stmt -> listOf(n.stmt)
        is StmtBlockLabelNode.LabelNeeded -> emptyList()
        is StmtBlockLabelNode.Multi -> n.children.flatMap(::stmts).let { children ->
            // If it was needed via a goto, define it, otherwise just use the stmts
            if (n.label == null) children else {
                // Define it
                var stmts: List<Node.Stmt> = listOf(property(
                    readOnly = true,
                    vars = listOf(propVar(
                        name = n.label.labelIdent(),
                        type = Node.Type(
                            mods = listOf(Node.Modifier.Keyword.SUSPEND.toMod()),
                            ref = Node.TypeRef.Func(
                                receiverType = null,
                                params = emptyList(),
                                type = compileTypeMultiResult(currFunc.type.results) ?: Unit::class.toType()
                            )
                        )
                    )),
                    expr = Node.Expr.Labeled(
                        label = n.label.labelIdent(),
                        expr = brace(children)
                    )
                ).toStmt())
                // Call if necessary
                if (n.callLabelToo) stmts += call(expr = n.label.labelIdent().toName()).toStmt()
                stmts

            }
        }
    }

    block(stmts(multi))
}

fun Context.compileStmtBlockStandalone(v: GNode.Stmt.Block) = call(
    expr = "run".toName(),
    lambda = trailLambda(compileStmtBlock(v).stmts)
).toStmt()

sealed class StmtBlockLabelNode {
    abstract fun labelsNeeded(): Set<String>

    data class Stmt(val stmt: Node.Stmt) : StmtBlockLabelNode() {
        override fun labelsNeeded() = emptySet<String>()
    }

    data class Multi(
        val label: String?,
        val children: MutableList<StmtBlockLabelNode>,
        val callLabelToo: Boolean = true
    ) : StmtBlockLabelNode() {
        override fun labelsNeeded() = children.fold(emptySet<String>()) { set, n -> set + n.labelsNeeded() }
    }

    data class LabelNeeded(val label: String) : StmtBlockLabelNode() {
        override fun labelsNeeded() = setOf(label)
    }
}