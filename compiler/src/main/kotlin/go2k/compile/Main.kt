package go2k.compile

import kastree.ast.Writer

fun main(args: Array<String>) {
    args.firstOrNull().also { cmd ->
        when (cmd) {
            null -> error("Missing arg")
            "compile" -> {
                val pkg = args.getOrNull(1) ?: error("Invalid package")
                val res = Parser.parse(pkg)
                println("WUT:\n$res")
                res.packages.packages.forEach {
                    Compiler.compilePackage(it).files.forEach { (name, file) ->
                        println("-------\nFILE: $name:")
                        println(Writer.write(file))
                        println("-------")
                    }
                }
            }
            else -> error("Unknown command $cmd")
        }
    }
}