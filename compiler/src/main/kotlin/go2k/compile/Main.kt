package go2k.compile

fun main(args: Array<String>) {
    args.firstOrNull().also { cmd ->
        when (cmd) {
            null -> error("Missing arg")
            "compile" -> {
                TODO()
            }
            else -> error("Unknown command $cmd")
        }
    }
}