package algorithm.genetic

import java.util.*

class Population: Iterable<Chromosome> {

    val randomFittest: Chromosome
    get()  {
        val randomIndex = (size/2 until size).random()
        return asSequence()
            .filterIndexed { index, _ -> index == randomIndex }
            .first()
    }

    // A set of chromosomes – backpacks. Sorted by its value ascending. The last entry is the bes.
    private val chromosomes = TreeMap<Int, MutableList<Chromosome>>()

    var size: Int = 0
    private set

    val fittest: Chromosome
        get() = chromosomes.lastEntry().value[0]

    operator fun plusAssign(chromosome: Chromosome) {
        chromosomes.merge(chromosome.fitnessScore, mutableListOf(chromosome)) { k,v ->
            (k + v).toMutableList()
        }
        size++
    }

    override fun iterator(): Iterator<Chromosome> = PopulationIterator()

    private inner class PopulationIterator: Iterator<Chromosome> {

        private var bucketIterator: Iterator<Chromosome>? = null
        private val mapIterator = chromosomes.iterator()

        override fun hasNext() = bucketHasNextElement || mapIterator.hasNext()

        override fun next(): Chromosome {
            if (bucketHasNextElement) return bucketIterator!!.next()
            val (_, bucket) = mapIterator.next()
            bucketIterator = bucket.iterator()
            return bucketIterator!!.next()
        }

        private val bucketHasNextElement
            get() = (bucketIterator != null && bucketIterator!!.hasNext())
    }
}
