import algorithm.base.AlgorithmDetails
import algorithm.base.ProblemSolver
import algorithm.genetic.GeneticAlgorithmParameters
import algorithm.genetic.crossover.ScoreBasedCrossover
import algorithm.genetic.generator.RandomGenesisPopulationGenerator
import algorithm.genetic.mutator.RandomMutator
import algorithm.genetic.selector.TournamentSelector
import app.DataLoader
import app.ResultSaver
import app.tuning.InputParams
import org.junit.jupiter.api.Test
import java.time.Instant

class SimulatorRunner {

    @Test
    fun runSimulation() {
        val input = "./data"
        val output = "./simuli"
        val fileName = "sim-${Instant.now()}"
        val repeats = 40

        val problems = DataLoader.load(input)

        repeat(repeats) { repeat ->
            println("### REPEAT $repeat #####")

            val results = details.flatMap { algorithmDetails ->
                problems.map { problem ->
                    ProblemSolver.solve(problem, algorithmDetails)
                }
            }

            ResultSaver.save(results, output, fileName )
        }
    }
}

private val geneticParams = listOf(
    InputParams(
        numberOfGenerations = 500,
        genesisPopulationSize = 400,
        tournamentSize = 2,
        mutationRate = 0.05
    ),
    InputParams(
        numberOfGenerations = 500,
        genesisPopulationSize = 300,
        tournamentSize = 2,
        mutationRate = 0.05
    )
)

private val geneticDetails = geneticParams.map { params ->
        AlgorithmDetails.Genetic(
            GeneticAlgorithmParameters(
                numberOfGenerations = params.numberOfGenerations,
                genesisPopulationGenerator =
                RandomGenesisPopulationGenerator(
                    populationSize = params.genesisPopulationSize
                ),
                selector = TournamentSelector(params.tournamentSize, 2),
                crossover = ScoreBasedCrossover,
                mutator = RandomMutator(params.mutationRate)
            )
        )
}

private val dynamicDetails = listOf(
    AlgorithmDetails.DynamicProgramming
)

private val details = geneticDetails + dynamicDetails
