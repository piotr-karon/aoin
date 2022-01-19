package algorithm.genetic

/** A single item */
class Gene(val name: String, val weight: Int, val value: Int){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Gene) return false

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}
