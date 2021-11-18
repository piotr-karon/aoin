import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required
import java.io.File
import java.lang.Exception
import kotlin.time.Duration.Companion.milliseconds

val jsonMapper = ObjectMapper().apply {
    registerModule(
        KotlinModule.Builder()
            .withReflectionCacheSize(512)
            .configure(KotlinFeature.NullToEmptyCollection, false)
            .configure(KotlinFeature.NullToEmptyMap, false)
            .configure(KotlinFeature.NullIsSameAsDefault, false)
            .configure(KotlinFeature.SingletonSupport, false)
            .configure(KotlinFeature.StrictNullChecks, false)
            .build()
    )
}

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

    val weight by parser.option(ArgType.Int, shortName = "w", description = "Weight value override")

    parser.parse(args)

    try {
        val problem = DataLoader.load(input)

        println(problem)
    }catch (e: Exception) {
        println("There was error while running the program: ${e.message}.")
        println("Exiting..")
    }
}

object DataLoader {

    fun load(path: String): Problem {
        val file = File(path)
        require(file.exists()) { "File $path doesn't exist" }

        val lines = file.readLines()
        require(lines.isNotEmpty()) { "File is empty" }

        val weightStr = requireNotNull(lines.firstOrNull()?.split(",")?.firstOrNull()) { "Weight not present" }
        val weight = requireNotNull(weightStr.toIntOrNull()) { "Couldn't parse weight $weightStr to Int" }
        require(weight > 0) { "Weight must be greater than 0" }

        val items = lines.drop(1).mapIndexed { idx, it ->
            val lineNumber = idx + 1
            val line = it.split(",")
            Item(
                name = requireNotNull(line.getOrNull(0)) { "Name must not be null. Line $lineNumber"},
                weight = requirePositiveInt(requireNotNull(line.getOrNull(1)){"Weight must not be null"}, "Weight", lineNumber),
                value = requirePositiveInt(requireNotNull(line.getOrNull(2)){"Value must not be null"}, "Value", lineNumber),
                number = lineNumber
            )
        }

        return Problem(
            weightLimit = weight,
            items = items.toSet()
        )
    }
}

data class ProblemResult(
    val timeMillis: Long,
    val weight: Int,
    val value: Int,
    val algorithm: Algorithm
) {
    val time = timeMillis.milliseconds.toString()
}

data class Problem(
    val weightLimit: Int,
    val items: Set<Item>
)

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
    Input file path.
			File should follow this format:
				30,,       – first line contains weight limit
				Gold,1,200 – next lines contain Item name,Weight,Value
				Kettle,1,2
"""

fun requirePositiveInt(string: String, fieldName: String, lineNumber: Int? = null): Int {
    val value = string.toIntOrNull()
    require(value != null) { "$fieldName (value=$string) must be an Int. ${lineNumber?.let { "Line $it" }}"}

    return requirePositiveInt(value, fieldName, lineNumber)
}

fun requirePositiveInt(value: Int, fieldName: String, lineNumber: Int? = null): Int {
    require(value > 0) {"$fieldName (value=$value) must be greater than 0. ${lineNumber?.let { "Line $it" }}"}
    return value
}
