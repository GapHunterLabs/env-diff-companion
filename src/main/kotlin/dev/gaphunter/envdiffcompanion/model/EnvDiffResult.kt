package dev.gaphunter.envdiffcompanion.model

data class EnvDiffResult(
    val fileAName: String,
    val fileBName: String,
    /** Keys present in A but missing from B, sorted. */
    val onlyInA: List<String>,
    /** Keys present in B but missing from A, sorted. */
    val onlyInB: List<String>,
    /** Keys present in both, sorted. */
    val common: List<String>,
) {
    val hasDifferences: Boolean get() = onlyInA.isNotEmpty() || onlyInB.isNotEmpty()
}
