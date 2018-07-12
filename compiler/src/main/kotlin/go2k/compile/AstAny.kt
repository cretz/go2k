package go2k.compile

object AstAny {
    private val pbandk.wkt.Any.typeName inline get() = typeUrl.substringAfterLast('.')

    sealed class Expr {
        data class BadExpr(val v: go2k.compile.dumppb.BadExpr) : Expr()
        data class Ident(val v: go2k.compile.dumppb.Ident) : Expr()
        data class Ellipsis(val v: go2k.compile.dumppb.Ellipsis) : Expr()
        data class BasicLit(val v: go2k.compile.dumppb.BasicLit) : Expr()
        data class FuncLit(val v: go2k.compile.dumppb.FuncLit) : Expr()
        data class CompositeLit(val v: go2k.compile.dumppb.CompositeLit) : Expr()
        data class ParenExpr(val v: go2k.compile.dumppb.ParenExpr) : Expr()
        data class SelectorExpr(val v: go2k.compile.dumppb.SelectorExpr) : Expr()
        data class IndexExpr(val v: go2k.compile.dumppb.IndexExpr) : Expr()
        data class SliceExpr(val v: go2k.compile.dumppb.SliceExpr) : Expr()
        data class TypeAssertExpr(val v: go2k.compile.dumppb.TypeAssertExpr) : Expr()
        data class CallExpr(val v: go2k.compile.dumppb.CallExpr) : Expr()
        data class StarExpr(val v: go2k.compile.dumppb.StarExpr) : Expr()
        data class UnaryExpr(val v: go2k.compile.dumppb.UnaryExpr) : Expr()
        data class BinaryExpr(val v: go2k.compile.dumppb.BinaryExpr) : Expr()
        data class KeyValueExpr(val v: go2k.compile.dumppb.KeyValueExpr) : Expr()
        data class ArrayType(val v: go2k.compile.dumppb.ArrayType) : Expr()
        data class StructType(val v: go2k.compile.dumppb.StructType) : Expr()
        data class FuncType(val v: go2k.compile.dumppb.FuncType) : Expr()
        data class InterfaceType(val v: go2k.compile.dumppb.InterfaceType) : Expr()
        data class MapType(val v: go2k.compile.dumppb.MapType) : Expr()
        data class ChanType(val v: go2k.compile.dumppb.ChanType) : Expr()
        companion object {
            fun fromAny(v: pbandk.wkt.Any) = when (v.typeName) {
                "BadExpr" -> BadExpr(go2k.compile.dumppb.BadExpr.protoUnmarshal(v.value.array))
                "Ident" -> Ident(go2k.compile.dumppb.Ident.protoUnmarshal(v.value.array))
                "Ellipsis" -> Ellipsis(go2k.compile.dumppb.Ellipsis.protoUnmarshal(v.value.array))
                "BasicLit" -> BasicLit(go2k.compile.dumppb.BasicLit.protoUnmarshal(v.value.array))
                "FuncLit" -> FuncLit(go2k.compile.dumppb.FuncLit.protoUnmarshal(v.value.array))
                "CompositeLit" -> CompositeLit(go2k.compile.dumppb.CompositeLit.protoUnmarshal(v.value.array))
                "ParenExpr" -> ParenExpr(go2k.compile.dumppb.ParenExpr.protoUnmarshal(v.value.array))
                "SelectorExpr" -> SelectorExpr(go2k.compile.dumppb.SelectorExpr.protoUnmarshal(v.value.array))
                "IndexExpr" -> IndexExpr(go2k.compile.dumppb.IndexExpr.protoUnmarshal(v.value.array))
                "SliceExpr" -> SliceExpr(go2k.compile.dumppb.SliceExpr.protoUnmarshal(v.value.array))
                "TypeAssertExpr" -> TypeAssertExpr(go2k.compile.dumppb.TypeAssertExpr.protoUnmarshal(v.value.array))
                "CallExpr" -> CallExpr(go2k.compile.dumppb.CallExpr.protoUnmarshal(v.value.array))
                "StarExpr" -> StarExpr(go2k.compile.dumppb.StarExpr.protoUnmarshal(v.value.array))
                "UnaryExpr" -> UnaryExpr(go2k.compile.dumppb.UnaryExpr.protoUnmarshal(v.value.array))
                "BinaryExpr" -> BinaryExpr(go2k.compile.dumppb.BinaryExpr.protoUnmarshal(v.value.array))
                "KeyValueExpr" -> KeyValueExpr(go2k.compile.dumppb.KeyValueExpr.protoUnmarshal(v.value.array))
                "ArrayType" -> ArrayType(go2k.compile.dumppb.ArrayType.protoUnmarshal(v.value.array))
                "StructType" -> StructType(go2k.compile.dumppb.StructType.protoUnmarshal(v.value.array))
                "FuncType" -> FuncType(go2k.compile.dumppb.FuncType.protoUnmarshal(v.value.array))
                "InterfaceType" -> InterfaceType(go2k.compile.dumppb.InterfaceType.protoUnmarshal(v.value.array))
                "MapType" -> MapType(go2k.compile.dumppb.MapType.protoUnmarshal(v.value.array))
                "ChanType" -> ChanType(go2k.compile.dumppb.ChanType.protoUnmarshal(v.value.array))
                else -> error("Unrecognized spec type: ${v.typeName}")
            }
        }
    }

    sealed class Spec {
        data class ImportSpec(val v: go2k.compile.dumppb.ImportSpec) : Spec()
        data class ValueSpec(val v: go2k.compile.dumppb.ValueSpec) : Spec()
        data class TypeSpec(val v: go2k.compile.dumppb.TypeSpec) : Spec()
        companion object {
            fun fromAny(v: pbandk.wkt.Any) = when (v.typeName) {
                "ImportSpec" -> ImportSpec(go2k.compile.dumppb.ImportSpec.protoUnmarshal(v.value.array))
                "ValueSpec" -> ValueSpec(go2k.compile.dumppb.ValueSpec.protoUnmarshal(v.value.array))
                "TypeSpec" -> TypeSpec(go2k.compile.dumppb.TypeSpec.protoUnmarshal(v.value.array))
                else -> error("Unrecognized spec type: ${v.typeName}")
            }
        }
    }

    sealed class Decl {
        data class GenDecl(val v: go2k.compile.dumppb.GenDecl) : Decl()
        data class FuncDecl(val v: go2k.compile.dumppb.FuncDecl) : Decl()
        companion object {
            fun fromAny(v: pbandk.wkt.Any) = when (v.typeName) {
                "GenDecl" -> GenDecl(go2k.compile.dumppb.GenDecl.protoUnmarshal(v.value.array))
                "FuncDecl" -> FuncDecl(go2k.compile.dumppb.FuncDecl.protoUnmarshal(v.value.array))
                else -> error("Unrecognized type: ${v.typeName}")
            }
        }
    }
}