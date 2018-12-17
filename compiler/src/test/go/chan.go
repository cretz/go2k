package main

func main() {
	// Simple chan
	a := make(chan int, 1)
	a <- 5
	b := <-a
	println("chan 1", b)
	close(a)
	// Unbuffered chan and receive-only
	a = make(chan int)
	go func(ch <-chan int) {
		println("chan 2", <-ch)
	}(a)
	println("chan 3")
	a <- 6
	close(a)
	<-a
	println("chan 4")
	// Send-only chan
	a = make(chan int)
	done := make(chan bool)
	go func(ch <-chan int) {
		println("chan 5", <-ch)
		done <- true
	}(a)
	go func(v int, ch chan<- int) {
		println("chan 6")
		ch <- v
		close(a)
		done <- true
	}(7, a)
	<-done
	<-done
	println("chan 7")
	close(done)
	// Closed receive
	a = make(chan int)
	close(a)
	println("chan 8", <-a)
	// Closed receive ok
	a = make(chan int)
	close(a)
	b, ok := <-a
	println("chan 9", b, ok)
	// Closed receive ok, ignore param
	a = make(chan int)
	close(a)
	_, ok = <-a
	println("chan 10", ok)
	// Simple select
	c, d := make(chan int), make(chan int)
	done = make(chan bool)
	go func() {
	MainLoop:
		for {
			select {
			case v1 := <-c:
				println("chan 11", v1)
			case v2 := <-d:
				println("chan 12", v2)
				if v2 == 13 {
					break MainLoop
				}
			}
		}
		println("chan 13")
		done <- true
	}()
	d <- 11
	c <- 12
	d <- 13
	close(c)
	close(d)
	<-done
	close(done)
	println("chan 14")
	// Select with default
	a = make(chan int, 1)
	select {
	case <-a:
		println("chan 15")
	default:
		println("chan 16")
	}
	println("chan 17")
	close(a)
	// Select with break
	a = make(chan int, 1)
	a <- 12
Select:
	select {
	case v := <-a:
		if v == 12 {
			println("chan 18")
			break Select
		}
		println("chan 19")
	}
	println("chan 20")
	close(a)
	// Select send
	a = make(chan int)
	done = make(chan bool, 1)
	go func() {
		println("chan 21", <-a)
		done <- true
	}()
	select {
	case a <- 15:
		<-done
		println("chan 22")
	}
	close(a)
	close(done)
	// Select closed
	a = make(chan int)
	close(a)
	select {
	case v, ok := <-a:
		println("chan 23", v, ok)
	default:
		println("chan 24")
	}

	// TODO:
	// chan of chan
	// send on full chan blocking
	// send on nil chan blocking forever
	// send on closed chan throwing
	// receive on empty chan blocking
	// receive on nil chan blocking forever
	// receive on closed chan return zero val
	// receive with "ok" on closed/empty and avail
}
