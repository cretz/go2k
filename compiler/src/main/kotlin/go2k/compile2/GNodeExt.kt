package go2k.compile2

fun GNode.Package.defaultPackageName() = path.split('/').filter(String::isNotEmpty).let { pieces ->
    val first = pieces.first().let {
        val dotIndex = it.indexOf('.')
        if (dotIndex == -1) it else it.substring(dotIndex + 1) + '.' + it.substring(0, dotIndex)
    }
    (listOf(first) + pieces.drop(1)).joinToString(".")
}