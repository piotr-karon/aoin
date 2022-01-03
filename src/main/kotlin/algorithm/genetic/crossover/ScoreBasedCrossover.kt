package algorithm.genetic.crossover

import algorithm.genetic.Chromosome

object ScoreBasedCrossover : Crossover {

    override fun crossover(parents: Pair<Chromosome, Chromosome>): Chromosome {
        val (parent1, parent2) = parents

        val child = Chromosome(parent1.weightLimit)

        val point = (0 until parent1.size).random()

        val fromParent1 = parent1.take(point)

        val toDrop = parent1.size - point
        val fromParent2 = parent2.drop(toDrop)

        fromParent1.forEach {
            child.tryToAdd(it)
        }

        fromParent2.forEach {
            child.tryToAdd(it)
        }

        parent1.forEach {
            child.tryToAdd(it)
        }

        return child
    }

}
