package app

import requirePositiveInt
import java.io.File

object FileLoader {
    fun load(path: String): List<File> {
        val file = File(path)
        debugScope {
            println("Input path: ${file.absolutePath}")
        }
        require(file.isDirectory) { "Given input path $path must exist and be directory" }
        require(requireNotNull(file.listFiles()).isNotEmpty()) { "Input directory must not be empty" }

        return file.listFiles()
            ?.filter { it.isFile }
            ?: listOf()
    }
}

object DataLoader {

    fun load(path: String): List<Problem> {
        val problems = FileLoader.load(path)
            .map {
                if (it.extension == "csv") it.loadCsv()
                else if (it.extension.isEmpty()) it.loadPlain()
                else throw IllegalArgumentException("Can't parse ${it.extension} file")
            }

        return problems
    }

    private fun File.loadPlain(): Problem {
        val lines = this.readLines()
        require(lines.isNotEmpty()) { "File is empty" }

        val weightLimit = lines.first().split(" ")[1].toIntOrNull()!!

        val items = lines.drop(1).mapIndexed { idx, line ->
            val elements = line.split(" ")
            Item(
                name = idx.toString(),
                weight = elements[0].toIntOrNull()!!,
                value = elements[1].toIntOrNull()!!
            )
        }

        return Problem(
            weightLimit = weightLimit,
            items = items.toSet(),
            fileName = this.name
        )

    }

    private fun File.loadCsv(): Problem {
        val lines = this.readLines()
        require(lines.isNotEmpty()) { "File is empty" }

        val weightStr = requireNotNull(lines.firstOrNull()?.split(",")?.firstOrNull()) { "Weight not present" }
        val weight = requireNotNull(weightStr.toIntOrNull()) { "Couldn't parse weight $weightStr to Int" }
        require(weight > 0) { "Weight must be greater than 0" }

        val items = lines.drop(1).mapIndexed { idx, line ->
            val lineNumber = idx + 1
            val lineElements = line.split(",")
            Item(
                name = requireNotNull(lineElements.getOrNull(0)) { "Name must not be null. Line $lineNumber" },
                weight = requirePositiveInt(
                    requireNotNull(lineElements.getOrNull(1)) { "Weight must not be null" },
                    "Weight",
                    lineNumber
                ),
                value = requirePositiveInt(
                    requireNotNull(lineElements.getOrNull(2)) { "Value must not be null" },
                    "Value",
                    lineNumber
                ),
            )
        }

        return Problem(
            weightLimit = weight,
            items = items.toSet(),
            fileName = this.name
        )
    }
}
