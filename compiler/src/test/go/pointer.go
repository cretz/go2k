package main

func main() {
	// // Primitive
	// var a *int
	// println("pointer 1", a == nil)
	// b := 5
	// a = &b
	// b++
	// println("pointer 2", *a, b)
	// (*a)++
	// println("pointer 3", *a, b)
	// Array
	var a *int
	c := [...]int{1, 2, 3}
	a = &c[1]
	*a++
	println("pointer 4", *a, c[1])

	// TODO:
	// deref in loop, if stmt, switch, etc
	// top level prim derefed in function
	// address of composite lits (slice/array/map/struct ... and inside of parens), slice elem, pointer indirect, and struct field
}
