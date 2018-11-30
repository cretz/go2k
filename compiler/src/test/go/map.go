package main

func main() {
	val := map[string]int{"foo": 12, "bar": 13}
	// Get and get default
	println("get 1", val["foo"])
	println("get 2", val["baz"])
	// Make
	println("make 1", make(map[string]int)["foo"])
	println("make 2", make(map[string]int, 100)["bar"])
	// Set
	val["bar"] = 15
	println("set 1", val["bar"])
	// Len
	println("len 1", len(val))
	// Delete
	delete(val, "bar")
	println("len 2", len(val))
}
