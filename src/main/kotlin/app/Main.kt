package app

import algorithm.base.AlgorithmDetails
import algorithm.base.ProblemSolver
import algorithm.genetic.GeneticAlgorithmParameters
import algorithm.genetic.crossover.ScoreBasedCrossover
import algorithm.genetic.generator.RandomGenesisPopulationGenerator
import algorithm.genetic.mutator.RandomMutator
import algorithm.genetic.selector.TournamentSelector
import app.tuning.Tuning
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import java.lang.Exception

private var enableDebug = true

fun debugScope(fn: () -> Unit) {
    if (enableDebug) fn()
}

@OptIn(ExperimentalCli::class)
fun main(args: Array<String>) {
    Tuning.runTuning()

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
