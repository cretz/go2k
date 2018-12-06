package main

func main() {
	// Simple if
	foo := true
	if foo {
		println("simple if 1")
	}
	if !foo {
		println("simple if 2")
	} else {
		println("simple if 3")
	}
	// Decls
	bar := 17
	if baz, bar := foo, bar*2; !baz {
		println("if decl 1")
	} else if qux := 17; bar/2 == qux {
		println("if decl 2", qux, bar)
	}
	qux := 18
	println("if decl 3", qux, bar)
}
