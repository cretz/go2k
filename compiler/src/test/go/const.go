package main

const (
	c0    = 0
	cm1   = -1
	chuge = 1 << 100

	chuge_1 = chuge - 1
	c1      = chuge >> 100
	c3div2  = 3 / 2
	c1e3    = 1e3

	rsh1 = 1e100 >> 1000
	rsh2 = 1e302 >> 1000

	ctrue  = true
	cfalse = !ctrue
)

const (
	f0              = 0.0
	fm1             = -1.
	fhuge   float64 = 1 << 100
	fhuge_1 float64 = chuge - 1
	f1      float64 = chuge >> 100
	f3div2          = 3. / 2.
	f1e3    float64 = 1e3
)

func main() {
	// println(c0, cm1, c1, cfalse, c1e3)
	// println(fhuge)
}
