package algorithm.genetic

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

        if(weight > weightLimit) {
            println("################# WeightLimit crossed ##############")
        }
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

    fun tryReplaceAt(idx: Int, gene: Gene) {
        if(gene !in genesHash && geneFits(gene)) {
            val toRemove = genes[idx]
            value -= toRemove.value
            weight -=  toRemove.weight
            genesHash -= toRemove

            genesHash += gene
            weight += gene.weight
            value+= gene.value
            genes[idx] = gene
        }
    }

    private fun geneFits(gene: Gene): Boolean = weight + gene.weight <= weightLimit

    override fun toString(): String {
        return "(weight=$weight, value=$value)"
    }
}
