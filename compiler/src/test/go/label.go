package main

func main() {

	// Break outer loop and switch
Label1:
	for i := 0; i < 4; i++ {
		println("label 1", i)
		for j := 0; j < 4; j++ {
			println("label 2", i, j)
			switch {
			case i > 0:
				break Label1
			}
		}
	}
	// Continue outer loop and switch
Label2:
	for i := 0; i < 4; i++ {
		println("label 3", i)
		for j := 0; j < 4; j++ {
			println("label 4", i, j)
			switch {
			case i > 0:
				continue Label2
			}
		}
	}
	// Break range outer loop
Label3:
	for _, v := range []string{"a", "b", "c", "d"} {
		for i := 0; i < 4; i++ {
			println("label 5", i, v)
			if v == "b" && i == 2 {
				break Label3
			}
		}
	}
	// Continue range outer loop
Label4:
	for _, v := range []string{"a", "b", "c", "d"} {
		for i := 0; i < 4; i++ {
			println("label 6", i, v)
			if v != "b" || i > 2 {
				continue Label4
			}
			println("label 7", i, v)
		}
	}
	// Break labeled switch
	for i := 0; i < 4; i++ {
	Label5:
		switch {
		case i%2 == 0:
			println("label 8", i)
			if i == 3 {
				break Label5
			}
			fallthrough
		default:
			println("label 9", i)
		}
	}
	// Break default on labeled loop
Label6:
	for i := 0; i < 4; i++ {
		println("label 10", i)
		if i == 2 {
			break
		} else if i == 3 {
			// unreachable of course
			break Label6
		}
	}
	// Continue default on labeled loop
Label7:
	for i := 0; i < 5; i++ {
		if i == 2 {
			continue
		} else if i == 3 {
			continue Label7
		}
		println("label 11", i)
	}

	// TODO: type switch
	// TODO: break and continue sans label but loop has explicit label
	// TODO: goto
	// TODO: select clause
}
