package go2k.compile.compiler

import go2k.compile.go.GNode

// File-level context
class Context(
    val pkg: GNode.Package,
    val pkgName: String,
    val anonStructTypes: Map<AnonStructType, String>
) {

    // Key is the full path, value is the alias value if necessary
    val imports = mutableMapOf<String, String?>()

    val funcContexts = mutableListOf<FuncContext>()
    val currFunc get() = funcContexts.last()
    fun pushFunc(type: GNode.Expr.FuncType) { funcContexts += FuncContext(type) }
    fun popFunc() = funcContexts.removeAt(funcContexts.lastIndex)
    fun <T> withFunc(type: GNode.Expr.FuncType, fn: Context.() -> T) =
        apply { pushFunc(type) }.fn().also { popFunc() }

    val varDefStack = mutableListOf<VarDefSet>()
    fun pushVarDefSet(varDefsNeedingRefs: Set<String>) { varDefStack += VarDefSet(varDefsNeedingRefs) }
    fun popVarDefSet() { varDefStack.removeAt(varDefStack.lastIndex) }
    fun <T> withVarDefSet(varDefsNeedingRefs: Set<String>, fn: Context.() -> T) =
        apply { pushVarDefSet(varDefsNeedingRefs) }.fn().also { popVarDefSet() }
    // Returns true if it must be a ref
    fun varDefWillBeRef(name: String) = varDefStack.lastOrNull()?.varDefWillBeRef(name) ?: false
    fun markVarDef(name: String) = varDefStack.lastOrNull()?.markVarDef(name) ?: false
    fun varDefIsRef(name: String) =
        varDefStack.asReversed().firstNotNullResult { it.varDefIsRef(name) } ?: false

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

    class VarDefSet(val varDefsNeedingRefs: Set<String>) {
        // Keyed by name, value is true if it's a ref
        val varDefs = mutableMapOf<String, Boolean>()

        fun varDefWillBeRef(name: String) = varDefsNeedingRefs.contains(name)

        fun markVarDef(name: String) = varDefWillBeRef(name).also { varDefs[name] = it }

        fun varDefIsRef(name: String) = varDefs[name]
    }

    data class AnonStructType(val fields: List<Pair<String, FieldType>>) {
        // This var is not part of hashCode on purpose
        lateinit var raw: GNode.Type.Struct

        sealed class FieldType {
            data class Anon(val v: AnonStructType) : FieldType()
            data class Known(val v: GNode.Type) : FieldType()
        }
    }
}