package algorithm.genetic.selector

import algorithm.genetic.Chromosome
import algorithm.genetic.Population

class TournamentSelector(val tournamentSize: Int, val numberOfParents: Int) : PopulationSelector {
    override val size: Int
        get() = tournamentSize

    override fun select(population: Population): List<Chromosome> = List(numberOfParents) {
        val tournamentPopulation = Population()

        tournamentPopulation += population.randomFittest(tournamentSize)

        tournamentPopulation.fittest
    }


}
