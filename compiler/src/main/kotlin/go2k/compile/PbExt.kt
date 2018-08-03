package go2k.compile

import go2k.compile.dumppb.TypeBasic
import go2k.compile.dumppb.TypeInfo

val TypeInfo.isJavaPrimitive get() =
    type is TypeInfo.Type.TypeBasic && type.typeBasic.kind != TypeBasic.Kind.STRING
val TypeInfo.isNullable get() =
    type is TypeInfo.Type.TypeSlice ||
    type is TypeInfo.Type.TypePointer ||
    type is TypeInfo.Type.TypeFunc ||
    type is TypeInfo.Type.TypeInterface ||
    type is TypeInfo.Type.TypeMap ||
    type is TypeInfo.Type.TypeChan