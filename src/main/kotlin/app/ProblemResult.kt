package app

import algorithm.base.Algorithm
import kotlin.time.Duration.Companion.milliseconds

data class ProblemResult(
    val timeMillis: Long,
    val weightLimit: Int,
    val weight: Int,
    val value: Int,
    val algorithmType: Algorithm.AlgorithmType,
    val inputFileName: String,
    val fittestHist: List<Int>? = null,
    val expectedOptimum: Int?
) {
    private val time = timeMillis.milliseconds.toString()

    override fun toString(): String {
        return """
            -----------------------------
            Algorithm: $algorithmType
            Time: $time (${timeMillis}ms)
            Knapsack weight limit: $weightLimit
            Result weight: $weight
            Result value: $value
            Input file: $inputFileName
            -----------------------------
        """.trimIndent()
    }

    fun asCsvLine() = "${inputFileName.replace(",", "_")},$algorithmType,$timeMillis,$weightLimit,$weight,$value,$expectedOptimum,${fittestCsvCompress()}"

    private fun fittestCsvCompress(): String? =
        fittestHist?.joinToString(separator = ";") { it.toString() }
}
