package app

import algorithm.base.Algorithm
import algorithm.genetic.GeneticAlgorithmParameters
import kotlin.time.Duration.Companion.milliseconds

data class ProblemResult(
    val timeMillis: Long,
    val weightLimit: Int,
    val weight: Int,
    val value: Int,
    val algorithmType: Algorithm.AlgorithmType,
    val inputFileName: String,
    val fittestHist: List<Int?>? = null,
    val expectedOptimum: Int?,
    val actualGenesisSize: Int? = null,
    val geneticParameters: GeneticAlgorithmParameters? = null
) {
    private val time = timeMillis.milliseconds.toString()

    override fun toString(): String {
        return """
            -----------------------------
            Algorithm: $algorithmType
            Time: $time (${timeMillis}ms)
            Knapsack weight limit: $weightLimit
            Result weight: $weight
            Result value: $value (${expectedOptimum?.let { (value.toDouble() / it * 100.0).toString() + "%" }?:""})
            Optimum: $expectedOptimum
            Input file: $inputFileName
            -----------------------------
        """.trimIndent()
    }

    fun asCsvLine() = "${inputFileName.replace(",", "_")},$algorithmType,$timeMillis,$weightLimit,$weight,$value,$expectedOptimum,${fittestCsvCompress()},$actualGenesisSize,${geneticParameters?.asCsvLine()}"

    private fun fittestCsvCompress(): String? =
        fittestHist?.joinToString(separator = ";") { it.toString() }
}
