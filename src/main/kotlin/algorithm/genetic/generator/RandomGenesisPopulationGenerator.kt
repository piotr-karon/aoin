package algorithm.genetic.generator

import algorithm.genetic.Chromosome
import algorithm.genetic.Gene
import algorithm.genetic.Population
import kotlin.random.Random

class RandomGenesisPopulationGenerator(override val populationSize: Int) : GenesisPopulationGenerator {

    override fun generate(genes: List<Gene>, weightLimit: Int): Population {
        val generation = Population()

        repeat(populationSize) {
            val backpack = Chromosome(weightLimit)

            val randomizedGenes: List<Gene> = genes.shuffled()

            randomizedGenes.forEach {
                backpack.tryToAdd(it)
            }

            generation += backpack

        }

        return generation
    }

}
