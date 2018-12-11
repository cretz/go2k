package go2k.compile

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.streams.toList

class TestUnit(
    val mainFilePath: Path
) {
    val goRunOutput by lazy {
        val pb = ProcessBuilder("go", "run", mainFilePath.toString()).redirectErrorStream(true)
        val proc = pb.start()
        proc.inputStream.use { it.readBytes().toString(Charsets.UTF_8) }.also {
            require(proc.exitValue() == 0) { "Unexpected exit value of ${proc.exitValue()}, output: $it" }
        }
    }

    // Get embedded compiler errors on some
    val useExternalCompiler = when (mainFilePath.fileName.toString()) {
        "chan.go" -> true
        else -> false
    }

    override fun toString() = mainFilePath.fileName.toString()

    companion object {
        val allUnits by lazy { localUnits + goUnits }

        val localUnits by lazy {
            val gopath = System.getenv("GOPATH") ?: error("Missing GOPATH")
            val testDir = Paths.get(gopath, "src/github.com/cretz/go2k/compiler/src/test/go")
            require(Files.isDirectory(testDir)) { "Missing local test dir: $testDir" }
            testDir.toFile().walkTopDown().
                filter { it.toString().endsWith(".go") }.map { TestUnit(it.toPath()) }.toList()
        }

        val goUnits by lazy {
            val gopath = System.getenv("GOPATH") ?: error("Missing GOPATH")
            val testDir = Paths.get(gopath, "src/github.com/golang/go/test")
            require(Files.isDirectory(testDir)) { "Missing Go test dir: $testDir" }
            // Only "// run" for now
            Files.list(testDir).filter {
                it.toString().endsWith(".go") && it.toFile().readText().startsWith("// run\n\n")
            }.map { TestUnit(it) }.toList()
        }
    }
}