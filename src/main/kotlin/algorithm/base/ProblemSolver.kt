package algorithm.base

import algorithm.genetic.GeneticAlgorithmSolver
import algorithm.dynamic.ReferenceAlgorithmSolver
import algorithm.genetic.GeneticAlgorithmParameters
import app.Problem
import app.ProblemResult

object ProblemSolver {

    fun solve(problem: Problem, algorithmDetails: AlgorithmDetails): ProblemResult =
        when (algorithmDetails) {
            is AlgorithmDetails.DynamicProgramming -> ReferenceAlgorithmSolver.solve(problem)
            is AlgorithmDetails.Genetic -> GeneticAlgorithmSolver(algorithmDetails.parameters).solve(problem)
        }

}

sealed interface AlgorithmDetails {

    val type: String

    object DynamicProgramming: AlgorithmDetails {
        override val type = "Dynamic Programming"
    }

    data class Genetic(
        val parameters: GeneticAlgorithmParameters
    ): AlgorithmDetails {
        override val type = "Genetic"
    }

}
