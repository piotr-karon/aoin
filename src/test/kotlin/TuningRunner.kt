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
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.concurrent.ThreadPoolExecutor
import kotlin.concurrent.thread
import kotlin.math.roundToInt

class TuningRunner {

    @Test
    fun runTuning(): Unit = runBlocking {
        val input = "./data"
        val output = "./tuning"
        val fileName = "tuning-${Instant.now()}"
        val repeats = 1

        val problems = DataLoader.load(input)
        val threads = mutableListOf<Job>()

        problems.forEachIndexed { idx, problem ->
            println("${problem.fileName}: items: ${problem.items.size}")
            gen().map { params ->
                repeat(repeats) { repeat ->
//                    threads += launch {
                        val res = ProblemSolver.solve(
                            problem,
                            AlgorithmDetails.Genetic(
                                GeneticAlgorithmParameters(
                                    numberOfGenerations = params.numberOfGenerations,
                                    genesisPopulationGenerator =
                                    RandomGenesisPopulationGenerator(
                                        populationSize = params.genesisPopulationSize.coerceAtMost((problem.items.size * 0.8).roundToInt())
                                    ),
                                    selector = TournamentSelector(params.tournamentSize, 2),
                                    crossover = ScoreBasedCrossover,
                                    mutator = RandomMutator(params.mutationRate)
                                )
                            )
                        )

                    ResultSaver.saveTuning(res, params, output, fileName + "$idx$repeat")
//                    }
                }
            }
            threads.forEach { it.start() }
            threads.forEach { it.join() }
        }
    }
}

// Fixed values
// genesis population size depend on size of instance
// generations depend
private val mutationRates = listOf(0.05, 0.1)
private val generationSizes = listOf(50, 100, 150, 200, 250, 300, 400, 500)
private val genesisSize = listOf(10, 30, 50, 100)

fun gen() = mutationRates.flatMap { mutationRates ->
    generationSizes.flatMap { genNum ->
        genesisSize.map { genStartSize ->
            InputParams(
                numberOfGenerations = genNum,
                genesisPopulationSize = genStartSize,
                tournamentSize = 2,
                mutationRate = mutationRates,
            )
        }
    }
}
