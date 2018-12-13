package main

func main() {
	// Simple struct
	a := Struct1{str: "foo"}
	println("struct 1", a.str, a.other.num)
	// Struct without field name
	a = Struct1{"foo", Struct2{5}}
	println("struct 2", a.str, a.other.num)
	// Struct with no field
	a = Struct1{}
	println("struct 3", a.str, a.other.num)
	// Zero val
	var b Struct1
	println("struct 4", b.str, b.other.num)
	// Struct copying on assign
	a = Struct1{"foo", Struct2{5}}
	b = a
	a.str, a.other.num = "bar", 6
	println("struct 5", a.str, a.other.num, b.str, b.other.num)

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
	str   string
	other Struct2
}

type Struct2 struct {
	num int
}
