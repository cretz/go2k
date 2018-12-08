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
	// TODO:
	// func w/ named returns
	// func w/ multi param names sharing type
	// func w/ multi return names sharing type
	// func w/ vararg
}

func funcNoReturn(v string) {
	println("func 1", v)
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
