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

	// TODO:
	// select with default
	// select with break
	// select blocked send
	// select with closed chans
	// select w/ receive ok
	// select w/ receive no assign
	// chan of chan
	// send on full chan blocking
	// send on nil chan blocking forever
	// send on closed chan throwing
	// receive on empty chan blocking
	// receive on nil chan blocking forever
	// receive on closed chan return zero val
	// receive with "ok" on closed/empty and avail
}
