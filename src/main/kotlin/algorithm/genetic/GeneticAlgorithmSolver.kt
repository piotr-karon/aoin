package algorithm.genetic

import algorithm.base.Algorithm
import app.Problem
import app.ProblemResult
import kotlin.random.Random
import kotlin.system.measureTimeMillis

data class GeneticAlgorithmParameters(
    val numberOfGenerations: Int = 10,
    val genesisPopulationGenerator: GenesisPopulationGenerator = RandomGenesisPopulationGenerator(230),
    val selector: PopulationSelector = TournamentSelector(5, 2),
    val crossover: Crossover = ScoreBasedCrossover,
    val mutator: Mutator = RandomMutator(0.05)
)

class GeneticAlgorithmSolver(
    private val parameters: GeneticAlgorithmParameters,
) : Algorithm {

    private val selector: PopulationSelector = parameters.selector
    private val genesisPopulationGenerator = parameters.genesisPopulationGenerator
    private val crossoverMethod = parameters.crossover
    private val mutator = parameters.mutator

    // TODO: implement
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
                //println("Gen.: $it. Fittests: ${fittest.value}" + fittest)
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

interface Mutator {
    fun mutate(child: Chromosome, genePool: List<Gene>): Chromosome
}

class RandomMutator(
    val mutationRate: Double
) : Mutator {
    override fun mutate(child: Chromosome, genePool: List<Gene>): Chromosome {
        val indicesToMutate =
            (0 until child.size)
                .map { Random.nextDouble(0.0, 1.0) < mutationRate }
                .toBooleanArray()

        val newChromosome = child.copy()

        indicesToMutate.forEachIndexed { idx, mutate ->
            if (mutate) newChromosome.tryReplaceAt(idx, genePool.random())
        }

        return newChromosome
    }
}

interface Crossover {

    fun crossover(parents: Pair<Chromosome, Chromosome>): Chromosome

}

object ScoreBasedCrossover : Crossover {

    override fun crossover(parents: Pair<Chromosome, Chromosome>): Chromosome {
        val (parent1, parent2) = parents

        val (child, parent) =
            if (parent1.fitnessScore > parent2.fitnessScore) parent1.copy() to parent2
            else parent2.copy() to parent1

        repeat(child.size / 2) {
            val itemFromParent = (0 until parent.size).random()
            val itemToReplaceInChild = (0 until child.size).random()

            child.tryReplaceAt(itemToReplaceInChild, parent[itemFromParent])
        }

        return child
    }

}

interface GenesisPopulationGenerator {

    fun generate(genes: List<Gene>, weightLimit: Int): Population

}

class RandomGenesisPopulationGenerator(private val populationSize: Int) : GenesisPopulationGenerator {

    override fun generate(genes: List<Gene>, weightLimit: Int): Population {
        val generation = Population()

        repeat(populationSize) {
            val backpack = Chromosome(weightLimit)

            val tempGenes: MutableList<Gene?> = genes.toMutableList()
            repeat(Random.nextInt(genes.size)) {
                val randomIdx = Random.nextInt(genes.size)
                val gene = tempGenes[randomIdx]
                tempGenes[randomIdx] = null

                gene?.let {
                    backpack += it
                }
            }

            generation += backpack

        }

        return generation
    }

}

interface PopulationSelector {

    fun select(population: Population): List<Chromosome>
}

class TournamentSelector(val tournamentSize: Int, val numberOfParents: Int) : PopulationSelector {

    override fun select(population: Population): List<Chromosome> = List(numberOfParents) {
        val tournamentPopulation = Population()

        repeat(tournamentSize) {
            tournamentPopulation += population.randomFittest
        }

        tournamentPopulation.fittest
    }


}
