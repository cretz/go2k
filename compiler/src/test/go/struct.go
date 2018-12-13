package main

func main() {
	// Simple struct
	a := Struct1{str: "foo"}
	println("struct 1", a.str)
	// TODO:
	// create struct w/out field names
	// embedded struct
	// empty struct
	// struct pointer
	// struct pointer pointer
	// struct methods
	// local structs
	// anonymous structs (locally, as params, as arrays, nested, etc)
	// non-pointer struct copying
	// tags
}

type Struct1 struct {
	str string
}

type Struct2 struct {
}
