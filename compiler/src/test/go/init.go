package main

var (
	a = c + b
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
