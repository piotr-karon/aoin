package app

import requirePositiveInt

class Item(
    val name: String,
    val weight: Int,
    val value: Int,
) {
    init {
        requirePositiveInt(value, "Value")
        requirePositiveInt(weight, "Weight")
    }
}
