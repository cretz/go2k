// Test init order

package main

var (
	// TODO: Ideally we'd test "a = c + b" here but it breaks our tests due to a bug in Go compiler.
	// Ref: https://github.com/golang/go/issues/22326
	a = b + c
	b = f()
	c = f()
	d = 3
)

func f() int {
	d++
	return d
}

func Dump() {
	println("a:", a, "b:", b, "c:", c, "d:", d, "g:", g, "h:", h, "i:", i)
}

var g = f()

var i int

func init() {
	d++
	i = h + 1
}

var h = d + 1

func main() {
	Dump()
}
