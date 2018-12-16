package main

func main() {
	// For
	sum := 0
	for i := 0; i < 6; i++ {
		if sum += i; sum < 5 {
			continue
		}
		println("for 1", i, sum)
	}
	println("for 2", sum)
	// For existing var
	i := 0
	for i = 0; i < 6; i++ {
		println("for 3", i)
	}
	println("for 4", i)
	// For condition only
	i = 0
	for i < 6 {
		i++
		println("for 5", i)
	}
	println("for 6", i)
	// Infinite
	i = 0
	for {
		println("for 7", i)
		if i++; i >= 6 {
			break
		}
	}
	println("for 8", i)
	// Array range
	arr := [...]int{1, 2, 3, 4}
	for k, v := range arr {
		if v%2 == 0 {
			continue
		}
		println("for 9", k, v)
	}
	// Array existing vars and break
	k, v := -5, -5
	for k, v = range arr {
		if v > 3 {
			break
		}
		println("for 10", k, v)
	}
	println("for 11", k, v)
	// Array ignoring parts
	for _, v := range arr {
		println("for 12", v)
	}
	for k := range arr {
		println("for 13", k)
	}
	for range arr {
		println("for 15")
	}
	// Slice
	slice := []int{1, 2, 3, 4}
	for k, v := range slice {
		if v%2 == 0 {
			continue
		}
		println("for 16", k, v)
	}
	// Map
	m := map[string]int{"a": 1, "b": 2, "c": 3, "d": 4}
	for k, v := range m {
		if v%2 == 0 {
			continue
		}
		println("for 17", len(k), v%2)
	}
	// Map existing vars and break
	mapK, mapV := "", -5
	for mapK, mapV = range m {
		if mapV > 3 {
			break
		}
	}
	println("for 18", len(mapK), mapV > 3)
	// Map ignoring parts
	for _, v := range m {
		println("for 19", v > 0)
	}
	for k := range m {
		println("for 20", len(k))
	}
	for range m {
		println("for 21")
	}
	// String range
	str := "test"
	for k, v := range str {
		if k%2 == 0 {
			continue
		}
		println("for 22", k, v)
	}
	// String existing vars and break
	strK, strV := -5, '5'
	for strK, strV = range str {
		if strK > 3 {
			break
		}
		println("for 23", strK, strV)
	}
	println("for 24", strK, strV)
	// String ignoring parts
	for _, v := range str {
		println("for 25", v)
	}
	for k := range str {
		println("for 26", k)
	}
	for range str {
		println("for 27")
	}

	// TODO: chan
	// TODO: labeled complex breaks and continues and gotos
}
