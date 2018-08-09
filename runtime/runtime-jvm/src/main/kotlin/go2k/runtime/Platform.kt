package go2k.runtime

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
            if (i == args.size - 1) System.err.print(arg)
            else System.err.println(arg)
        }
    }

    actual inline fun stringToBytes(str: String) = str.toByteArray()
}