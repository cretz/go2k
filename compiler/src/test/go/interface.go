package main

func main() {
	// Simple iface
	var a Iface1 = &Struct1{"foo"}
	println("iface 1", a.Foo("bar"), a.Foo2("baz"))

	// TODO
	// varargs
	// embeds
	// empty
	// anon
	// out of package pointer methods and non-pointer methods
}

type Iface1 interface {
	Foo(string) string
	Foo2(string) string
}

type Struct1 struct {
	foo string
}

func (s Struct1) Foo(str string) string   { return s.foo + "-foo-" + str }
func (s *Struct1) Foo2(str string) string { return s.foo + "-foo2-" + str }
