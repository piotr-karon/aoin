package algorithm.genetic

import algorithm.base.Algorithm
import app.Problem
import app.ProblemResult
import kotlin.random.Random

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
