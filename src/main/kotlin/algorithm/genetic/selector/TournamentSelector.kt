package algorithm.genetic.selector

import algorithm.genetic.Chromosome
import algorithm.genetic.Population

class TournamentSelector(val tournamentSize: Int, val numberOfParents: Int) : PopulationSelector {

    override fun select(population: Population): List<Chromosome> = List(numberOfParents) {
        val tournamentPopulation = Population()

        repeat(tournamentSize) {
            tournamentPopulation += population.randomFittest
        }

        tournamentPopulation.fittest
    }


}
