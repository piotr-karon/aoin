fun requirePositiveInt(string: String, fieldName: String, lineNumber: Int? = null): Int {
    val value = string.toIntOrNull()
    require(value != null) { "$fieldName (value=$string) must be an Int. ${lineNumber?.let { "Line $it" }}" }

    return requirePositiveInt(value, fieldName, lineNumber)
}

fun requirePositiveInt(value: Int, fieldName: String, lineNumber: Int? = null): Int {
    require(value > 0) { "$fieldName (value=$value) must be greater than 0. ${lineNumber?.let { "Line $it" }}" }
    return value
}
