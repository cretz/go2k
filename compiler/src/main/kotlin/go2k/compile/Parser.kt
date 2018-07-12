package go2k.compile

import go2k.compile.dumppb.Packages
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

interface Parser {
    fun parsePackage(qualifiedPackage: String): ParseResult {
        val goPath = System.getenv("GOPATH") ?: error("Missing GOPATH env var")
        return parseDir(Paths.get(goPath, "src", qualifiedPackage))
    }

    fun parseDir(fullPath: Path): ParseResult

    data class ParseResult(val packages: Packages)

    open class FromGoProcess : Parser {
        override fun parseDir(fullPath: Path): ParseResult {
            if (!Files.isDirectory(fullPath)) error("Not a directory: $fullPath")
            // Get dump path
            val goPath = System.getenv("GOPATH") ?: error("Missing GOPATH env var")
            val dumpAstPath = Paths.get(goPath, "src/github.com/cretz/go-dump/main.go")
            if (!Files.isRegularFile(dumpAstPath)) error("Not a file: $dumpAstPath")
            // Run proc
            val pb = ProcessBuilder("go", "run", dumpAstPath.toString(), fullPath.toString()).redirectErrorStream(true)
            val proc = pb.start()
            val output = proc.inputStream.use { it.readBytes() }
            require(proc.exitValue() == 0) { "Unexpected exit value of ${proc.exitValue()}, output: $output" }
            // Parse proto
            return ParseResult(Packages.protoUnmarshal(output))
        }

        companion object : FromGoProcess()
    }

    companion object : Parser {
        override fun parseDir(fullPath: Path) = FromGoProcess.parseDir(fullPath)
    }
}