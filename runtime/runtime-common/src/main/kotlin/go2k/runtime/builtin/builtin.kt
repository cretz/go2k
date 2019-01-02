package go2k.runtime.builtin

import go2k.runtime.*
import kotlinx.coroutines.channels.Channel

suspend inline fun <T> append(slice: Slice<T>?, elems: Slice<T>?) =
    slice?.append(elems!!) ?: elems!!.slice(0, null, null)

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
suspend inline fun cap(v: Slice<*>?) = v?.cap() ?: 0

suspend inline fun close(ch: Channel<*>?) { require(ch!!.close()) { "Channel already closed" } }

suspend inline fun <T> copy(dst: Slice<T>?, src: Slice<T>?) = dst?.let { src?.copyTo(it) } ?: 0
suspend inline fun copy(dst: Slice<UByte>?, src: GoString): Int = copy(dst, slice(src.bytes.toUByteArray()))

inline fun <K> delete(m: go2k.runtime.GoMap<K, *>?, k: K) { m?.remove(k) }

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
suspend inline fun len(v: Slice<*>?) = v?.len() ?: 0
inline fun len(v: GoString) = v.length
inline fun len(v: go2k.runtime.GoMap<*, *>?) = v?.size ?: 0

inline fun <K, V> makeMap(defaultValue: V? = null, size: Int? = null) =
    mapFactory.make<K, V>(defaultValue as V, size)

// TODO: no, this is wrong, they have to be instantiated to zero vals instead of nil
inline fun <T> makeObjectSlice(len: Int, cap: Int? = null) =
    slice(arrayOfNulls<Any?>(cap ?: len) as Array<T>, high = len)
inline fun makeByteSlice(len: Int, cap: Int? = null) =
    slice(ByteArray(cap ?: len), high = len)
inline fun makeUByteSlice(len: Int, cap: Int? = null) =
    slice(UByteArray(cap ?: len), high = len)
inline fun makeShortSlice(len: Int, cap: Int? = null) =
    slice(ShortArray(cap ?: len), high = len)
inline fun makeUShortSlice(len: Int, cap: Int? = null) =
    slice(UShortArray(cap ?: len), high = len)
inline fun makeIntSlice(len: Int, cap: Int? = null) =
    slice(IntArray(cap ?: len), high = len)
inline fun makeUIntSlice(len: Int, cap: Int? = null) =
    slice(UIntArray(cap ?: len), high = len)
inline fun makeLongSlice(len: Int, cap: Int? = null) =
    slice(LongArray(cap ?: len), high = len)
inline fun makeULongSlice(len: Int, cap: Int? = null) =
    slice(ULongArray(cap ?: len), high = len)
inline fun makeFloatSlice(len: Int, cap: Int? = null) =
    slice(FloatArray(cap ?: len), high = len)
inline fun makeDoubleSlice(len: Int, cap: Int? = null) =
    slice(DoubleArray(cap ?: len), high = len)
inline fun makeBooleanSlice(len: Int, cap: Int? = null) =
    slice(BooleanArray(cap ?: len), high = len)
inline fun makeStringSlice(len: Int, cap: Int? = null) =
    slice(Array<GoString>(cap ?: len) { GoString.Empty }, high = len)

fun <T> makeChan(cap: Int = 0) = Channel<T>(cap)

suspend inline fun panic(v: Any?): Nothing = throw Panic(v?.let { GoInterface.Empty.impl(it) })

// Usually these would be "args: Slice<*>" like other Go varargs, but these are special builtins
// and Go handles them differently (e.g. you can't splat the args).
suspend inline fun print(vararg args: Any?) = Platform.print(*args)
suspend inline fun println(vararg args: Any?) = Platform.println(*args)

suspend inline fun recover(): GoInterface.Empty? = null