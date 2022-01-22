package algorithm.genetic

import algorithm.genetic.crossover.Crossover
import algorithm.genetic.crossover.ScoreBasedCrossover
import algorithm.genetic.generator.GenesisPopulationGenerator
import algorithm.genetic.generator.RandomGenesisPopulationGenerator
import algorithm.genetic.mutator.Mutator
import algorithm.genetic.mutator.RandomMutator
import algorithm.genetic.selector.PopulationSelector
import algorithm.genetic.selector.TournamentSelector

data class GeneticAlgorithmParameters(
    val numberOfGenerations: Int = 1000,
    val genesisPopulationGenerator: GenesisPopulationGenerator = RandomGenesisPopulationGenerator(230),
    val selector: PopulationSelector = TournamentSelector(5, 2),
    val crossover: Crossover = ScoreBasedCrossover,
    val mutator: Mutator = RandomMutator(0.05)
){
    fun asCsvLine() =
        "$numberOfGenerations,${genesisPopulationGenerator.populationSize},${selector.size},${mutator.mutationRate}"
}
