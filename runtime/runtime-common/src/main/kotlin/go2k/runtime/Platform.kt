package go2k.runtime

expect object Platform {
    fun arrayCopy(src: Any, srcPos: Int, dest: Any, destPos: Int, length: Int)
    suspend fun print(vararg args: Any?)
    suspend fun println(vararg args: Any?)
    fun <T> runSuspended(fn: suspend () -> T, cb: ((T) -> Unit)? = null)
    fun stringToBytes(str: String): ByteArray
}