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

    override fun toString() = mainFilePath.fileName.toString()

    companion object {
        val units by lazy {
            val gopath = System.getenv("GOPATH") ?: error("Missing GOPATH")
            val testDir = Paths.get(gopath, "src/github.com/cretz/go2k/compiler/src/test/go")
            Files.list(testDir).filter { it.toString().endsWith(".go") }.map { TestUnit(it) }.toList()
        }
    }
}