package go2k.runtime

import kotlinx.coroutines.runBlocking

actual object Platform {
    actual suspend fun print(vararg args: Any?) {
        args.forEachIndexed { i, arg ->
            if (i > 0) System.err.print(' ')
            System.err.print(when (arg) {
                // To match Go version, chars are ints
                is Char -> arg.toInt()
                else -> arg
            })
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
}