import java.io.File

object DataLoader {

    fun load(path: String): List<Problem> {
        val file = File(path)
        debugScope {
            println("Input path: ${file.absolutePath}")
        }
        require(file.isDirectory) { "Given input path $path must exist and be directory" }
        require(requireNotNull(file.listFiles()).isNotEmpty()) { "Input directory must not be empty" }

        val problems = file.listFiles()?.map {
            debugScope {
                println("Loading ${it.name}")
            }
            val lines = it.readLines()
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
                    number = lineNumber
                )
            }

            Problem(
                weightLimit = weight,
                items = items.toSet(),
                fileName = it.name
            )
        }

        return problems ?: throw IllegalStateException("There was error reading files")
    }
}
