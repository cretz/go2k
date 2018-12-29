package go2k.runtime

import kotlinx.coroutines.runBlocking

actual object Platform {
    internal fun printableArg(arg: Any?): Any? = when (arg) {
        is GoInterface -> printableArg(arg.v)
        is GoString -> arg.string
        else -> arg
    }

    actual suspend fun print(vararg args: Any?) {
        args.forEachIndexed { i, arg ->
            if (i > 0) System.err.print(' ')
            System.err.print(printableArg(arg))
        }
    }

    actual suspend fun println(vararg args: Any?) {
        print(*args)
        System.err.print('\n')
    }

    actual fun <T> runSuspended(fn: suspend () -> T, cb: ((T) -> Unit)?) {
        runBlocking {
            val v = fn()
            cb?.invoke(v)
        }
    }

    actual inline fun stringToBytes(str: String) = str.toByteArray()
    actual inline fun bytesToString(bytes: ByteArray) = bytes.toString(Charsets.UTF_8)
    actual inline fun forEachStringChars(string: String, action: (Int) -> Unit) {
        // We have to exec action only once on two-char codepoints
        var skipNext = false
        for (i in 0 until string.length) {
            if (skipNext) {
                skipNext = false
                continue
            }
            val c = string.codePointAt(i)
            action(c)
            skipNext = Character.charCount(c) == 2
        }
    }
    actual inline fun forEachStringCharsWithByteIndex(string: String, action: (Int, Int) -> Unit) {
        // Ref: https://stackoverflow.com/questions/27651543/character-index-to-and-from-byte-index
        var byteIndex = 0
        forEachStringChars(string) { rune ->
            action(byteIndex, rune)
            byteIndex += when {
                rune <= 0x7F -> 1
                rune <= 0x7FF -> 2
                rune <= 0xFFFF -> 3
                else -> 4
            }
        }
    }
}