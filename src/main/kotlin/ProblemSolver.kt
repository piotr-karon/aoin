import kotlin.random.Random

interface ProblemSolver {
    fun solve(problem: Problem): ProblemResult = ProblemResult(
        timeMillis = Random.nextLong(1000, 60000),
        weightLimit = problem.weightLimit,
        weight = Random.nextInt(0, problem.weightLimit),
        value = Random.nextInt(0, 129),
        algorithm = Algorithm.GEN,
        inputFileName = problem.fileName
    )
}
