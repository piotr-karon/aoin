import kotlin.math.max
import kotlin.random.Random

interface ProblemSolver {

    fun solve(problem: Problem, algorithmType: Algorithm.AlgorithmType): ProblemResult =
        when (algorithmType) {
            Algorithm.AlgorithmType.REF -> ReferenceAlgorithmSolver.solve(problem)
            Algorithm.AlgorithmType.GEN -> GeneticAlgorithmSolver.solve(problem)
        }
}

object ReferenceAlgorithmSolver : Algorithm {
    override fun solve(problem: Problem): ProblemResult {
        val (time, result) = Utils.measureTimeMillisWithResult {
            knapsackDynamicProgramming(
                knapsackWeight = problem.weightLimit,
                items = problem.items
            )
        }

        return ProblemResult(
            timeMillis = time,
            weightLimit = problem.weightLimit,
            weight = result.weight,
            value = result.value,
            algorithmType = Algorithm.AlgorithmType.REF,
            inputFileName = problem.fileName
        )
    }

    private fun knapsackDynamicProgramming(knapsackWeight: Int, items: Set<Item>): KnapsackResult {
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

        val resultValue = lookupArray[items.size][knapsackWeight]
        val resultWeight = getKnapsackWeight(lookupArray, weights, values, knapsackWeight)

        return KnapsackResult(
            value = resultValue,
            weight = resultWeight
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


object GeneticAlgorithmSolver : Algorithm {
    // TODO: implement
    override fun solve(problem: Problem): ProblemResult = ProblemResult(
        timeMillis = Random.nextLong(1000, 60000),
        weightLimit = problem.weightLimit,
        weight = Random.nextInt(0, problem.weightLimit),
        value = Random.nextInt(0, 129),
        algorithmType = Algorithm.AlgorithmType.GEN,
        inputFileName = problem.fileName
    )
}

interface Algorithm {
    enum class AlgorithmType {
        REF, GEN
    }

    fun solve(problem: Problem): ProblemResult
}


data class KnapsackResult(
    val value: Int,
    val weight: Int
)