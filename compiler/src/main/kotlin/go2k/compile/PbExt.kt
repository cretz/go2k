package go2k.compile

import go2k.compile.dumppb.TypeBasic
import go2k.compile.dumppb.Type_
import go2k.compile.dumppb.TypeRef

val Type_.isJavaPrimitive get() =
    type is Type_.Type.TypeBasic && type.typeBasic.kind != TypeBasic.Kind.STRING
val Type_.isNullable get() =
    type is Type_.Type.TypeSlice ||
    type is Type_.Type.TypePointer ||
    type is Type_.Type.TypeFunc ||
    type is Type_.Type.TypeInterface ||
    type is Type_.Type.TypeMap ||
    type is Type_.Type.TypeChan