package algorithm.dynamic

import algorithm.base.Algorithm
import algorithm.base.KnapsackResult
import app.Item
import app.Problem
import app.ProblemResult
import kotlin.math.max
import kotlin.system.measureTimeMillis

object ReferenceAlgorithmSolver : Algorithm {
    override fun solve(problem: Problem): ProblemResult {
        val result = solve(problem.weightLimit, problem.items)

        return ProblemResult(
            timeMillis = result.executionTimeMillis,
            weightLimit = problem.weightLimit,
            weight = result.weight,
            value = result.value,
            algorithmType = Algorithm.AlgorithmType.REF,
            inputFileName = problem.fileName,
            expectedOptimum = problem.expectedOptimum
        )
    }

    private fun solve(W: Int, items: Set<Item>): KnapsackResult {
        val weight = items.map { it.weight }.toTypedArray()
        val values = items.map { it.value }.toTypedArray()

        var result: Pair<Int, Int>
        val time = measureTimeMillis {
            result = solve(W, weight, values)
        }

        return KnapsackResult(
            value = result.first,
            weight = result.second,
            executionTimeMillis = time
        )

    }

    private fun solve(weightLimit: Int, weight: Array<Int>, values: Array<Int>): Pair<Int, Int> {
        val itemsSize = values.size

        if (weightLimit < 0) return 0 to 0
        val k = Array(itemsSize + 1) { IntArray(weightLimit + 1) { 0 } }
        for (i in 0..itemsSize) {
            for (j in 0..weightLimit) {
                if (i == 0 || j == 0)
                    k[i][j] = 0
                else if (weight[i - 1] <= j)
                    k[i][j] = max(
                        a = values[i - 1] + k[i - 1][j - weight[i - 1]],
                        b = k[i - 1][j]
                    )
                else
                    k[i][j] = k[i - 1][j]
            }
        }
        return k[itemsSize][weightLimit] to weightLimit
    }
}
