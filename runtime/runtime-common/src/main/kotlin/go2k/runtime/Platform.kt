package go2k.runtime

expect object Platform {
    suspend fun print(vararg args: Any?)
    suspend fun println(vararg args: Any?)
    fun <T> runSuspended(fn: suspend () -> T, cb: ((T) -> Unit)? = null)
    fun stringToBytes(str: String): ByteArray
    fun bytesToString(bytes: ByteArray): String
    inline fun forEachStringChars(string: String, action: (Int) -> Unit)
    inline fun forEachStringCharsWithByteIndex(string: String, action: (Int, Int) -> Unit)
}