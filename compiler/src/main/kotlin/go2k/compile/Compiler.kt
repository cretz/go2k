package go2k.compile

import go2k.compile.dumppb.*
import kastree.ast.Node

open class Compiler(val conf: Conf = Conf()) {

    fun compileDeclTopLevel(v: Decl_.Decl) = when (v) {
        is Decl_.Decl.BadDecl -> error("Bad decl: $v")
        is Decl_.Decl.GenDecl -> compileGenDeclTopLevel(v.genDecl)
        is Decl_.Decl.FuncDecl -> compileFuncDeclTopLevel(v.funcDecl)
    }

    fun compileFile(v: File) = Node.File(
        anns = emptyList(),
        pkg = null,
        imports = emptyList(),
        decls = v.decls.map { compileDeclTopLevel(it.decl!!) }
    )

    fun compileFuncDeclTopLevel(v: FuncDecl): Node.Decl.Func = TODO()

    fun compileGenDeclTopLevel(v: GenDecl): Node.Decl = TODO()

    fun compilePackage(v: Package) = KotlinPackage(
        files = v.files.map {
            it.fileName.removeSuffix(".go") + ".kt" to compileFile(it).copy(
                pkg = Node.Package(emptyList(), conf.namer.packageName(v.path, v.name).split('.'))
            )
        }.toMap()
    )

    data class KotlinPackage(
        val files: Map<String, Node.File>
    )

    data class Conf(
        val namer: Namer = Namer.Simple()
    )

    companion object : Compiler()
}