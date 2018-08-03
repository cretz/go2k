package go2k.runtime.builtin

import go2k.runtime.Slice

fun <T> append(slice: Slice<T>?, elems: Slice<T>?): Slice<T>? = TODO()

inline fun cap(v: Array<*>) = v.size
inline fun cap(v: ByteArray) = v.size
inline fun cap(v: UByteArray) = v.size
inline fun cap(v: ShortArray) = v.size
inline fun cap(v: UShortArray) = v.size
inline fun cap(v: IntArray) = v.size
inline fun cap(v: UIntArray) = v.size
inline fun cap(v: LongArray) = v.size
inline fun cap(v: ULongArray) = v.size
inline fun cap(v: FloatArray) = v.size
inline fun cap(v: DoubleArray) = v.size
inline fun cap(v: BooleanArray) = v.size
inline fun cap(v: CharArray) = v.size
suspend inline fun cap(v: Slice<*>) = v.cap()

suspend inline fun <T> copy(dst: Slice<T>?, src: Slice<T>?) = dst?.let { src?.copyTo(it) } ?: 0
fun copy(dst: Slice<Byte>?, src: String): Int = TODO()

inline fun len(v: Array<*>) = v.size
inline fun len(v: ByteArray) = v.size
inline fun len(v: UByteArray) = v.size
inline fun len(v: ShortArray) = v.size
inline fun len(v: UShortArray) = v.size
inline fun len(v: IntArray) = v.size
inline fun len(v: UIntArray) = v.size
inline fun len(v: LongArray) = v.size
inline fun len(v: ULongArray) = v.size
inline fun len(v: FloatArray) = v.size
inline fun len(v: DoubleArray) = v.size
inline fun len(v: BooleanArray) = v.size
inline fun len(v: CharArray) = v.size
suspend inline fun len(v: Slice<*>) = v.len()
inline fun len(v: String) = v.length

var sliceFactory: Slice.Factory = Slice.ArrayBased

inline fun <T> makeSlice(len: Int, cap: Int? = null, zero: () -> T) =
    slice(Array(cap ?: len) { zero() }, high = len)
inline fun <T> makeSlice(len: Int, cap: Int? = null) =
    slice(arrayOfNulls<T>(cap ?: len), high = len)
inline fun makeByteSlice(len: Int, cap: Int? = null) =
    slice(ByteArray(cap ?: len), high = len)
// TODO: this is not really an acceptable way to construct this array
// TODO: Waiting for Kotlin feedback
inline fun makeUByteSlice(len: Int, cap: Int? = null) =
    slice(UByteArray(cap ?: len) { 0.toUByte() }, high = len)
inline fun makeShortSlice(len: Int, cap: Int? = null) =
    slice(ShortArray(cap ?: len), high = len)
inline fun makeUShortSlice(len: Int, cap: Int? = null) =
    slice(UShortArray(cap ?: len) { 0.toUShort() }, high = len)
inline fun makeIntSlice(len: Int, cap: Int? = null) =
    slice(IntArray(cap ?: len), high = len)
inline fun makeUIntSlice(len: Int, cap: Int? = null) =
    slice(UIntArray(cap ?: len) { 0.toUInt() }, high = len)
inline fun makeLongSlice(len: Int, cap: Int? = null) =
    slice(LongArray(cap ?: len), high = len)
inline fun makeULongSlice(len: Int, cap: Int? = null) =
    slice(ULongArray(cap ?: len) { 0.toULong() }, high = len)
inline fun makeFloatSlice(len: Int, cap: Int? = null) =
    slice(FloatArray(cap ?: len), high = len)
inline fun makeDoubleSlice(len: Int, cap: Int? = null) =
    slice(DoubleArray(cap ?: len), high = len)
inline fun makeBooleanSlice(len: Int, cap: Int? = null) =
    slice(BooleanArray(cap ?: len), high = len)
inline fun makeCharSlice(len: Int, cap: Int? = null) =
    slice(CharArray(cap ?: len), high = len)

inline fun <T> slice(arr: Array<T>, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.new(arr, low, high, max)
inline fun slice(arr: ByteArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.new(arr, low, high, max)
inline fun slice(arr: UByteArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.new(arr, low, high, max)
inline fun slice(arr: ShortArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.new(arr, low, high, max)
inline fun slice(arr: UShortArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.new(arr, low, high, max)
inline fun slice(arr: IntArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.new(arr, low, high, max)
inline fun slice(arr: UIntArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.new(arr, low, high, max)
inline fun slice(arr: LongArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.new(arr, low, high, max)
inline fun slice(arr: ULongArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.new(arr, low, high, max)
inline fun slice(arr: FloatArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.new(arr, low, high, max)
inline fun slice(arr: DoubleArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.new(arr, low, high, max)
inline fun slice(arr: BooleanArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.new(arr, low, high, max)
inline fun slice(arr: CharArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.new(arr, low, high, max)
