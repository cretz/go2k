package go2k.compile.compiler

import go2k.compile.go.GNode
import go2k.compile.go.GNodeVisitor
import kastree.ast.Node

fun compilePackage(v: GNode.Package, name: String = v.defaultPackageName()): KPackage {
    // Have to pre-calc anon struct types and name clashes
    val anonStructTypes = compilePackageAnonStructTypes(v)
    val methodNameClashes = compilePackageMethodNameClashes(v)
    var initCount = 0
    return KPackage(v.files.mapIndexed { index, file ->
        val ctx = Context(
            pkg = v,
            pkgName = name,
            anonStructTypes = anonStructTypes,
            methodNameClashes = methodNameClashes
        )
        "${file.fileName}.kt" to ctx.compilePackageFile(
            v = file,
            mutateDecl = { decl ->
                // Make init function names unique
                if (decl is Node.Decl.Func && decl.name == "init") decl.copy(name = "\$init${++initCount}") else decl
            },
            additionalDecls = {
                // Last has additional package artifacts
                if (index == v.files.lastIndex) compilePackageArtifacts(initCount) else emptyList()
            }
        )
    }.toMap())
}

fun Context.compilePackageAnonStructs(types: Map<Context.AnonStructType, String>) =
    // Don't compile the empty one
    types.filter { (type, _) -> type.fields.isNotEmpty() }.map { (type, name) -> compileExprStructType(name, type.raw) }

fun compilePackageAnonStructTypes(v: GNode.Package): LinkedHashMap<Context.AnonStructType, String> {
    // Get all top level anon types
    val topLevelAnonTypes = mutableListOf<Context.AnonStructType>()
    (object : GNodeVisitor() {
        val nodeStack = mutableListOf<GNode>()
        // Store the seen just to save cycles
        val seenStructTypes = mutableSetOf<GNode.Type.Struct>()
        override fun visit(v: GNode, parent: GNode) {
            nodeStack.add(v)
            if (v is GNode.Type.Struct && !seenStructTypes.contains(v)) {
                // The struct is named if the parent is named or the parent
                // is const, then expr struct type, then type name
                val named = parent is GNode.Type.Named || (
                    nodeStack.getOrNull(nodeStack.size - 2) is GNode.Type.Const &&
                    nodeStack.getOrNull(nodeStack.size - 3) is GNode.Expr.StructType &&
                    nodeStack.getOrNull(nodeStack.size - 4) is GNode.Spec.Type
                )
                if (!named) topLevelAnonTypes += v.toAnonType().also { seenStructTypes += v }
            }
            super.visit(v, parent)
            nodeStack.removeAt(nodeStack.lastIndex)
        }
    }).visit(v)
    // Now, recursively add them to a set to make sure we capture them all, and give them names
    val allAnonTypes = linkedSetOf<Context.AnonStructType>()
    fun addAnon(v: Context.AnonStructType) {
        allAnonTypes += v
        v.fields.forEach { (_, type) -> if (type is Context.AnonStructType.FieldType.Anon) addAnon(type.v) }
    }
    topLevelAnonTypes.forEach(::addAnon)
    // Give them all names and return as linked map, with empty always explicit
    return linkedMapOf<Context.AnonStructType, String>().also { map ->
        allAnonTypes.forEachIndexed { index, type -> map[type] = "Anon\$Struct${index + 1}" }
        map[Context.AnonStructType(emptyList())] = "go2k.runtime.GoStruct.Empty"
    }
}

fun Context.compilePackageArtifacts(initCount: Int) =
    listOfNotNull(compilePackageInit(initCount), compilePackageMain()) + compilePackageAnonStructs(anonStructTypes)

fun Context.compilePackageFile(
    v: GNode.File,
    mutateDecl: (Node.Decl) -> Node.Decl = { it },
    additionalDecls: Context.() -> List<Node.Decl> = { emptyList() }
) = Node.File(
    anns = emptyList(),
    pkg = Node.Package(emptyList(), pkgName.split('.')),
    decls = v.decls.flatMap { compileDecl(it, topLevel = true).map(mutateDecl) } + additionalDecls(),
    imports = imports.map { (importPath, alias) ->
        Node.Import(names = importPath.split('.'), wildcard = false, alias = alias)
    }
)

fun Context.compilePackageInit(initCount: Int): Node.Decl {
    // Suspendable public init sets vars and calls init funcs
    val topLevelValues = pkg.files.flatMap { it.decls.mapNotNull { it as? GNode.Decl.Var }.flatMap { it.specs } }
    val varInitsByName = topLevelValues.flatMap {
        it.names.zip(it.values) { name, value ->
            name.name to binaryOp(name.name.toName(), Node.Expr.BinaryOp.Token.ASSN, compileExpr(value)).toStmt()
        }
    }.toMap()
    return func(
        mods = listOf(Node.Modifier.Keyword.SUSPEND.toMod()),
        name = "init",
        body = block(
            pkg.varInitOrder.mapNotNull { varInitsByName[it] } +
            (1..initCount).map { call("\$init$it".toName()).toStmt() }
        ).toFuncBody()
    )
}

fun Context.compilePackageMain(): Node.Decl.Func? {
    // Main just calls runMain w/ the args, the init func ref, and a lambda to call main()
    val hasMain = pkg.name == "main" && pkg.files.any {
        it.decls.any { it is GNode.Decl.Func && it.name == "main" && it.recv.isEmpty() }
    }
    return if (!hasMain) null else func(
        name = "main",
        params = listOf(param(name = "args", type = arrayType(String::class))),
        body = call(
            expr = "go2k.runtime.runMain".toDottedExpr(),
            args = listOf(
                valueArg("args".toName()),
                valueArg(Node.Expr.DoubleColonRef.Callable(recv = null, name = "init")),
                valueArg(brace(listOf(call("main".toName()).toStmt())))
            )
        ).toFuncBody()
    )
}

fun compilePackageMethodNameClashes(v: GNode.Package): Map<Context.MethodNameClash, String> {
    // Keyed by method name
    var clashableMethods = emptyMap<String, List<String>>()
    v.files.forEach { file ->
        file.decls.forEach {
            if (it is GNode.Decl.Func) {
                it.clashableRecvTypeName()?.also { typeName ->
                    clashableMethods += it.name to (clashableMethods[it.name].orEmpty() + typeName)
                }
            } else if (it is GNode.Decl.Type) it.specs.forEach { spec ->
                val structType = when {
                    spec.expr is GNode.Expr.StructType -> spec.expr.type.nonEntityType() as GNode.Type.Struct
                    spec.expr is GNode.Expr.Ident -> spec.expr.type.namedUnderlyingType() as? GNode.Type.Struct
                    else -> null
                } ?: return@forEach
                compileExprStructTypeGetEmbedMembers(v, structType).forEach { member ->
                    if (!member.self && member.params != null) {
                        clashableMethods += member.name to (clashableMethods[member.name].orEmpty() + spec.name)
                    }
                }
            }
        }
    }
    // Now return duplicates w/ new method names
    var usedMethodNames = clashableMethods.keys
    return clashableMethods.filter { (_, v) -> v.size > 1 }.flatMap { (methodName, typeNames) ->
        typeNames.map { typeName ->
            var newName = "${methodName}On$typeName"
            var index = 0
            while (usedMethodNames.contains(newName)) newName = "${methodName}On$typeName${++index}"
            usedMethodNames += newName
            Context.MethodNameClash(typeName, methodName) to newName
        }
    }.toMap()
}

data class KPackage(
    val files: Map<String, Node.File>
)