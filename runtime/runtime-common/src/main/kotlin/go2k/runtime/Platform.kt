package go2k.runtime

expect object Platform {
    fun arrayCopy(src: Any, srcPos: Int, dest: Any, destPos: Int, length: Int)
    fun stringToBytes(str: String): ByteArray
}