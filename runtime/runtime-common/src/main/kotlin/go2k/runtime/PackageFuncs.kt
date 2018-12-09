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

suspend inline fun <T> forEach(s: Slice<T>?, action: suspend (T) -> Unit) {
    if (s != null) for (i in 0 until s.len()) action(s[i])
}
suspend inline fun <T> forEachIndexed(s: Slice<T>?, action: suspend (index: Int, T) -> Unit) {
    if (s != null) for (i in 0 until s.len()) action(i, s[i])
}