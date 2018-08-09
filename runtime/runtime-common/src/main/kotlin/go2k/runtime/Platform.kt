package go2k.runtime

expect object Platform {
    fun arrayCopy(src: Any, srcPos: Int, dest: Any, destPos: Int, length: Int)
    suspend fun print(vararg args: Any?)
    suspend fun println(vararg args: Any?)
    fun stringToBytes(str: String): ByteArray
}