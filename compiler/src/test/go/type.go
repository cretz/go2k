package main

func main() {
	// Type of slice
	a := typeIntSlice{1, 2}
	println("type 1", a[0], a[1])
	b := make(typeIntSlice, 5, 10)
	b = append(b, 12, 13)
	a = nil
	println("type 2", b[0], b[5], b[6], len(b), cap(b), len(a), cap(a), len(append(a, 20)))
	// Methods
	c := typeIntSlice{1, 2}
	println("type 3", c.Method1(1), a.Method3())
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
	println("type 13", g, g+5, g-6, g|7, g^8, g*9, g/10, g%11, g<<12, g>>13, g<<h, g&14, g&^15)
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
	// Type of rune
	k := typeRune('f')
	var l typeRune = 103
	k++
	println("type 21", k, l, k+5, k-6, k|7, k^8, k*9, k/10, k%11, k<<12, k>>13, k&14, k&^15)
	println("type 22", k+l, k-l, k|l, k^l, k*l, k/l, k%l, k&l, k&^l)
	println("type 23", k == 46, k < 100, k > 1, k == l, k != l, k < l, k > l, k <= l, k >= l)
	l.Method2(105)
	println("type 24", k.Method1(), l)
	println("type 25", rune(k), uint16(l))
	// Type of int array
	m := typeIntArray{1: 1, 2}
	println("type 26", m[0], m[1], m[2], m[3], len(m[2:3]), m[2:][0])
	var n typeIntArray
	n[4] = 12
	println("type 27", n[0], n[4], len(n), cap(n), n.Method1(4))
	n.Method2(15)
	println("type 28", n[0], n[3])
	// Type of bool
	o := typeBool(true)
	var p typeBool
	println("type 29", o, p, !o, o == p, o || p, o && p, o.Method1(), p.Method1())
	p.Method2(true)
	println("type 30", p)
	// Type of struct
	q := typeStructB{"test"}
	var r typeStructB
	println("type 31", q.foo, r.foo, q.Method1())
	r.Method2("test2")
	println("type 32", r.foo)
	// Type of struct pointer
	var s typeStructPointer = &typeStructA{"test"}
	println("type 33", s.foo)
	s = nil
	println("type 34", s == nil)
	// Type of type of int
	var t typeTypeInt = 45
	u := typeTypeInt(19)
	t++
	println("type 35", t, t+5, t-6, t|7, t^8, t*9, t/10, t%11, t<<12, t>>13, t&14, t&^15)
	println("type 36", t+u, t-u, t|u, t^u, t*u, t/u, t%u, t&u, t&^u)
	println("type 37", t == 46, t < 100, t > -1, t == u, t != u, t < u, t > u, t <= u, t >= u)
	u = -17
	println("type 38", -u, +u, -t, +t, ^t)
	u.Method2(37)
	println("type 39", t.Method1(), u)
	println("type 40", int(t), uint16(u))
	// Type of int pointer
	v := 44
	var w typeIntPointer = &v
	*w++
	println("type 41", *w, *w**w)
	// Type of func
	x := typeFunc(func(s string) string { return "testfunc1-" + s })
	var y typeFunc = func(s string) string { return "testfunc2-" + s }
	println("type 42", x("foo"), y("bar"))
	x = typeStructA{"baz"}.Method1
	println("type 43", x("qux"))
	x = y
	println("type 44", x.Method1("bar"))
	x.Method2("another-test")
	println("type 45", x("foo"))
	// Type of map
	z := typeMap{"foo": "1", "bar": "2"}
	println("type 46", z["foo"], z["notthere"], len(z))
	delete(z, "foo")
	println("type 47", z["foo"], len(z))
	z = make(typeMap, 300)
	z["baz"] = "7"
	println("type 47", z["baz"], len(z), z.Method1("baz"))
	z.Method2("blah")
	println("type 48", z["foo"], len(z))
	// Type of chan
	aa := make(typeChan, 1)
	aa <- "foo"
	println("type 49", <-aa)
	close(aa)
	aa.Method2("bar")
	println("type 50", aa.Method1())
	close(aa)

	// TODO:
	// other types: iface
	// as consts, params, returns, chan subjects, map subjects, array subjects, slice subjects
	// local types
	// aliases
}

type (
	typeIntSlice      []int
	typeInt           int
	typeUint16        uint16
	typeString        string
	typeRune          rune
	typeIntArray      [5]int
	typeBool          bool
	typeStructA       struct{ foo string }
	typeStructB       typeStructA
	typeStructPointer *typeStructA
	typeTypeInt       typeInt
	typeIntPointer    *int
	typeFunc          func(string) string
	typeMap           map[string]string
	typeChan          chan string
)

func (t typeIntSlice) Method1(index int) int { return t[index] }
func (t *typeIntSlice) Method2(v int)        { *t = append(*t, v) }
func (t typeIntSlice) Method3() int          { return len(t) }

func (t typeInt) Method1() typeInt   { return t + 12 }
func (t *typeInt) Method2(v typeInt) { *t = v }

func (t typeUint16) Method1() typeUint16   { return t + 12 }
func (t *typeUint16) Method2(v typeUint16) { *t = v }

func (t typeString) Method1(index int) byte { return t[index] }
func (t *typeString) Method2(v string)      { *t = "method-2-" + typeString(v) }

func (t typeRune) Method1() typeRune   { return t + 12 }
func (t *typeRune) Method2(v typeRune) { *t = v }

func (t typeIntArray) Method1(index int) int { return t[index] }
func (t *typeIntArray) Method2(v int)        { *t = typeIntArray{3: v} }

func (t typeBool) Method1() typeBool   { return !t }
func (t *typeBool) Method2(v typeBool) { *t = v }

func (t typeStructA) Method1(v string) string { return "method-1-" + t.foo + "-" + v }

func (t typeStructB) Method1() string   { return "method-1-" + t.foo }
func (t *typeStructB) Method2(v string) { *t = typeStructB{foo: v} }

func (t typeTypeInt) Method1() typeTypeInt   { return t + 13 }
func (t *typeTypeInt) Method2(v typeTypeInt) { *t = v + 4 }

func (t typeFunc) Method1(v string) string { return "method-1-" + t(v) }
func (t *typeFunc) Method2(v string)       { *t = func(subV string) string { return v + subV } }

func (t typeMap) Method1(v string) string { return t[v] }
func (t *typeMap) Method2(v string)       { *t = typeMap{"foo": v} }

func (t typeChan) Method1() string { return <-t }
func (t *typeChan) Method2(v string) {
	*t = make(typeChan, 1)
	*t <- v
}
