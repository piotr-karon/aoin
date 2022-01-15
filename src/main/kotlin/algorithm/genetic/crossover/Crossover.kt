package algorithm.genetic.crossover

import algorithm.genetic.Chromosome

interface Crossover {

    fun crossover(parents: Pair<Chromosome, Chromosome>): Pair<Chromosome, Chromosome>

}
