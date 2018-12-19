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
	// Embedded struct simple
	c := Struct3{Struct4{6, 7}, 8}
	d := c
	c.num = 9
	c.num2 = 10
	c.Struct4.num2 = 11
	println("struct 6", c.num, c.num2, c.Struct4.num, c.Struct4.num2, d.num, d.num2, d.Struct4.num, d.Struct4.num2)
	// Embedded struct create with field name
	c = Struct3{Struct4: Struct4{num: 8}}
	println("struct 7", c.num)
	// Pointer
	e := &Struct5{}
	println("struct 8", e.Struct2 == nil, e.other == nil, e.num2)
	e = &Struct5{Struct2: &Struct2{13}, other: &Struct1{str: "test"}}
	println("struct 9", e.num2, e.num, e.Struct2.num, e.other.str)
	var f Struct5
	e = &f
	f.Struct2 = &Struct2{}
	e.num = 15
	println("struct 10", f.num, e.num)
	// Pointer pointer
	g := &Struct6{num: 45}
	h := &g
	i := &Struct1{str: "test"}
	(*h).other = &i
	println("struct 11", g.num, (*h).num, (*g.other).str, (*(*h).other).str)
	// Deref assign
	e = &Struct5{num2: 12}
	j := e
	*e = Struct5{num2: 13}
	println("struct 12", e.num2, j.num2)
	// // Struct method no pointer
	// k := Struct7{"foo"}
	// k.Method3("bar")
	// println("struct 13", k.str)

	// TODO:
	// empty struct
	// struct methods
	// local structs
	// anonymous structs (locally, as params, as arrays, nested, etc)
	// non-pointer struct copying on call args
	// tags
	// struct embedded but with var overrides and method overrides
	// nested embeddeds with top level multi-depth access and name ambiguities
}

type Struct1 struct {
	str   string
	other Struct2
}

type Struct2 struct {
	num int
}

type Struct3 struct {
	Struct4
	num2 int
}

type Struct4 struct {
	num  int
	num2 int
}

type Struct5 struct {
	*Struct2
	other *Struct1
	num2  int
}

type Struct6 struct {
	other **Struct1
	num   int
}

// type Struct7 struct {
// 	str string
// }

// func (Struct7) Method1() string  { return "method 1" }
// func (*Struct7) Method2() string { return "method 2" }
// func (s Struct7) Method3(str string) {
// 	s.str = str
// }
