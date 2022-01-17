package algorithm.genetic.mutator

import algorithm.genetic.Chromosome
import algorithm.genetic.Gene
import kotlin.random.Random

class RandomMutator(
    val mutationRate: Double
) : Mutator {
    override fun mutate(child: Chromosome, genePool: List<Gene>): Chromosome {
        val indicesToMutate =
            (0 until child.size)
                .map { Random.nextDouble(0.0, 1.0) < mutationRate }
                .toBooleanArray()

        indicesToMutate.forEachIndexed { idx, mutate ->
            if (mutate) child.tryReplaceAt(idx, genePool.random())
        }

        return child
    }
}
