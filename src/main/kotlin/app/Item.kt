package app

import requirePositiveInt
import requirePositiveOrZeroInt

data class Item(
    val name: String,
    val weight: Int,
    val value: Int,
) {
    init {
        require(name.isNotBlank()) { "Name must not be blank. app. " }
        requirePositiveOrZeroInt(value, "Value")
        requirePositiveOrZeroInt(weight, "Weight")
    }
}
