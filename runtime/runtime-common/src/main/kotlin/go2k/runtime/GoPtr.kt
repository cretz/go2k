package go2k.runtime

interface GoPtr<T> {
    val `$nil`: Boolean
    var `$v`: T

    companion object {
        fun <T> lit(v: T): GoPtr<T>? = ref(GoRef(v))
        fun <T> ref(v: GoRef<T>?): GoPtr<T>? = Ref(v)
        fun <T> index(v: Array<T>, index: Int): GoPtr<T>? = Arr.ObjectArr(v, index)
        fun <S, T> field(v: S, get: (S) -> T, set: (S, T) -> Unit): GoPtr<T>? = Field(v, get, set)
        fun index(v: ByteArray, index: Int): GoPtr<Byte>? = Arr.ByteArr(v, index)
        fun index(v: UByteArray, index: Int): GoPtr<UByte>? = Arr.UByteArr(v, index)
        fun index(v: ShortArray, index: Int): GoPtr<Short>? = Arr.ShortArr(v, index)
        fun index(v: UShortArray, index: Int): GoPtr<UShort>? = Arr.UShortArr(v, index)
        fun index(v: IntArray, index: Int): GoPtr<Int>? = Arr.IntArr(v, index)
        fun index(v: UIntArray, index: Int): GoPtr<UInt>? = Arr.UIntArr(v, index)
        fun index(v: LongArray, index: Int): GoPtr<Long>? = Arr.LongArr(v, index)
        fun index(v: ULongArray, index: Int): GoPtr<ULong>? = Arr.ULongArr(v, index)
        fun index(v: FloatArray, index: Int): GoPtr<Float>? = Arr.FloatArr(v, index)
        fun index(v: DoubleArray, index: Int): GoPtr<Double>? = Arr.DoubleArr(v, index)
        fun index(v: BooleanArray, index: Int): GoPtr<Boolean>? = Arr.BooleanArr(v, index)
        fun index(v: CharArray, index: Int): GoPtr<Char>? = Arr.CharArr(v, index)

        internal sealed class Arr<T> : GoPtr<T> {
            override val `$nil` get() = false
            class ObjectArr<T>(val arr: Array<T>, val index: Int) : Arr<T>() {
                override var `$v` get() = arr[index]; set(value) { arr[index] = value }
            }
            class ByteArr(val arr: ByteArray, val index: Int) : Arr<Byte>() {
                override var `$v` get() = arr[index]; set(value) { arr[index] = value }
            }
            class UByteArr(val arr: UByteArray, val index: Int) : Arr<UByte>() {
                override var `$v` get() = arr[index]; set(value) { arr[index] = value }
            }
            class ShortArr(val arr: ShortArray, val index: Int) : Arr<Short>() {
                override var `$v` get() = arr[index]; set(value) { arr[index] = value }
            }
            class UShortArr(val arr: UShortArray, val index: Int) : Arr<UShort>() {
                override var `$v` get() = arr[index]; set(value) { arr[index] = value }
            }
            class IntArr(val arr: IntArray, val index: Int) : Arr<Int>() {
                override var `$v` get() = arr[index]; set(value) { arr[index] = value }
            }
            class UIntArr(val arr: UIntArray, val index: Int) : Arr<UInt>() {
                override var `$v` get() = arr[index]; set(value) { arr[index] = value }
            }
            class LongArr(val arr: LongArray, val index: Int) : Arr<Long>() {
                override var `$v` get() = arr[index]; set(value) { arr[index] = value }
            }
            class ULongArr(val arr: ULongArray, val index: Int) : Arr<ULong>() {
                override var `$v` get() = arr[index]; set(value) { arr[index] = value }
            }
            class FloatArr(val arr: FloatArray, val index: Int) : Arr<Float>() {
                override var `$v` get() = arr[index]; set(value) { arr[index] = value }
            }
            class DoubleArr(val arr: DoubleArray, val index: Int) : Arr<Double>() {
                override var `$v` get() = arr[index]; set(value) { arr[index] = value }
            }
            class BooleanArr(val arr: BooleanArray, val index: Int) : Arr<Boolean>() {
                override var `$v` get() = arr[index]; set(value) { arr[index] = value }
            }
            class CharArr(val arr: CharArray, val index: Int) : Arr<Char>() {
                override var `$v` get() = arr[index]; set(value) { arr[index] = value }
            }
        }

        internal class Field<S, T>(val v: S, val get: (S) -> T, val set: (S, T) -> Unit) : GoPtr<T> {
            // TODO
            override val `$nil` get() = false
            override var `$v`
                get() = get(v)
                set(value) { set(v, value) }
        }

        internal class Ref<T>(var ref: GoRef<T>?) : GoPtr<T> {
            override val `$nil` get() = ref == null
            override var `$v`
                get() = ref!!.`$v`
                set(value) { ref!!.`$v` = value }
        }
    }
}