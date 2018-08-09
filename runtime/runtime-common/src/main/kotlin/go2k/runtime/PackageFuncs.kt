package go2k.runtime

fun runMain(args: Array<String>, packageInit: suspend () -> Unit, main: suspend () -> Unit) {
    Platform.runSuspended({
        packageInit()
        // TODO: do something with args?
        main()
    })
}