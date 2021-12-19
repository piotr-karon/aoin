package algorithm.base

import algorithm.genetic.GeneticAlgorithmSolver
import algorithm.dynamic.ReferenceAlgorithmSolver
import app.Problem
import app.ProblemResult

interface ProblemSolver {

    fun solve(problem: Problem, algorithmType: Algorithm.AlgorithmType): ProblemResult =
        when (algorithmType) {
            Algorithm.AlgorithmType.REF -> ReferenceAlgorithmSolver.solve(problem)
            Algorithm.AlgorithmType.GEN -> GeneticAlgorithmSolver.solve(problem)
        }
}
