package go2k.runtime

fun runMain(args: Array<String>, packageInit: suspend () -> Unit, main: suspend () -> Unit) {
    Platform.runSuspended({
        packageInit()
        // TODO: do something with args?
        main()
    })
}

suspend inline fun forLoop(
    cond: suspend () -> Boolean,
    post: suspend () -> Unit,
    body: suspend () -> Unit
) {
    var runPost = false
    while (run {
        if (runPost) post() else runPost = true
        cond()
    }) {
        body()
    }
}