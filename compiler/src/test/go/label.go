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
	// Goto after decl
	var1 := 1
	goto Label8
	println("label 12")
Label8:
	println("label 13", var1)
	// Goto before decl
	goto Label9
	println("label 14")
Label9:
	var2 := 1
	println("label 15", var2)
	// Goto before inner decl
	goto Label10
	{
		var3 := 1
		println("label 16", var3)
	}
Label10:
	println("label 17")

	// TODO: Goto out of block, e.g. a loop
	// TODO: Goto self
	// TODO: Goto in func returning single val
	// TODO: Goto in func returning multi val

	// TODO: type switch
	// TODO: select clause
}
