package go2k.compile

import go2k.runtime.NestedPtr
import kotlin.reflect.KClass

val UBYTE_CLASS = Class.forName("kotlin.UByte").kotlin as KClass<UByte>
val UBYTE_ARRAY_CLASS = Class.forName("kotlin.UByteArray").kotlin as KClass<UByteArray>
val USHORT_CLASS = Class.forName("kotlin.UShort").kotlin as KClass<UShort>
val USHORT_ARRAY_CLASS = Class.forName("kotlin.UShortArray").kotlin as KClass<UShortArray>
val UINT_CLASS = Class.forName("kotlin.UInt").kotlin as KClass<UInt>
val UINT_ARRAY_CLASS = Class.forName("kotlin.UIntArray").kotlin as KClass<UIntArray>
val ULONG_CLASS = Class.forName("kotlin.ULong").kotlin as KClass<ULong>
val ULONG_ARRAY_CLASS = Class.forName("kotlin.ULongArray").kotlin as KClass<ULongArray>

val NESTED_PTR_CLASS = Class.forName("go2k.runtime.NestedPtr").kotlin as KClass<NestedPtr<*>>

fun KClass<*>.primitiveArrayClass() = when (this) {
    Byte::class -> ByteArray::class
    Char::class -> CharArray::class
    Double::class -> DoubleArray::class
    Float::class -> FloatArray::class
    Int::class -> IntArray::class
    Long::class -> LongArray::class
    Short::class -> ShortArray::class
    UBYTE_CLASS -> UBYTE_ARRAY_CLASS
    UINT_CLASS -> UINT_ARRAY_CLASS
    ULONG_CLASS -> ULONG_ARRAY_CLASS
    USHORT_CLASS -> USHORT_ARRAY_CLASS
    else -> null
}