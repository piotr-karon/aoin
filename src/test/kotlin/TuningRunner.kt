import app.tuning.Tuning
import org.junit.jupiter.api.Test
import java.io.File

class TuningRunner {

    @Test
    fun runTuning() {
        Tuning.runTuning()
    }

    @Test
    fun renamer() {
        val inp = "./instances_01_KP/low-dimensional"
        val opt = "./instances_01_KP/low-dimensional-optimum"

        val instFiles = File(inp).listFiles()
        val optFiles = File(opt).listFiles()

        instFiles.forEach { instFile ->
            val text = instFile.readLines().toMutableList()
            val opt = optFiles.find { it.name == instFile.name }!!.readLines()[0].trim()

            val firstLine = text[0]
            text[0] = "$firstLine $opt"

            instFile.writeText(text.joinToString(separator = System.lineSeparator()))
        }
    }
}
