package main

func main() {
	// Simple chan
	a := make(chan int, 1)
	a <- 5
	b := <-a
	println("chan 1", b)
	close(a)

	// TODO: work with https://youtrack.jetbrains.com/issue/KT-28752
	// // Unbuffered chan
	// a := make(chan int)
	// go func(ch <-chan int) {
	// 	println("chan 2", <-ch)
	// }(a)
	// println("chan 3")
	// a <- 6
	// close(a)
	// println("chan 4")

	// TODO:
	// make, send, receive unbuffered chan
	// close chan
	// receive only and send only chan
	// chan of chan
	// send on full chan blocking
	// send on nil chan blocking forever
	// send on closed chan throwing
	// receive on empty chan blocking
	// receive on nil chan blocking forever
	// receive on closed chan return zero val
	// receive with "ok" on closed/empty and avail
}
