import java.io.File
import java.time.Instant
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile

object ResultSaver {

    fun save(results: List<ProblemResult>, outputPath: String, fileName: String?): File {
        if (fileName != null) {
            require(fileName.isNotBlank()) { "Output file name cannot be blank." }
        }

        val outputDir = Path(outputPath).apply {
            createDirectories()
        }

        val nonNullFileName = fileName ?: Instant.now().toString()

        val fileWithExtension = if (nonNullFileName.takeLast(4) == ".csv") nonNullFileName
        else "$nonNullFileName.csv"

        val file = outputDir.resolve(fileWithExtension)
            .createFile()
            .toFile()

        val lines = results
            .joinToString(separator = System.lineSeparator(), transform = ProblemResult::asCsvLine)

        file.writeText(lines)

        return file
    }
}
