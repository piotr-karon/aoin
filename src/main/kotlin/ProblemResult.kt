import kotlin.time.Duration.Companion.milliseconds

data class ProblemResult(
    val timeMillis: Long,
    val weightLimit: Int,
    val weight: Int,
    val value: Int,
    val algorithm: Algorithm,
    val inputFileName: String
) {
    val time = timeMillis.milliseconds.toString()

    override fun toString(): String {
        return """
            time: $time (${timeMillis}ms) | weightLimit: $weightLimit | weight: $weight | value: $value | algorithm: $algorithm | input file: $inputFileName
        """.trimIndent()
    }

    fun asCsvLine() = "${inputFileName.replace(",", "_")},$algorithm,$timeMillis,$weightLimit,$weight,$value"
}
