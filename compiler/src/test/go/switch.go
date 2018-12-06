package main

func main() {
	// Simple
	for i := 0; i < 4; i++ {
		switch i {
		case 0:
			println("switch 1")
		case 1, 2:
			println("switch 2")
		default:
			println("switch 3")
		}
	}
	// Default at the top
	for i := 0; i < 4; i++ {
		switch i {
		default:
			println("switch 4")
		case 0:
			println("switch 5")
		case 1, 2:
			println("switch 6")
		}
	}
	// Init stmt
	for i := 0; i < 4; i++ {
		switch j := i; j {
		case 0:
			println("switch 7", j)
		case 1, 2:
			println("switch 8", j)
		default:
			println("switch 9", j)
		}
	}
	// No expr
	for i := 0; i < 5; i++ {
		switch {
		case i == 0:
			println("switch 10", i)
		case i < 3, i == 4:
			println("switch 11", i)
		default:
			println("switch 12", i)
		}
	}
	// Init w/ no expr
	for i := 0; i < 5; i++ {
		switch j := i; {
		case j == 0:
			println("switch 13", j)
		case j < 3, j == 4:
			println("switch 14", j)
		default:
			println("switch 15", j)
		}
	}
	// Break
	for i := 0; i < 5; i++ {
		switch i {
		case 0:
			break
			println("switch 16", i)
		case 1, 2:
			println("switch 17", i)
		case 4:
			continue
			println("switch 18", i)
		default:
			println("switch 19", i)
		}
	}
	// Fallthrough
	for i := 0; i < 5; i++ {
		switch i {
		default:
			println("switch 20", i)
			fallthrough
		case 4:
			println("switch 21", i)
			fallthrough
		case 2:
			println("switch 22", i)
		case 3, 1:
			println("switch 23", i)
		}
	}

	// TODO: explicit break/continue labels
	// TODO: impure case expressions to confirm not eval'd if not reached
	// TODO: type switches and all the details
}
