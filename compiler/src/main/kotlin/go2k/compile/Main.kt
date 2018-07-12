package go2k.compile

fun main(args: Array<String>) {
    args.firstOrNull().also { cmd ->
        when (cmd) {
            null -> error("Missing arg")
            "compile" -> {
                val pkg = args.getOrNull(1) ?: error("Invalid package")
                val res = Parser.parsePackage(pkg)
                println("WUT:\n$res")
                res.packages.packages.forEach { Compiler.compile(it) }
            }
            else -> error("Unknown command $cmd")
        }
    }
}