data class Item(
    val name: String,
    val weight: Int,
    val value: Int,
    val number: Int
) {
    init {
        require(name.isNotBlank()) { "Name must not be blank. Item number: $number" }
        requirePositiveInt(value, "Value", number)
        requirePositiveInt(weight, "Weight", number)
    }
}
