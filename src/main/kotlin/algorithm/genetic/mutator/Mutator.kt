package algorithm.genetic.mutator

import algorithm.genetic.Chromosome
import algorithm.genetic.Gene

interface Mutator {
    fun mutate(child: Chromosome, genePool: List<Gene>): Chromosome
    val mutationRate: Double
}
