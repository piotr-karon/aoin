package app

import requirePositiveInt

data class Item(
    val name: String,
    val weight: Int,
    val value: Int,
) {
    init {
        require(name.isNotBlank()) { "Name must not be blank. app. " }
        requirePositiveInt(value, "Value")
        requirePositiveInt(weight, "Weight")
    }
}
