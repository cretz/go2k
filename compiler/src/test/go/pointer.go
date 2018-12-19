package main

func main() {
	// Primitive
	var a *int
	println("pointer 1", a == nil)
	b := 5
	a = &b
	b++
	println("pointer 2", *a, b)
	(*a)++
	println("pointer 3", *a, b)
	// Array
	c := [...]int{1, 2, 3}
	a = &c[1]
	*a++
	println("pointer 4", *a, c[1])
	// For loop
	var d *int
	for i := 0; i < 10; i++ {
		if i > 0 {
			d = &i
		}
	}
	println("pointer 5", *d)
	// Range loop define
	var e *string
	for k, v := range map[int]string{1: "foo"} {
		if k > 0 {
			e = &v
		}
	}
	println("pointer 6", *e)
	// Range loop full assign
	var k int
	var v string
	for k, v = range map[int]string{2: "bar"} {
		if k > 0 {
			e = &v
		}
	}
	println("pointer 7", *e)
	// Range define ambiguous
	var val = "notbaz"
	for key, val := range map[int]string{3: "baz"} {
		if key > 0 {
			e = &val
		}
	}
	println("pointer 8", *e, val)
	// Switch
	var f *string
	switch s := "foo"; s {
	case "foo":
		f = &s
	}
	println("pointer 9", *f)
	// If
	var h *string
	if s := "bar"; len(s) > 2 {
		h = &s
	}
	println("pointer 10", *h)
	// Else
	var i *string
	if s := "baz"; len(s) > 4 {
	} else {
		i = &s
	}
	println("pointer 11", *i)
	// Anon func
	getPtr := func(s string) *string {
		s = "foo" + s
		return &s
	}
	j := getPtr("bar")
	println("pointer 12", *j)
	changePtr := func(s *string) { *s = *s + "qux" }
	l := "baz"
	println("pointer 13", l)
	changePtr(&l)
	println("pointer 14", l)
	m := "foo"
	getMPtr := func() *string { return &m }
	*getMPtr() = m + "baz"
	println("pointer 15", m)
	// Array lit
	n := &[...]int{1, 2, 3}
	println("pointer 16", n[1])
	o := n
	o[1] = 10
	println("pointer 17", n[1])
	// Array lit paren
	n = &([...]int{4, 5, 6})
	println("pointer 18", n[1])
	o = n
	o[1] = 11
	println("pointer 19", n[1])
	// Slice lit
	p := &[]int{1, 2, 3}
	println("pointer 20", (*p)[1])
	q := p
	(*q)[1] = 10
	println("pointer 21", (*p)[1])
	// Map lit
	r := &map[int]int{5: 6, 7: 8}
	println("pointer 22", (*r)[7])
	s := r
	(*s)[7] = 10
	println("pointer 23", (*r)[7])
	// Struct lit
	t := &struct1{num: 1}
	println("pointer 24", t.num)
	u := t
	*u = struct1{num: 2}
	println("pointer 25", t.num)
	// Struct field ref
	w := struct1{num: 5}
	x := &w.num
	*x = 6
	println("pointer 26", w.num)

	// TODO:
	// top level prim derefed in function
	// type switch
	// other composite lits: named struct, anon struct
	// pointer indirect
	// struct method, incl nil ptr receiver
	// struct method ref
	// pointer to pointer
}

type struct1 struct {
	num int
}
