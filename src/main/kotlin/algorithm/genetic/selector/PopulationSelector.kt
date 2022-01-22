package algorithm.genetic.selector

import algorithm.genetic.Chromosome
import algorithm.genetic.Population

interface PopulationSelector {

    fun select(population: Population): List<Chromosome>

    val size: Int
}
