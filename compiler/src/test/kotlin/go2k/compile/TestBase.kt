package go2k.compile

open class TestBase {

    fun debug(fn: () -> String) { if (debug) println("[DEBUG] " + fn()) }

    companion object {
        // Change to true to see debug logs
        const val debug = false
    }
}