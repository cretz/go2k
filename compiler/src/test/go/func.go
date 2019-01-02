package main

func main() {
	// No return
	funcNoReturn("foo")
	// Multi return
	println("func 2", funcOneReturn("foo"))
	a, b := funcTwoReturn("foo")
	println("func 3", a, b)
	a, b, c := funcThreeReturn("foo")
	println("func 4", a, b, c)
	a, b, c, d := funcFourReturn("foo")
	println("func 5", a, b, c, d)
	a = funcOneNamedReturn("foo")
	println("func 6", a)
	a, b = funcTwoNamedReturn("foo")
	println("func 7", a, b)
	// Multi-return passed direct
	a, b = funcTwoParamTwoResult(funcTwoNamedReturn("foo"))
	println("func 8", a, b)
	// Vararg
	a = funcVararg("foo", "bar", "baz")
	println("func 9", a)
	a = funcVararg()
	println("func 10", a)
	a = funcVararg(funcTwoNamedReturn("foo"))
	println("func 11", a)
	// Anon func
	fn := func(str string) string { return str + "-anon-1" }
	println("func 12", fn("foo"))
	println("func 13", func(str string) string { return str + "-anon-2" }("foo"))
	fn = funcOneReturn
	println("func 14", fn("baz"))
}

func funcNoReturn(v string) {
	println("func 1", v)
	return
}

func funcOneReturn(v string) string {
	return v + "-one"
}

func funcTwoReturn(v string) (string, string) {
	return v + "-two-1", v + "-two-2"
}

func funcThreeReturn(v string) (string, string, string) {
	return v + "-three-1", v + "-three-2", v + "-three-3"
}

func funcFourReturn(v string) (string, string, string, string) {
	return v + "-four-1", v + "-four-2", v + "-four-3", v + "-four-4"
}

func funcOneNamedReturn(v string) (one string) {
	one = v + "-onenamed"
	return
}

func funcTwoNamedReturn(v string) (one, two string) {
	one, two = v+"-twonamed-1", v+"-twonamed-2"
	return
}

func funcTwoParamTwoResult(v1, v2 string) (string, string) {
	return v1 + "-twoparam-1", v2 + "-twoparam-2"
}

func funcVararg(v ...string) string {
	s := "-"
	for _, str := range v {
		s += str + "-vararg-"
	}
	return s
}
