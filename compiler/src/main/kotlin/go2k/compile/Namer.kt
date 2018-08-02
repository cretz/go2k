package go2k.compile

interface Namer {
    fun packageName(goPackagePath: String, goPackageName: String): String

    open class Simple : Namer {
        override fun packageName(goPackagePath: String, goPackageName: String): String {
            val pieces = goPackagePath.split('/')
            val first = pieces.first().let {
                val dotIndex = it.indexOf('.')
                if (dotIndex == -1) it else it.substring(dotIndex + 1) + '.' + it.substring(0, dotIndex)
            }
            return (listOf(first) + pieces.drop(1)).joinToString(".")
        }
    }
}