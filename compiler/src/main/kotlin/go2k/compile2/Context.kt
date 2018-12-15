package go2k.compile2

import kastree.ast.Node

// File-level context
class Context(
    val pkg: GNode.Package,
    val pkgName: String
) {

    // Key is the full path, value is the alias value if necessary
    val imports = mutableMapOf<String, String?>()

    val funcContexts = mutableListOf<FuncContext>()
    val currFunc get() = funcContexts.last()
    fun pushFunc(type: GNode.Expr.FuncType) { funcContexts += FuncContext(type) }
    fun popFunc() = funcContexts.removeAt(funcContexts.lastIndex)
    fun <T> withFunc(type: GNode.Expr.FuncType, fn: Context.() -> T) = apply { pushFunc(type) }.fn().also { popFunc() }

    class FuncContext(val type: GNode.Expr.FuncType) {
        val breakables = Branchable("break")
        val continuables = Branchable("continue")
        val seenGotos = mutableSetOf<String>()
        val returnLabelStack = mutableListOf<String?>()

        val seenTempVars = mutableSetOf<String>()
        fun newTempVar(prefix: String = ""): String {
            var index = 0
            while (true) "$prefix\$temp${index++}".also { if (seenTempVars.add(it)) return it }
        }

        var inDefer = false
        var deferDepth = 0
        var hasDefer = false
        fun pushDefer() { hasDefer = true; deferDepth++ }
        fun popDefer() { deferDepth-- }
    }

    class Branchable(val labelPostfix: String) {
        // First in pair is label, second is whether it's used
        val used = mutableListOf<Pair<String, Boolean>>()

        fun labelName(labelPrefix: String) = "\$$labelPrefix\$$labelPostfix"

        fun push(labelPrefix: String? = null) { used += labelName(labelPrefix ?: "\$") to false }
        fun pop() = used.removeAt(used.size - 1)
        fun mark(labelPrefix: String? = null) = (labelPrefix?.let(::labelName) ?: used.last().first).also { label ->
            used[used.indexOfLast { it.first == label }] = label to true
        }
    }
}