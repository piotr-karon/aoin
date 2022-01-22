package algorithm.genetic

/** This is a Backpack */
class Chromosome(val weightLimit: Int): Iterable<Gene> {

    /** Items in backpack */
    private val genes = arrayListOf<Gene>()
    private val genesHash = hashMapOf<String, Gene?>()

    val genesList: List<Gene> get() = genes

    private fun addGene(gene: Gene) {
        genes.add(gene)
        genesHash[gene.name] = gene
        value += gene.value
        weight += gene.weight

        if(weight > weightLimit) {
            println("################# WeightLimit crossed ##############")
        }
    }

    private fun isAlreadyAdded(gene: Gene): Boolean {
        return genesHash[gene.name] != null
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
        if(!isAlreadyAdded(gene) && geneFits(gene)){
            addGene(gene)
            return true
        }

        return false
    }

    fun tryReplaceAt(idx: Int, gene: Gene) {
        if(!isAlreadyAdded(gene) && geneFits(gene)) {
            val toRemove = genes[idx]
            value -= toRemove.value
            weight -=  toRemove.weight
            genesHash[toRemove.name] = null

            genesHash[gene.name] = gene
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
