import kotlin.time.Duration.Companion.milliseconds

data class ProblemResult(
    val timeMillis: Long,
    val weightLimit: Int,
    val weight: Int,
    val value: Int,
    val algorithmType: Algorithm.AlgorithmType,
    val inputFileName: String
) {
    val time = timeMillis.milliseconds.toString()

    override fun toString(): String {
        return """
            time: $time (${timeMillis}ms) | weightLimit: $weightLimit | weight: $weight | value: $value | algorithm: $algorithmType | input file: $inputFileName
        """.trimIndent()
    }

    fun asCsvLine() = "${inputFileName.replace(",", "_")},$algorithmType,$timeMillis,$weightLimit,$weight,$value"
}
