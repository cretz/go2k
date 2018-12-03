package main

func main() {
	// Simple assign and define
	a := 5
	println("simple 1", a)
	a = 6
	println("simple 2", a)
	// Simple multi
	b, c, d := 'b', 4, 'd'
	println("simple multi 1", b, c, d)
	b, c, d = 'B', 6, 'D'
	println("simple multi 2", b, c, d)
	b, d = d, b
	println("simple multi 3", b, d)
	// Index multi
	e, f := [10]int{}, []int{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}
	c, e[c], f[c/2] = 300, 400, 500
	println("index multi 1", c, e[6], f[3])
	// Blank
	g, _, h := "one", "two", "three"
	println("blank 1", g, h)
	f[f[5]], _ = 300, f[f[5]]+1
	println("blank 2", f[5])

	// TODO:
	// LHS with fn call, advanced index exprs, etc (see some commented out code below)
}

// type foo struct {
// 	a int
// 	x []int
// 	y []*foo
// }

// // func (f *foo) Y(i int) *foo { return f.y[i] }

// func (f *foo) incrA() *foo {
// 	f.a++
// 	return f
// }

// func main() {
// 	a := &foo{0, []int{5, 6}, []*foo{&foo{a: 1}}}

// 	// a.a, a.Y(a.a).a, a.y[a.a].a, a.x[a.Y(a.a).a] = 12, 6, 7, 8
// 	a.a, a.incrA().x[a.a] = 3, 7
// }
