package go2k.runtime

interface Conversions {

    suspend fun stringToBytes(str: String): ByteArray

    open class Standard : Conversions {
        override suspend fun stringToBytes(str: String) = Platform.stringToBytes(str)
    }

    companion object {
        var impl: Conversions = Standard()
    }
}