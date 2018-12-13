package go2k.compile2

import go2k.compile.dumppb.File
import go2k.compile.dumppb.Package
import go2k.compile.dumppb.Type_

class PbToGNode(val pkg: Package) {
    val types = pkg.types.map(::convertType)

    fun convertFile(v: File): GNode.File = TODO()

    fun convertPackage() = GNode.Package(
        name = pkg.name,
        path = pkg.path,
        files = pkg.files.map(::convertFile),
        types = types,
        varInitOrder = pkg.varInitOrder
    )

    fun convertType(v: Type_): GNode.Type = when (v.type) {
        is Type_.Type.TypeArray -> TODO()
        is Type_.Type.TypeBasic -> TODO()
        is Type_.Type.TypeBuiltin -> TODO()
        is Type_.Type.TypeChan -> TODO()
        is Type_.Type.TypeConst -> TODO()
        is Type_.Type.TypeFunc -> TODO()
        is Type_.Type.TypeInterface -> TODO()
        is Type_.Type.TypeLabel -> TODO()
        is Type_.Type.TypeMap -> TODO()
        is Type_.Type.TypeName -> TODO()
        is Type_.Type.TypeNamed -> TODO()
        is Type_.Type.TypeNil -> TODO()
        is Type_.Type.TypePackage -> TODO()
        is Type_.Type.TypePointer -> TODO()
        is Type_.Type.TypeSignature -> TODO()
        is Type_.Type.TypeSlice -> TODO()
        is Type_.Type.TypeStruct -> TODO()
        is Type_.Type.TypeTuple -> TODO()
        is Type_.Type.TypeVar -> TODO()
    }
}