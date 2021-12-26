package app

import algorithm.base.Algorithm
import algorithm.base.AlgorithmDetails
import algorithm.base.ProblemSolver
import algorithm.genetic.GeneticAlgorithmParameters
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required
import java.lang.Exception

private var enableDebug = false

fun debugScope(fn: () -> Unit) {
    if (enableDebug) fn()
}

fun main(args: Array<String>) {
    val parser = ArgParser("aoin")

    val algorithm by parser.option(
        ArgType.Choice<Algorithm.AlgorithmType>(),
        shortName = "a",
        description = "Algorithm: ref - reference, gen - genetic"
    ).required()

    // Use docker mounts for that
    val input by parser.option(
        ArgType.String,
        shortName = "i",
        description = "Input directory path. Don't use while using docker version"
    ).default("/input")
    val outputDir by parser.option(
        ArgType.String,
        shortName = "od",
        description = "Output directory path. Don't use while using docker version"
    ).default("/output")
    val output by parser.option(ArgType.String, shortName = "o", description = "Output file name")

    val debug by parser.option(ArgType.Boolean, shortName = "d", description = "Debug mode").default(false)
    parser.parse(args)
    enableDebug = debug

    try {
        val problems = DataLoader.load(input)
        println("Loaded ${problems.size} problem instances")

        val results = problems.map { problem ->
            println("Solving problems using $algorithm algorithm")

            val details = when(algorithm){
                Algorithm.AlgorithmType.REF -> AlgorithmDetails.DynamicProgramming
                Algorithm.AlgorithmType.GEN -> AlgorithmDetails.Genetic(GeneticAlgorithmParameters())
            }

            ProblemSolver.solve(problem, details).also { println("Resolved. Result: \n$it") }
        }

        ResultSaver.save(results, outputDir, output)

    } catch (e: Exception) {
        debugScope { e.printStackTrace() }
        println("There was error while running the program: ${e.message}.")
        println("Exiting..")
    }
}
