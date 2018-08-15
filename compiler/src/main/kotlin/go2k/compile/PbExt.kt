package go2k.compile

import go2k.compile.dumppb.Expr_
import go2k.compile.dumppb.TypeBasic
import go2k.compile.dumppb.Type_
import go2k.compile.dumppb.TypeRef

val Expr_.Expr.typeRef get() = when (this) {
    is Expr_.Expr.BadExpr -> badExpr.typeRef
    is Expr_.Expr.Ident -> ident.typeRef
    is Expr_.Expr.Ellipsis -> ellipsis.typeRef
    is Expr_.Expr.BasicLit -> basicLit.typeRef
    is Expr_.Expr.FuncLit -> funcLit.typeRef
    is Expr_.Expr.CompositeLit -> compositeLit.typeRef
    is Expr_.Expr.ParenExpr -> parenExpr.typeRef
    is Expr_.Expr.SelectorExpr -> selectorExpr.typeRef
    is Expr_.Expr.IndexExpr -> indexExpr.typeRef
    is Expr_.Expr.SliceExpr -> sliceExpr.typeRef
    is Expr_.Expr.TypeAssertExpr -> typeAssertExpr.typeRef
    is Expr_.Expr.CallExpr -> callExpr.typeRef
    is Expr_.Expr.StarExpr -> starExpr.typeRef
    is Expr_.Expr.UnaryExpr -> unaryExpr.typeRef
    is Expr_.Expr.BinaryExpr -> binaryExpr.typeRef
    is Expr_.Expr.KeyValueExpr -> keyValueExpr.typeRef
    is Expr_.Expr.ArrayType -> arrayType.typeRef
    is Expr_.Expr.StructType -> structType.typeRef
    is Expr_.Expr.FuncType -> funcType.typeRef
    is Expr_.Expr.InterfaceType -> interfaceType.typeRef
    is Expr_.Expr.MapType -> mapType.typeRef
    is Expr_.Expr.ChanType -> chanType.typeRef
}

val Type_.isJavaPrimitive get() =
    type is Type_.Type.TypeBasic && type.typeBasic.kind != TypeBasic.Kind.STRING
val Type_.isNullable get() =
    type is Type_.Type.TypeSlice ||
    type is Type_.Type.TypePointer ||
    type is Type_.Type.TypeFunc ||
    type is Type_.Type.TypeInterface ||
    type is Type_.Type.TypeMap ||
    type is Type_.Type.TypeChan