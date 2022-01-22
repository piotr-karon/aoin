package app.tuning

import algorithm.base.AlgorithmDetails
import algorithm.base.ProblemSolver
import algorithm.genetic.GeneticAlgorithmParameters
import algorithm.genetic.crossover.ScoreBasedCrossover
import algorithm.genetic.generator.RandomGenesisPopulationGenerator
import algorithm.genetic.mutator.RandomMutator
import algorithm.genetic.selector.TournamentSelector
import app.DataLoader
import app.ResultSaver
import java.time.Instant
import kotlin.math.roundToInt

object Tuning {

    fun runTuning(): Unit {
        val input = "./data"
        val output = "./tuning"
        val fileName = "tuning-${Instant.now()}"
        val repeats = 20

        val problems = DataLoader.load(input)

        repeat(repeats) { repeat ->
            println("#### REPEAT $repeat #######")
            problems.forEachIndexed { idx, problem ->
                gen.mapIndexed { pId, params ->
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
                        } || ${(res.value.toDouble() / (res.expectedOptimum ?: 1) * 100.0).roundToInt()}% ||${pId}-${repeat} ${problem.fileName}:" +
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
private val mutationRates = listOf(0.05)
private val generationSizes = listOf(500)
private val genesisSize = listOf(400)
private val tournamentSizes = listOf(2)

val gen =
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
