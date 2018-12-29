package main

func main() {
	// Type of slice
	a := typeIntSlice{1, 2}
	println("type 1", a[0], a[1])
	b := make(typeIntSlice, 5, 10)
	b = append(b, 12, 13)
	println("type 2", b[0], b[5], b[6], len(b), cap(b))
	// Methods
	c := typeIntSlice{1, 2}
	println("type 3", c.Method1(1))
	c.Method2(5)
	println("type 4", c.Method1(2))
	// Pointer
	d := &typeIntSlice{3, 4}
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
	f = -17
	println("type 10", -f, +f, -e, +e, ^e)
	f.Method2(35)
	println("type 11", e.Method1(), f)
	println("type 12", int(e), uint16(f))
	// Type of uint16
	var g typeUint16 = 45
	h := typeUint16(17)
	g++
	println("type 13", g, g+5) //, g-6, g|7, g^8, g*9, g/10, g%11, g<<12, g>>13, g&14, g&^15)
	println("type 14", g+h, g-h, g|h, g^h, g*h, g/h, g%h, g&h, g&^h)
	println("type 15", g == 46, g < 100, g > 1, g == h, g != h, g < h, g > h, g <= h, g >= h)
	h = 17
	println("type 16", -h, +h, -g, +g, ^g)
	h.Method2(35)
	println("type 17", g.Method1(), h)
	println("type 18", int(g), uint16(h))
	// Type of string
	i := typeString("foobar")
	println("type 19", i, i+"baz", i[0], i[1:3], i[:4], i[4:], len(i), i.Method1(5))
	i.Method2("baz")
	var j typeString = "bazqux"
	println("type 20", i, j)

	// TODO:
	// nil slice
	// other types: int, uint16, string, rune array, bool, other struct, pointer, func, other iface, map, and chan
	// as consts, params, returns
	// operations, including across types of same underlying type
	// local types
	// aliases
}

type intSlice []int

type (
	typeIntSlice []int
	typeInt      int
	typeUint16   uint16
	typeString   string
	// typeRune rune
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

func (t typeIntSlice) Method1(index int) int { return t[index] }
func (t *typeIntSlice) Method2(v int)        { *t = append(*t, v) }

func (t typeInt) Method1() typeInt   { return t + 12 }
func (t *typeInt) Method2(v typeInt) { *t = v }

func (t typeUint16) Method1() typeUint16   { return t + 12 }
func (t *typeUint16) Method2(v typeUint16) { *t = v }

func (t typeString) Method1(index int) byte { return t[index] }
func (t *typeString) Method2(v string)      { *t = "method-2-" + typeString(v) }
