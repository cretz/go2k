package main

func main() {
	str := "foo"
	// Unicode
	println("unicode 1", "日本語", "日本語"[0], len("日本語"), "日本語"[1:4])
	println("unicode 2", "\u65e5本\U00008a9e")
	// Escapes
	println("escapes 1", `\t$\n
	\n`)
	println("escapes 2", "\a \b \f \n \r \t \v \\ ' \" $foo")
	// Concat
	println("concat 1", str+"bar")
	str += "baz"
	println("concat 2", str)
	// Access
	println("access 1", str[3])
	println("access 2", str[2:4])
	// Len
	println("len 1", len(str))
	// Rune
	println("runes 1", ' ', '\a', '\b', '\f', '\n', '\r', '\t', '\v', '\\', '\'', '"', '$', '日', '\u65e5', '\U00008a9e', '🎄')
	r := 'a'
	println("runes 2", r, r+5)
	// Copy to bytes
	byts := make([]byte, 3)
	copy(byts, str[2:5])
	for _, v := range byts {
		println("copy 1", v)
	}
}
