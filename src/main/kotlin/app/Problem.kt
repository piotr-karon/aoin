package app

data class Problem(
    val weightLimit: Int,
    val items: Set<Item>,
    val fileName: String,
    val expectedOptimum: Int? = null
)
