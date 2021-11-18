import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required
import java.io.File
import java.lang.Exception
import kotlin.time.Duration.Companion.milliseconds

enum class Algorithm {
    REF, GEN
}

fun main(args: Array<String>) {
    val parser = ArgParser("knapsack_solver")

    val input by parser.option(ArgType.String, shortName = "i", description = inputDescription).required()
    val output by parser.option(ArgType.String, shortName = "o", description = "Output file name")
        .default("${System.currentTimeMillis()}.txt")
    val algorithm by parser.option(
        ArgType.Choice<Algorithm>(),
        shortName = "a",
        description = "Algorithm: ref - reference, gen - genetic"
    ).required()

    parser.parse(args)

    try {
        val problems = DataLoader.load(input)
        println("Loaded ${problems.size} problem instances")

        val results = problems.map {
            println("Solving problems using $algorithm algorithm")

            object : ProblemSolver {}.solve(it).also { println("Resolved. Result: \n$it") }
        }

    } catch (e: Exception) {
        e.printStackTrace()
        println("There was error while running the program: ${e.message}.")
        println("Exiting..")
    }
}

object DataLoader {

    fun load(path: String): List<Problem> {
        val file = File(path)
        require(file.isDirectory) { "Given input path $path must exist and be directory" }
        require(requireNotNull(file.listFiles()).isNotEmpty()) { "Input directory must not be empty" }

        val problems = file.listFiles()?.map {

            val lines = it.readLines()
            require(lines.isNotEmpty()) { "File is empty" }

            val weightStr = requireNotNull(lines.firstOrNull()?.split(",")?.firstOrNull()) { "Weight not present" }
            val weight = requireNotNull(weightStr.toIntOrNull()) { "Couldn't parse weight $weightStr to Int" }
            require(weight > 0) { "Weight must be greater than 0" }

            val items = lines.drop(1).mapIndexed { idx, it ->
                val lineNumber = idx + 1
                val line = it.split(",")
                Item(
                    name = requireNotNull(line.getOrNull(0)) { "Name must not be null. Line $lineNumber" },
                    weight = requirePositiveInt(
                        requireNotNull(line.getOrNull(1)) { "Weight must not be null" },
                        "Weight",
                        lineNumber
                    ),
                    value = requirePositiveInt(
                        requireNotNull(line.getOrNull(2)) { "Value must not be null" },
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

interface ProblemSolver {
    fun solve(problem: Problem): ProblemResult = ProblemResult(1000 * 3 * 62, 30, 29, 300, Algorithm.GEN, "data1.csv")
}

data class ProblemResult(
    val timeMillis: Long,
    val weightLimit: Int,
    val weight: Int,
    val value: Int,
    val algorithm: Algorithm,
    val inputFileName: String
) {
    val time = timeMillis.milliseconds.toString()
    val outputFileName = "resolved-$inputFileName"

    override fun toString(): String {
        return """
            time: $time (${timeMillis}ms)
            weightLimit: $weightLimit
            weight: $weight
            value: $value
            algorithm: $algorithm
            output file: $outputFileName
        """.trimIndent()
    }

    fun csv() = "$timeMillis,$weightLimit,$weight,$value,$algorithm,$outputFileName"
}

data class Problem(
    val weightLimit: Int,
    val items: Set<Item>,
    val fileName: String
) {
    fun overrideWeight(weightLimit: Int) = this.copy(weightLimit = weightLimit)
}

data class Item(
    val name: String,
    val weight: Int,
    val value: Int,
    val number: Int
) {
    init {
        require(name.isNotBlank()) { "Name must not be blank. Item number: $number" }
        requirePositiveInt(value, "Value", number)
        requirePositiveInt(weight, "Weight", number)
    }
}

const val inputDescription = """
    Path to a directory containing files with problem data.
			File should follow this format:
				30,,       – first line contains weight limit
				Gold,1,200 – next lines contain Item name,Weight,Value
				Kettle,1,2
"""

fun requirePositiveInt(string: String, fieldName: String, lineNumber: Int? = null): Int {
    val value = string.toIntOrNull()
    require(value != null) { "$fieldName (value=$string) must be an Int. ${lineNumber?.let { "Line $it" }}" }

    return requirePositiveInt(value, fieldName, lineNumber)
}

fun requirePositiveInt(value: Int, fieldName: String, lineNumber: Int? = null): Int {
    require(value > 0) { "$fieldName (value=$value) must be greater than 0. ${lineNumber?.let { "Line $it" }}" }
    return value
}
