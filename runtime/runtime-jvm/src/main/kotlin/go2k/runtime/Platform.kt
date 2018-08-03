package go2k.runtime

actual object Platform {
    actual inline fun arrayCopy(src: Any, srcPos: Int, dest: Any, destPos: Int, length: Int) {
        System.arraycopy(src, srcPos, dest, destPos, length)
    }

    actual inline fun stringToBytes(str: String) = str.toByteArray()
}