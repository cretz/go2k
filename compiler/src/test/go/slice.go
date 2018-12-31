package main

var uintsLit = []uint{1, 2, 3}
var uint8sLit = []uint8{1, 2, 3}
var runesLit = []rune{'1', '2', '3'}
var uint16sLit = []uint16{1, 2, 3}
var uint32sLit = []uint32{1, 2, 3}
var uint64sLit = []uint64{1, 2, 3}
var intsLit = []int{1, 2, 3}
var int8sLit = []int8{1, 2, 3}
var int16sLit = []int16{1, 2, 3}
var int32sLit = []int32{1, 2, 3}
var int64sLit = []int64{1, 2, 3}
var float32sLit = []float32{1, 2, 3}
var float64sLit = []float64{1, 2, 3}
var stringsLit = []string{"foo", "bar", "baz", "qux"}
var nilStringSlice []string

func main() {
	// Access
	println("access 1", uintsLit[1])
	println("access 2", intsLit[1])
	println("access 3", stringsLit[1:][0])
	// Len
	println("len 1", len(uint32sLit))
	println("len 2", len(int32sLit))
	println("len 3", len(nilStringSlice))
	// Cap
	println("cap 1", cap(uint32sLit))
	println("cap 2", cap(make([]int, 0, 12)))
	println("cap 3", cap(nilStringSlice))
	println("cap 4", cap(stringsLit[:1:2]))
	// Make
	println("make 1", make([]uint32, 5)[4])
	println("make 2", make([]string, 5)[4])
	println("make 3", cap(make([]string, 5, 10)))
	// Append
	println("append 1", append([]uint64{5}, 6, 7)[2])
	println("append 2", append([]string{"foo"}, "bar", "baz")[2])
	println("append 3", append(nilStringSlice, "bar", "baz")[1])
	// Copy
	newStrings := []string{"one", "two", "three"}
	println("copy 1", copy(newStrings[1:], stringsLit[3:]))
	println("copy 2", newStrings[0], newStrings[1], newStrings[2])
	// Keyed literal
	foo := []string{2: "two", "three", 5: "five"}
	println("lit 1", foo[0], foo[2], foo[3], foo[5], len(foo))

	// TODO: nil
}
