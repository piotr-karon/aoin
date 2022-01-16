package app.tuning

data class InputParams(
    val numberOfGenerations: Int,
    val genesisPopulationSize: Int,
    val tournamentSize: Int,
    val mutationRate: Double
) {
    fun asCsvLine() =
        "$numberOfGenerations,$genesisPopulationSize,$tournamentSize,$mutationRate"
}
