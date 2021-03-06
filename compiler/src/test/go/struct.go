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
	// Struct method non-pointer receiver
	k := Struct7{"foo"}
	println("struct 13", k.Method1())
	l := k.Method3("bar")
	println("struct 14", k.str, l.str)
	// Struct method pointer receiver
	m := Struct7{"foo"}
	println("struct 15", m.Method2())
	n := &Struct7{"bar"}
	o := n.Method4("baz")
	println("struct 16", n.str, o.str)
	p := Struct7{"qux"}
	p.Method5()
	println("struct 17", p.str)
	// Local
	type LocalStruct struct {
		str string
	}
	q := LocalStruct{"foo"}
	println("struct 18", q.str)
	r := &LocalStruct{"bar"}
	println("struct 19", r.str)
	// Anon
	s := struct{ str string }{"foo"}
	println("struct 20", s.str)
	s = struct{ str string }{"bar"}
	println("struct 21", s.str)
	t := struct{ foo struct{ str string } }{}
	println("struct 22", t.foo.str)
	t = struct{ foo struct{ str string } }{foo: struct{ str string }{"baz"}}
	println("struct 23", t.foo.str)
	// Anon assign to/from defined
	u := Struct2{5}
	println("struct 23", u.num)
	u = struct{ num int }{6}
	println("struct 24", u.num)
	v := struct{ num int }{7}
	println("struct 25", v.num)
	v = Struct2{8}
	println("struct 26", v.num)
	// Anon pointer/copy
	w := struct{ str string }{"foo"}
	x := w
	w.str = "bar"
	println("struct 27", w.str, x.str)
	// Empty struct
	type LocalEmpty struct{}
	y := struct{}{}
	z := LocalEmpty{}
	y, z = z, y
	// Anon param and return
	aa := Func1(struct{ str string }{"foo"})
	println("struct 28", aa.str)
	var ab Struct7 = Func1(Struct7{"bar"})
	println("struct 29", ab.str)
	// Equality
	ac := Struct7{"foo"}
	ad := Struct7{"bar"}
	println("struct 30", ac == ad)
	ac.str = "bad"
	println("struct 31", ac == ad)
	// Different struct same field equality
	ae := Struct7{"foo"}
	af := struct{ str string }{"foo"}
	println("struct 32", ae == af)
	// Slice of structs without struct name
	ag := []*Struct7{{"foo"}, {}, {"bar"}}
	println("struct 33", len(ag), ag[0].str, ag[1].str, ag[2].str)
	// Struct with embedded method override
	ah := Struct8{&Struct7{"foo"}}
	println("struct 34", ah.Method1(), ah.Method2(), ah.Method4("test").str)

	// TODO:
	// tags
	// struct embedded but with var overrides and method overrides
	// nested embeddeds with top level multi-depth access and name ambiguities
	// same-named method and top-level-func called inside other method of same type w/ unnamed receiver (testing "this" ambiguity issues)
	// equality
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

type Struct7 struct {
	str string
}

func (Struct7) Method1() string  { return "method-1" }
func (*Struct7) Method2() string { return "method-2" }
func (s Struct7) Method3(str string) *Struct7 {
	s.str = "method-3-" + str
	return &s
}
func (s *Struct7) Method4(str string) *Struct7 {
	s.str = "method-4-" + str
	return s
}
func (s *Struct7) Method5() {
	*s = Struct7{"method-5-" + s.str}
}

func Func1(p1 struct{ str string }) struct{ str string } {
	return Struct7{"func-1-" + p1.str}
}

type Struct8 struct {
	*Struct7
}

func (*Struct8) Method2() string { return "other-method-2" }
func (s Struct8) Method4(str string) Struct8 {
	s.str = "other-method-4-" + str
	return s
}
