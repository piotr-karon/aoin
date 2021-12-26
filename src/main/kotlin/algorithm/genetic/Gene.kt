package algorithm.genetic

import java.util.*

/** A single item */
data class Gene(val name: String, val weight: Int, val value: Int)

/** This is a Backpack */
class Chromosome(val weightLimit: Int): Iterable<Gene> {

    /** Items in backpack */
    private val genes = mutableListOf<Gene>()

    /** Current number of items in backpack */
    val size get() = genes.size

    /** Value of items inside backpack, also fitness score */
    val value: Int get() = genes.sumOf { it.value }

    /** Weight of items inside backpack */
    val weight: Int get() = genes.sumOf { it.weight }

    val fitnessScore: Int get() = value

    override fun iterator(): Iterator<Gene> = genes.iterator()

    operator fun plusAssign(gene: Gene) {
        if(gene !in this && geneFits(gene)){
            genes += gene
        }
    }

    fun copy(): Chromosome {
        val copy = Chromosome(weightLimit)

        // Do we need to copy? Since genes are immutable anyway?
        copy.genes.addAll(genes.map { it.copy() })

        return copy
    }

    operator fun get(itemFromParent: Int): Gene = genes[itemFromParent]

    fun tryReplaceAt(itemToReplaceInChild: Int, gene: Gene) {
        if(gene !in this) {
            if(geneFits(gene)){
                genes[itemToReplaceInChild] = gene
            }
        }
    }

    private fun geneFits(gene: Gene): Boolean = weight + gene.weight <= weightLimit

    override fun toString(): String {
        return "(weight=$weight, value=$value)"
    }
}

class Population: Iterable<Chromosome> {

    val randomFittest: Chromosome
    get()  {
        val randomIndex = (size/2 until size).random()
//        var chromosome: Chromosome? = null
//        forEachIndexed { index, chromo -> if(index == randomIndex) chromosome = chromo }

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
