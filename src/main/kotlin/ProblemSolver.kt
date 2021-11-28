import kotlin.random.Random

interface ProblemSolver {

    fun solve(problem: Problem, algorithmType: Algorithm.AlgorithmType): ProblemResult =
        when (algorithmType) {
            Algorithm.AlgorithmType.REF -> ReferenceAlgorithmSolver.solve(problem)
            Algorithm.AlgorithmType.GEN -> GeneticAlgorithmSolver.solve(problem)
        }
}

object ReferenceAlgorithmSolver : Algorithm {
    // TODO: implement
    override fun solve(problem: Problem): ProblemResult = ProblemResult(
        timeMillis = Random.nextLong(1000, 60000),
        weightLimit = problem.weightLimit,
        weight = Random.nextInt(0, problem.weightLimit),
        value = Random.nextInt(0, 129),
        algorithmType = Algorithm.AlgorithmType.REF,
        inputFileName = problem.fileName
    )
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
