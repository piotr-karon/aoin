package algorithm.genetic.crossover

import algorithm.genetic.Chromosome

object ScoreBasedCrossover : Crossover {

    override fun crossover(parents: Pair<Chromosome, Chromosome>): Pair<Chromosome, Chromosome> {
        val (parent1, parent2) = parents

        val child1 = Chromosome(parent1.weightLimit)
        val child2 = Chromosome(parent2.weightLimit)

        val cutPoint = (0 until minOf(parent1.size, parent2.size)).random()

        val firstHalfOfFirstParent = parent1.take(cutPoint)
        val secondHalfOfFirstParent  = parent1.take(parent1.size - cutPoint)

        val firstHalfOfSecondParent = parent2.drop(cutPoint)
        val secondHalfOfSecondParent = parent2.drop(parent2.size - cutPoint)

        firstHalfOfFirstParent.forEach { child1.tryToAdd(it) }
        secondHalfOfSecondParent.forEach { child1.tryToAdd(it) }
        parent1.forEach { child1.tryToAdd(it) }

        firstHalfOfSecondParent.forEach { child2.tryToAdd(it) }
        secondHalfOfFirstParent.forEach { child2.tryToAdd(it) }
        parent2.forEach { child2.tryToAdd(it) }

        return child1 to child2
    }

}
