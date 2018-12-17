package go2k.runtime

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield

suspend fun <T> send(ch: SendChannel<T>?, v: T) {
    // TODO: is this the best way to suspend forever
    if (ch == null) delay(Long.MAX_VALUE) else ch.send(v)
}
suspend fun <T> recv(ch: ReceiveChannel<T>?, onNull: T) =
    // TODO: is this the best way to suspend forever
    if (ch == null) {
        delay(Long.MAX_VALUE)
        onNull
    } else ch.receiveOrNull() ?: onNull
suspend inline fun <T> recvWithOk(ch: ReceiveChannel<T>?, onNull: T) =
    Tuple2(recv(ch, onNull), ch?.isClosedForReceive == false)
suspend inline fun <T> selectRecv(ch: ReceiveChannel<T>?, onNull: T, fn: (T) -> Unit) =
    // TODO: not thread-safe, ref: https://discuss.kotlinlang.org/t/thread-safe-receivechannel-poll-on-channel-with-nullable-elements/10731
    if (ch == null || ch.isEmpty) false else true.also { fn(ch.poll() ?: onNull) }
suspend inline fun <T> selectRecvWithOk(ch: ReceiveChannel<T>?, onNull: T, fn: (T, Boolean) -> Unit) =
    // TODO: not thread-safe, ref: https://discuss.kotlinlang.org/t/thread-safe-receivechannel-poll-on-channel-with-nullable-elements/10731
    if (ch == null || ch.isEmpty) false else true.also { fn(ch.poll() ?: onNull, !ch.isClosedForReceive) }
suspend inline fun <T> selectSend(ch: SendChannel<T>?, v: T, fn: () -> Unit) =
    ch != null && ch.offer(v).also { if (it) fn() }

// TODO: Unused until https://youtrack.jetbrains.com/issue/KT-28752 is fixed
//suspend inline fun go(noinline fn: suspend CoroutineScope.() -> Unit) { coroutineScope { launch { fn() } } }

suspend inline fun select(fn: suspend () -> Unit) {
    while (true) {
        fn()
        yield()
    }
}