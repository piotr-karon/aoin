package algorithm.genetic

import app.gen

/** This is a Backpack */
class Chromosome(val weightLimit: Int): Iterable<Gene> {

    /** Items in backpack */
    private val genes = arrayListOf<Gene>()
    private val genesHash = hashSetOf<Gene>()

    private fun addGene(gene: Gene) {
        genes.add(gene)
        genesHash.add(gene)
        value += gene.value
        weight += gene.weight
    }

    private fun removeGene(gene: Gene) {
        genes -= gene
        genesHash.remove(gene)
        value -= gene.value
        weight -= gene.weight
    }

    /** Current number of items in backpack */
    val size get() = genes.size

    /** Value of items inside backpack, also fitness score */
    var value: Int = 0
    private set
    /** Weight of items inside backpack */
    var weight: Int = 0
    private set

    val fitnessScore: Int get() = value

    override fun iterator(): Iterator<Gene> = genes.iterator()

    fun tryToAdd(gene: Gene): Boolean {
        if(gene !in genesHash && geneFits(gene)){
            addGene(gene)
            return true
        }

        return false
    }

    fun copy(): Chromosome {
        val copy = Chromosome(weightLimit)
        genes.forEach { copy.addGene(it) }
        return copy
    }

    fun replaceRandom(gene: Gene) {
        if(gene !in genesHash) {
            val toRemove = genes.random()
            removeGene(toRemove)

            if(geneFits(gene)){
                addGene(gene)
            } else {
                addGene(toRemove)
            }
        }
    }

    private fun geneFits(gene: Gene): Boolean = weight + gene.weight <= weightLimit

    override fun toString(): String {
        return "(weight=$weight, value=$value)"
    }
}
