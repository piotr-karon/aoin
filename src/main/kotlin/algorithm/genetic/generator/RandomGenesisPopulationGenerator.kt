package algorithm.genetic.generator

import algorithm.genetic.Chromosome
import algorithm.genetic.Gene
import algorithm.genetic.Population
import kotlin.random.Random

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
