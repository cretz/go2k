package go2k.compile

import kotlin.reflect.KClass

val UBYTE_CLASS = Class.forName("kotlin.UByte").kotlin as KClass<UByte>
val USHORT_CLASS = Class.forName("kotlin.UShort").kotlin as KClass<UShort>
val UINT_CLASS = Class.forName("kotlin.UInt").kotlin as KClass<UInt>
val ULONG_CLASS = Class.forName("kotlin.ULong").kotlin as KClass<ULong>