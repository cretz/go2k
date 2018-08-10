package go2k.runtime

import kotlinx.coroutines.runBlocking

actual object Platform {
    actual inline fun arrayCopy(src: Any, srcPos: Int, dest: Any, destPos: Int, length: Int) {
        System.arraycopy(src, srcPos, dest, destPos, length)
    }

    actual suspend fun print(vararg args: Any?) {
        args.forEachIndexed { i, arg ->
            if (i > 0) System.err.print(' ')
            System.err.print(arg)
        }
    }

    actual suspend fun println(vararg args: Any?) {
        args.forEachIndexed { i, arg ->
            if (i > 0) System.err.print(' ')
            System.err.print(arg)
            if (i == args.size - 1) System.err.print('\n')
        }
    }

    actual fun <T> runSuspended(fn: suspend () -> T, cb: ((T) -> Unit)?) {
        runBlocking {
            val v = fn()
            cb?.invoke(v)
        }
    }

    actual inline fun stringToBytes(str: String) = str.toByteArray()
}