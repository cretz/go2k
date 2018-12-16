package go2k.compile2

import kastree.ast.Node

fun compilePackage(v: GNode.Package, name: String = v.defaultPackageName()): KPackage {
    var initCount = 0
    return KPackage(v.files.mapIndexed { index, file ->
        "${file.fileName}.kt" to Context(v, name).compilePackageFile(
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

fun Context.compilePackageArtifacts(initCount: Int) =
    listOfNotNull(compilePackageInit(initCount), compilePackageMain())

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

data class KPackage(
    val files: Map<String, Node.File>
)