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


    object DynamicProgramming: AlgorithmDetails

    data class Genetic(
        val parameters: GeneticAlgorithmParameters
    ): AlgorithmDetails

}
