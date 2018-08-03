package go2k.compile

import kastree.ast.Writer
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class CompilerTest : TestBase() {
    @ParameterizedTest(name = "{0}")
    @MethodSource("unitProvider")
    fun testCompiler(unit: TestUnit) {
        println("YAY: ${unit.mainFilePath}")
        println("GO OUT: ${unit.goRunOutput}")
        // Parse
        val parsed = Parser.parse(unit.mainFilePath.toString())
        println("PARSED: $parsed")
        // Compile
        val compiled = parsed.packages.packages.map {
            Compiler.compilePackage(it).also {
                it.files.forEach { (name, code) ->
                    println("CODE FOR $name:\n" + Writer.write(code))
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun unitProvider() = TestUnit.units
    }
}