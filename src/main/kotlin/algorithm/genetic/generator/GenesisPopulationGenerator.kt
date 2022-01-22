package algorithm.genetic.generator

import algorithm.genetic.Gene
import algorithm.genetic.Population

interface GenesisPopulationGenerator {

    fun generate(genes: List<Gene>, weightLimit: Int): Population

    val populationSize: Int
}
