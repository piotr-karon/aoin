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
        val result = knapsackDynamicProgramming(
            knapsackWeight = problem.weightLimit,
            items = problem.items
        )

        return ProblemResult(
            timeMillis = result.executionTimeMillis,
            weightLimit = problem.weightLimit,
            weight = result.weight,
            value = result.value,
            algorithmType = Algorithm.AlgorithmType.REF,
            inputFileName = problem.fileName
        )
    }

    private fun knapsackDynamicProgramming(knapsackWeight: Int, items: Set<Item>): KnapsackResult {
        var resultValue: Int
        var resultWeight: Int

        val time = measureTimeMillis {
            val lookupArray = Array(items.size + 1) { Array(knapsackWeight + 1) { j -> 0 } }
            val weights = items.map { item -> item.weight }
            val values = items.map { item -> item.value }

            for (i in 0..items.size) {
                for (j in 0..knapsackWeight) {
                    if (i == 0 || j == 0)
                        lookupArray[i][j] = 0
                    else if (weights[i - 1] <= j)
                        lookupArray[i][j] =
                            max(values[i - 1] + lookupArray[i - 1][j - weights[i - 1]], lookupArray[i - 1][j])
                    else
                        lookupArray[i][j] = lookupArray[i - 1][j]
                }
            }

            resultValue = lookupArray[items.size][knapsackWeight]
            resultWeight = getKnapsackWeight(lookupArray, weights, values, knapsackWeight)
        }

        return KnapsackResult(
            value = resultValue,
            weight = resultWeight,
            executionTimeMillis = time
        )
    }

    private fun getKnapsackWeight(
        dp: Array<Array<Int>>,
        weights: List<Int>,
        profits: List<Int>,
        knapsackWeight: Int
    ): Int {
        var knapsackCapacity = knapsackWeight
        var resultWeight = 0
        var totalProfit = dp[weights.size - 1][knapsackCapacity]

        for (i in weights.size - 1 downTo 1) {
            if (totalProfit != dp[i - 1][knapsackCapacity]) {
                resultWeight += weights[i]
                knapsackCapacity -= weights[i]
                totalProfit -= profits[i]
            }
        }

        return resultWeight
    }
}
