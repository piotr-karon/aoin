package app

import app.tuning.InputParams
import java.io.File
import java.time.Instant
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile

object ResultSaver {

    fun save(results: List<ProblemResult>, outputDirectory: String, fileName: String?): File {
        if (fileName != null) {
            require(fileName.isNotBlank()) { "Output file name cannot be blank." }
        }

        val outputDirPath = Path(outputDirectory).apply {
            createDirectories()
        }

        val nonNullFileName = fileName ?: Instant.now().toString()

        val fileWithExtension = if (nonNullFileName.takeLast(4) == ".csv") nonNullFileName
        else "$nonNullFileName.csv"

        val file = outputDirPath.resolve(fileWithExtension)
            .toFile()

        if(!file.exists()){
            file.createNewFile()
        }

        val lines = results
            .joinToString(separator = System.lineSeparator(), transform = ProblemResult::asCsvLine)

        file.appendText(lines)

        return file
    }

    fun saveTuning(result: ProblemResult, inputParams: InputParams, outputPath: String, fileName: String): File {
        require(fileName.isNotBlank()) { "Output file name cannot be blank." }

        val outputDir = Path(outputPath).apply {
            createDirectories()
        }

        val fileWithExtension = if (fileName.takeLast(4) == ".csv") fileName
        else "$fileName.csv"

        val file = outputDir.resolve(fileWithExtension)
            .toFile()

        if(!file.exists()){
            file.createNewFile()
        }

        val line = result.asCsvLine() + "," + inputParams.asCsvLine() + System.lineSeparator()

        file.appendText(line)

        return file
    }
}
