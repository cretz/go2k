package main

func main() {
	// Type of slice
	a := intSlice{1, 2}
	println("type 1", a[0], a[1])
	b := make(intSlice, 5, 10)
	b = append(b, 12, 13)
	println("type 2", b[0], b[5], b[6], len(b), cap(b))
	// Methods
	c := intSlice{1, 2}
	println("type 3", c.Method1(1))
	c.Method2(5)
	println("type 4", c.Method1(2))
	// Pointer
	d := &intSlice{3, 4}
	println("type 5", (*d)[0], d.Method1(1))
	d.Method2(12)
	println("type 6", d.Method1(2))
	// Type of int
	var e typeInt = 45
	f := typeInt(17)
	e++
	println("type 7", e, e+5, e-6, e|7, e^8, e*9, e/10, e%11, e<<12, e>>13, e&14, e&^15)
	println("type 8", e+f, e-f, e|f, e^f, e*f, e/f, e%f, e&f, e&^f)
	println("type 9", e == 46, e < 100, e > -1, e == f, e != f, e < f, e > f, e <= f, e >= f)
	// TODO: unary ops

	// TODO:
	// nil slice
	// other types: int, uint16, string, array, bool, other struct, pointer, func, other iface, map, and chan
	// as consts, params, returns
	// operations, including across types of same underlying type
	// local types
	// aliases
}

type intSlice []int

func (i intSlice) Method1(index int) int { return i[index] }

func (i *intSlice) Method2(v int) { *i = append(*i, v) }

type (
	typeInt int
	// typeUint16        uint16
	// typeString        string
	// typeIntArray      [5]int
	// typeBool          bool
	// typeStructA       struct{ foo string }
	// typeStructB       typeStructA
	// typeIntPointer    *int
	// typeStructPointer *typeStructA
	// typeFunc          func(string) string
	// typeMap           map[string]string
	// typeChan          chan string
)
