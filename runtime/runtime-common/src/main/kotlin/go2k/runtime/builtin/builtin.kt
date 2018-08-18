package go2k.runtime.builtin

import go2k.runtime.GoInterface
import go2k.runtime.Panic
import go2k.runtime.Platform
import go2k.runtime.Slice

fun <T> append(slice: Slice<T>?, elems: Slice<T>?): Slice<T>? = TODO()

inline fun cap(v: Array<*>) = v.size
inline fun cap(v: ByteArray) = v.size
//inline fun cap(v: UByteArray) = v.size
inline fun cap(v: ShortArray) = v.size
//inline fun cap(v: UShortArray) = v.size
inline fun cap(v: IntArray) = v.size
//inline fun cap(v: UIntArray) = v.size
inline fun cap(v: LongArray) = v.size
//inline fun cap(v: ULongArray) = v.size
inline fun cap(v: FloatArray) = v.size
inline fun cap(v: DoubleArray) = v.size
inline fun cap(v: BooleanArray) = v.size
inline fun cap(v: CharArray) = v.size
suspend inline fun cap(v: Slice<*>) = v.cap()

suspend inline fun <T> copy(dst: Slice<T>?, src: Slice<T>?) = dst?.let { src?.copyTo(it) } ?: 0
suspend inline fun copy(dst: Slice<Byte>?, src: String): Int = copy(dst, sliceByteArray(Platform.stringToBytes(src)))

interface EmptyInterface : GoInterface {
    companion object {
        inline fun impl(v: Any?): EmptyInterface = EmptyInterfaceImpl(v)
    }
}

inline class EmptyInterfaceImpl(override val v: Any?) : EmptyInterface

inline fun len(v: Array<*>) = v.size
inline fun len(v: ByteArray) = v.size
//inline fun len(v: UByteArray) = v.size
inline fun len(v: ShortArray) = v.size
//inline fun len(v: UShortArray) = v.size
inline fun len(v: IntArray) = v.size
//inline fun len(v: UIntArray) = v.size
inline fun len(v: LongArray) = v.size
//inline fun len(v: ULongArray) = v.size
inline fun len(v: FloatArray) = v.size
inline fun len(v: DoubleArray) = v.size
inline fun len(v: BooleanArray) = v.size
inline fun len(v: CharArray) = v.size
suspend inline fun len(v: Slice<*>) = v.len()
inline fun len(v: String) = v.length

var sliceFactory: Slice.Factory = Slice.ArrayBased

inline fun <T> makeObjectSlice(len: Int, cap: Int? = null) =
    sliceObjectArray(arrayOfNulls<Any?>(cap ?: len) as Array<T>, high = len)
inline fun makeByteSlice(len: Int, cap: Int? = null) =
    sliceByteArray(ByteArray(cap ?: len), high = len)
// TODO: this is not really an acceptable way to construct this array
// Ref: https://youtrack.jetbrains.com/issue/KT-25875
inline fun makeUByteSlice(len: Int, cap: Int? = null) =
    sliceUByteArray(UByteArray(cap ?: len) { 0.toUByte() }, high = len)
inline fun makeShortSlice(len: Int, cap: Int? = null) =
    sliceShortArray(ShortArray(cap ?: len), high = len)
inline fun makeUShortSlice(len: Int, cap: Int? = null) =
    sliceUShortArray(UShortArray(cap ?: len) { 0.toUShort() }, high = len)
inline fun makeIntSlice(len: Int, cap: Int? = null) =
    sliceIntArray(IntArray(cap ?: len), high = len)
inline fun makeUIntSlice(len: Int, cap: Int? = null) =
    sliceUIntArray(UIntArray(cap ?: len) { 0.toUInt() }, high = len)
inline fun makeLongSlice(len: Int, cap: Int? = null) =
    sliceLongArray(LongArray(cap ?: len), high = len)
inline fun makeULongSlice(len: Int, cap: Int? = null) =
    sliceULongArray(ULongArray(cap ?: len) { 0.toULong() }, high = len)
inline fun makeFloatSlice(len: Int, cap: Int? = null) =
    sliceFloatArray(FloatArray(cap ?: len), high = len)
inline fun makeDoubleSlice(len: Int, cap: Int? = null) =
    sliceDoubleArray(DoubleArray(cap ?: len), high = len)
inline fun makeBooleanSlice(len: Int, cap: Int? = null) =
    sliceBooleanArray(BooleanArray(cap ?: len), high = len)
inline fun makeCharSlice(len: Int, cap: Int? = null) =
    sliceCharArray(CharArray(cap ?: len), high = len)

inline fun panic(v: Any?): Nothing = throw Panic(v)

// Usually these would be "args: Slice<*>" like other Go varargs, but these are special builtins
// and Go handles them differently (e.g. you can't splat the args)
suspend inline fun print(vararg args: Any?) = Platform.print(*args)
suspend inline fun println(vararg args: Any?) = Platform.println(*args)

inline fun <T> sliceObjectArray(arr: Array<T>, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.newObjectSlice(arr, low, high, max)
inline fun sliceByteArray(arr: ByteArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.newByteSlice(arr, low, high, max)
inline fun sliceUByteArray(arr: UByteArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.newUByteSlice(arr, low, high, max)
inline fun sliceShortArray(arr: ShortArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.newShortSlice(arr, low, high, max)
inline fun sliceUShortArray(arr: UShortArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.newUShortSlice(arr, low, high, max)
inline fun sliceIntArray(arr: IntArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.newIntSlice(arr, low, high, max)
inline fun sliceUIntArray(arr: UIntArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.newUIntSlice(arr, low, high, max)
inline fun sliceLongArray(arr: LongArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.newLongSlice(arr, low, high, max)
inline fun sliceULongArray(arr: ULongArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.newULongSlice(arr, low, high, max)
inline fun sliceFloatArray(arr: FloatArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.newFloatSlice(arr, low, high, max)
inline fun sliceDoubleArray(arr: DoubleArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.newDoubleSlice(arr, low, high, max)
inline fun sliceBooleanArray(arr: BooleanArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.newBooleanSlice(arr, low, high, max)
inline fun sliceCharArray(arr: CharArray, low: Int = 0, high: Int = arr.size, max: Int = arr.size) =
    sliceFactory.newCharSlice(arr, low, high, max)
