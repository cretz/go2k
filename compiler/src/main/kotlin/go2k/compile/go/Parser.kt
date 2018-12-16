package go2k.compile.go

import go2k.compile.go.dumppb.Packages
import java.nio.file.Files
import java.nio.file.Paths

interface Parser {
    fun parse(vararg packagesOrFiles: String): ParseResult

    data class ParseResult(val packages: Packages)

    open class FromGoProcess : Parser {
        val goDumpPath by lazy {
            val goPath = System.getenv("GOPATH") ?: error("Missing GOPATH env var")
            Paths.get(goPath, "src/github.com/cretz/go-dump/main.go")!!.also {
                require(Files.isRegularFile(it)) { "Not a file: $it" }
            }
        }

        override fun parse(vararg packagesOrFiles: String): ParseResult {
            // Run proc
            val pb = ProcessBuilder("go", "run", goDumpPath.toString(),
                "--", *packagesOrFiles).redirectErrorStream(true)
            val proc = pb.start()
            val output = proc.inputStream.use { it.readBytes() }
            require(proc.exitValue() == 0) {
                "Unexpected exit value of ${proc.exitValue()}, output: ${output.toString(Charsets.UTF_8)}"
            }
            // Parse proto
            return ParseResult(Packages.protoUnmarshal(output))
        }

        companion object : FromGoProcess()
    }

    companion object : Parser {
        override fun parse(vararg packagesOrFiles: String) = FromGoProcess.parse(*packagesOrFiles)
    }
}