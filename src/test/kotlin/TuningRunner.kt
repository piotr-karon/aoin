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
    fun runTuning(): Unit {
        val input = "./data"
        val output = "./tuning"
        val fileName = "tuning-${Instant.now()}"
        val repeats = 3

        val problems = DataLoader.load(input)

        problems.forEachIndexed { idx, problem ->
            gen().mapIndexed { pId, params ->
                repeat(repeats) { repeat ->
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
                    ResultSaver.saveTuning(res, params, output, fileName)
                    println(
                        "Exec time: ${
                            String.format(
                                "%5s",
                                res.timeMillis.toString()
                            )
                        } || ${(res.value.toDouble() / (res.expectedOptimum ?: 1)* 100.0).roundToInt()}% ||${pId}-${repeat} ${problem.fileName}:" +
                            " items: ${problem.items.size} || $params"
                    )
                }
            }
        }
    }
}

// Fixed values
// genesis population size depend on size of instance
// generations depend
private val mutationRates = listOf(0.05, 0.1)
private val generationSizes = listOf(500)
private val genesisSize = listOf(100, 200, 300, 400)
private val tournamentSizes = listOf(2, 5, 10)

fun gen() =
    mutationRates.flatMap { mutationRates ->
        generationSizes.flatMap { genNum ->
            genesisSize.flatMap { genStartSize ->
                tournamentSizes.map { tSize ->
                    InputParams(
                        numberOfGenerations = genNum,
                        genesisPopulationSize = genStartSize,
                        tournamentSize = tSize,
                        mutationRate = mutationRates,
                    )
                }
            }
        }
    }
