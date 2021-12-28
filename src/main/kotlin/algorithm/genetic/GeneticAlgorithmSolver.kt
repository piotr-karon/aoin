package algorithm.genetic

import algorithm.base.Algorithm
import algorithm.genetic.selector.PopulationSelector
import app.Problem
import app.ProblemResult
import kotlin.system.measureTimeMillis

class GeneticAlgorithmSolver(
    private val parameters: GeneticAlgorithmParameters,
) : Algorithm {

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

        repeat(population.size) {
            val parents =
                selector.select(population)

            val child =
                crossoverMethod.crossover(Pair(parents[0], parents[1]))

            val mutatedChild = mutator.mutate(child, genes)

            newGeneration += mutatedChild
        }

        return newGeneration
    }
}
