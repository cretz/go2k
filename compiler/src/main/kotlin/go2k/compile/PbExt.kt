package go2k.compile

import go2k.compile.dumppb.*

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

val Ident.isExposed get() = name.isExposed

val String.isExposed get() = firstOrNull()?.isUpperCase() == true

val Type_.isJavaPrimitive get() =
    type is Type_.Type.TypeBasic && type.typeBasic.kind != TypeBasic.Kind.STRING
val Type_.isNil get() =
    type is Type_.Type.TypeNil
val Type_.isNullable get() =
    type is Type_.Type.TypeSlice ||
    type is Type_.Type.TypePointer ||
    type is Type_.Type.TypeFunc ||
    type is Type_.Type.TypeInterface ||
    type is Type_.Type.TypeMap ||
    type is Type_.Type.TypeChan

val TypeBasic.Kind.normalized get() = when (this) {
    TypeBasic.Kind.INT -> TypeBasic.Kind.INT_32
    TypeBasic.Kind.UINT -> TypeBasic.Kind.UINT_32
    else -> this
}