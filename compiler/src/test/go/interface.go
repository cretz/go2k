package main

func main() {
	// Simple iface
	var a Iface1 = &Iface1Struct{"foo"}
	println("iface 1", a.Foo("bar"))
	a.Foo2("baz")
	println("iface 2", a.Foo("test"), a.(*Iface1Struct).foo)
	b := Iface1Struct{"bar"}
	c := Iface1(&b)
	println("iface 3", c.Foo3(a, "a", "b"), b.foo)
	// // Embeds
	var d Iface4Num
	d += 14
	var e Iface4 = d
	println("iface 4", e.Foo(12), e.Bar(), e.Baz(35))
	println("iface 5", e.(Iface2).Foo(15))

	// TODO
	// empty
	// anon
	// local
	// out of package pointer methods and non-pointer methods
	// returns, params, slice subjects, etc
}

type Iface1 interface {
	Foo(string) string
	Foo2(string)
	Foo3(i Iface1, strs ...string) string
}

type Iface1Struct struct {
	foo string
}

func (s Iface1Struct) Foo(str string) string { return s.foo + "-foo-" + str }
func (s *Iface1Struct) Foo2(str string)      { *s = Iface1Struct{s.foo + "-foo2-" + str} }
func (s Iface1Struct) Foo3(i Iface1, strs ...string) string {
	for _, v := range strs {
		s.foo += i.Foo("-" + v + "-")
	}
	return s.foo
}

type Iface2 interface {
	Foo(i int) int
}

type Iface3 interface {
	Bar() uint16
}

type Iface4 interface {
	Iface2
	Iface3
	Baz(uint16) Iface4Num
}

type Iface4Num uint16

func (i Iface4Num) Foo(j int) int          { return int(i) * j }
func (i Iface4Num) Bar() uint16            { return uint16(i) * 3 }
func (i Iface4Num) Baz(j uint16) Iface4Num { return i * Iface4Num(j) }
