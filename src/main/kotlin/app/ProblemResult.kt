package app

import algorithm.base.Algorithm
import kotlin.time.Duration.Companion.milliseconds

data class ProblemResult(
    val timeMillis: Long,
    val weightLimit: Int,
    val weight: Int,
    val value: Int,
    val algorithmType: Algorithm.AlgorithmType,
    val inputFileName: String
) {
    private val time = timeMillis.milliseconds.toString()

    override fun toString(): String {
        return """
            -----------------------------
            Algorithm: $algorithmType
            Time: $time (${timeMillis}ms)
            Knapsack weight: $weightLimit
            Result weight: $weight
            Result value: $value
            Input file: $inputFileName
            -----------------------------
        """.trimIndent()
    }

    fun asCsvLine() = "${inputFileName.replace(",", "_")},$algorithmType,$timeMillis,$weightLimit,$weight,$value"
}
