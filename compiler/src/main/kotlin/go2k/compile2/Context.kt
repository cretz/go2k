package go2k.compile2

// File-level context
class Context(
    val pkg: GNode.Package,
    val pkgName: String
) {

    // Key is the full path, value is the alias value if necessary
    val imports = mutableMapOf<String, String?>()
}