package app

import algorithm.base.AlgorithmDetails
import algorithm.base.ProblemSolver
import algorithm.genetic.GeneticAlgorithmParameters
import algorithm.genetic.crossover.ScoreBasedCrossover
import algorithm.genetic.generator.RandomGenesisPopulationGenerator
import algorithm.genetic.mutator.RandomMutator
import algorithm.genetic.selector.TournamentSelector
import app.tuning.InputParams
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.time.Instant
import kotlin.math.roundToInt

private var enableDebug = true

fun debugScope(fn: () -> Unit) {
    if (enableDebug) fn()
}

@OptIn(ExperimentalCli::class)
fun main(args: Array<String>) {
    runTuning()
//    val parser = ArgParser("aoin")
//
//    parser.subcommands(Reference(), Genetic())
//    parser.parse(args)
}

@OptIn(ExperimentalCli::class)
abstract class AlgorithmSubcommand(
    name: String,
    description: String
): Subcommand(name, description) {

    abstract val algorithmDetails: AlgorithmDetails

    // Use docker mounts for that
    val input by option(
        ArgType.String,
        shortName = "i",
        description = "Input directory path. Don't use while using docker version"
    ).default("/input")

    val outputDir by option(
        ArgType.String,
        shortName = "od",
        description = "Output directory path. Don't use while using docker version"
    ).default("/output")

    private val output by option(ArgType.String, shortName = "o", description = "Output file name")

    override fun execute() {
        try {
            val problems = DataLoader.load(input)
            println("Loaded ${problems.size} problem instances")

            val results = problems.map { problem ->

                println("Solving problems using ${algorithmDetails.type} algorithm")

                ProblemSolver.solve(problem, algorithmDetails).also { println("Resolved. Result: \n$it") }
            }

            ResultSaver.save(results, outputDir, output)

        } catch (e: Exception) {
            debugScope { e.printStackTrace() }
            println("There was error while running the program: ${e.message}.")
            println("Exiting..")
        }
    }
}

class Genetic : AlgorithmSubcommand("genetic", "Run genetic algorithm") {

    val numberOfGenerations by option(ArgType.Int).default(100)
    val genesisPopulationSize by option(ArgType.Int).default(20)
    val tournamentSize by option(ArgType.Int).default(5)
    val numberOfParentsForCrossover by option(ArgType.Int).default(2)
    val mutationRate by option(ArgType.Double).default(0.05)

    override val algorithmDetails: AlgorithmDetails
        get() = AlgorithmDetails.Genetic(GeneticAlgorithmParameters(
            numberOfGenerations = numberOfGenerations,
            genesisPopulationGenerator = RandomGenesisPopulationGenerator(populationSize = genesisPopulationSize),
            selector = TournamentSelector(tournamentSize, numberOfParentsForCrossover),
            crossover = ScoreBasedCrossover,
            mutator = RandomMutator(mutationRate)
        ))
}

class Reference : AlgorithmSubcommand("reference", "Run reference algorithm - dynamic programming") {
    override val algorithmDetails: AlgorithmDetails
        get() = AlgorithmDetails.DynamicProgramming
}

fun runTuning(): Unit  {
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
                println("$repeat ${problem.fileName} with input params: $params | time=${res.timeMillis} %opt=${(res.value / (res.expectedOptimum ?: 1) * 100.0).roundToInt()}")

//                    }
            }
        }
//        threads.forEach { it.start() }
//        threads.forEach { it.join() }
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
