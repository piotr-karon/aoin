package algorithm.base

import app.Problem
import app.ProblemResult

interface Algorithm {
    enum class AlgorithmType {
        REF, GEN
    }

    fun solve(problem: Problem): ProblemResult
}
