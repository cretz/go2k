package go2k.compile

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class CompilerTest : TestBase() {
    @ParameterizedTest(name = "{0}")
    @MethodSource("unitProvider")
    fun testCompiler(unit: TestUnit) {
        println("YAY: ${unit.mainFilePath}")
        println("GO OUT: ${unit.goRunOutput}")
        // Parse
        val res = Parser.parse(unit.mainFilePath.toString())
        println("RES: $res")
    }

    companion object {
        @JvmStatic
        fun unitProvider() = TestUnit.units
    }
}