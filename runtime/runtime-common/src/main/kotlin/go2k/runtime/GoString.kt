package go2k.runtime

// TODO: lots can be done better than lazily storing the string and bytes. There are a ton
// of known performance problems here. Should probably just keep as byte array always and
// wait until range'd or rune-array'd before converting back. Problem is it makes string
// literals look ugly, (e.g. `GoString(byteArrayOf(47, 48))`) and it has a performance
// issue because it takes many instructions to create a byte array.
//
// TODO: Should also be an inlined class but like mentioned elsewhere, I want to call from
// Java and that's not supported yet.
sealed class GoString {
    abstract val string: String
    abstract val bytes: ByteArray
    val length inline get() = bytes.size
    internal fun slice(low: Int, high: Int): GoString = FromByteArray(bytes.copyOfRange(low, high))

    operator fun plus(other: GoString): GoString = FromString(string + other.string)
    operator fun get(index: Int) = bytes[index].toUByte()
    override fun equals(other: Any?) = this === other || (other as? GoString)?.string == string
    override fun hashCode() = string.hashCode()

    inline fun forEach(action: (Int) -> Unit) = Platform.forEachStringChars(string, action)
    inline fun forEachIndexed(action: (Int, Int) -> Unit) =
        Platform.forEachStringCharsWithByteIndex(string, action)

    internal class FromString(override val string: String) : GoString() {
        override val bytes by lazy { Platform.stringToBytes(string) }
    }

    internal class FromByteArray(override val bytes: ByteArray) : GoString() {
        override val string by lazy { Platform.bytesToString(bytes) }
    }

    companion object {
        operator fun invoke(str: String = ""): GoString = if (str.isEmpty()) Empty else FromString(str)
    }

    object Empty : GoString() {
        override val string = ""
        override val bytes = byteArrayOf()
    }
}