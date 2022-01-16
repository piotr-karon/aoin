package algorithm.genetic

import algorithm.base.Algorithm
import algorithm.genetic.selector.PopulationSelector
import app.Problem
import app.ProblemResult
import com.github.michaelbull.logging.InlineLogger
import kotlin.system.measureTimeMillis

class GeneticAlgorithmSolver(
    private val parameters: GeneticAlgorithmParameters,
) : Algorithm {
    private val log = InlineLogger()

    private val selector: PopulationSelector = parameters.selector
    private val genesisPopulationGenerator = parameters.genesisPopulationGenerator
    private val crossoverMethod = parameters.crossover
    private val mutator = parameters.mutator

    override fun solve(problem: Problem): ProblemResult {

        val genes = problem.items.map { Gene(it.name, it.weight, it.value) }

        var fittest: Chromosome

        val executionTimeMillis = measureTimeMillis {
            var population = genesisPopulationGenerator
                .generate(genes, problem.weightLimit)

            fittest = population.fittest

            repeat(parameters.numberOfGenerations) {
                population = evolve(population, genes)
                fittest = population.fittest
//                println("Generation no.: $it. Fittest: ${fittest.value}")
            }
        }

        return ProblemResult(
            timeMillis = executionTimeMillis,
            weightLimit = problem.weightLimit,
            weight = fittest.weight,
            value = fittest.value,
            algorithmType = Algorithm.AlgorithmType.GEN,
            inputFileName = problem.fileName
        )
    }

    private fun evolve(population: Population, genes: List<Gene>): Population {
        val newGeneration = Population()

        repeat(population.size / 2) {
            val parents = selector.select(population)

            val children = crossoverMethod.crossover(Pair(parents[0], parents[1]))

            val mutatedChild1 = mutator.mutate(children.first, genes)
            val mutatedChild2 = mutator.mutate(children.second, genes)

            newGeneration += mutatedChild1
            newGeneration += mutatedChild2
        }

        return newGeneration
    }
}
