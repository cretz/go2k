package main

var uints [3]uint
var uint8s [3]uint8
var runes [3]rune
var uint16s [3]uint16
var uint32s [3]uint32
var uint64s [3]uint64
var ints [3]int
var int8s [3]int8
var int16s [3]int16
var int32s [3]int32
var int64s [3]int64
var float32s [3]float32
var float64s [3]float64
var strings [3]string

func main() {
	// Access
	uints[1] = 5
	println("access 1", uints[1])
	ints[1] = -5
	println("access 2", ints[1])
	strings[1] = "foo"
	println("access 3", strings[1:][0])
	// Len
	println("len 1", len(uint32s))
	println("len 2", len(int32s))
	// Cap
	println("cap 1", cap(uint32s))
	println("cap 2", cap(strings[:1:2]))
	// Literal
	foo := [6]int{1, 2, 3, 5}
	println("lit 1", len(foo), foo[2], foo[5])
	bar := [...]int{1, 2, 3, 5}
	println("lit 2", len(bar), bar[2])
	baz := [150]bool{'f': true}
	println("lit 3", len(baz), baz['a'], baz['f'])
	qux := [...]int{3: 4, 2, 12: 15}
	println("lit 4", len(qux), qux[3], qux[4], qux[11], qux[12])
}
