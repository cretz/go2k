package main

func main() {
	// Boolean
	t := true
	f := false
	println("bool 1", t || f, f || t, t && f, f && t, !f, !t)
	// Equality
	a := 1
	b := 2
	var c uint = 3
	d := -1
	println("eq 1", a == b, a < b, a <= b, a > b, a >= b)
	// Arithmetic
	println("arith 1", a+b, a-b, a|b, a^b)
	println("arith 2", a*b, a/b, a%b, a<<c, a>>c, a&b, a&^b)
	// Unary
	println("unary 1", +a, +d, -a, -d, ^b)
	// Assignment
	a = 4
	b += a
	println("assign 1", a, b)
	b -= a
	println("assign 2", b)
	b |= a
	println("assign 3", b)
	b ^= a
	println("assign 4", b)
	b *= a
	println("assign 5", b)
	b /= a
	println("assign 6", b)
	b %= a
	println("assign 7", b)
	b <<= c
	println("assign 8", b)
	b >>= c
	println("assign 9", b)
	b &= a
	println("assign 10", b)
	b &^= a
	println("assign 11", b)
}
