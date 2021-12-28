package algorithm.genetic.crossover

import algorithm.genetic.Chromosome

object ScoreBasedCrossover : Crossover {

    override fun crossover(parents: Pair<Chromosome, Chromosome>): Chromosome {
        val (parent1, parent2) = parents

        val (child, parent) =
            if (parent1.fitnessScore > parent2.fitnessScore) parent1.copy() to parent2
            else parent2.copy() to parent1

        repeat(child.size / 2) {
            val itemFromParent = (0 until parent.size).random()
            val itemToReplaceInChild = (0 until child.size).random()

            child.tryReplaceAt(itemToReplaceInChild, parent[itemFromParent])
        }

        return child
    }

}
