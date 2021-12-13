object Utils {
    fun <R> measureTimeMillisWithResult(block: () -> R): Pair<Long, R> {
        val start = System.currentTimeMillis()
        val result = block()
        return Pair(System.currentTimeMillis() - start, result)
    }
}