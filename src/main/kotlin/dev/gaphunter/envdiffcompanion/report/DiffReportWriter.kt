package dev.gaphunter.envdiffcompanion.report

import dev.gaphunter.envdiffcompanion.model.EnvDiffResult

/** Pure string rendering, no I/O -- the caller decides where the report goes. */
object DiffReportWriter {

    fun render(result: EnvDiffResult): String = buildString {
        appendLine("# Env Diff Report")
        appendLine()
        appendLine("Comparing `${result.fileAName}` and `${result.fileBName}`.")
        appendLine()
        appendLine("## Missing in `${result.fileBName}` (present in `${result.fileAName}`)")
        appendLine()
        renderKeyList(this, result.onlyInA)
        appendLine()
        appendLine("## Missing in `${result.fileAName}` (present in `${result.fileBName}`)")
        appendLine()
        renderKeyList(this, result.onlyInB)
        appendLine()
        appendLine("## Present in both (${result.common.size})")
        appendLine()
        renderKeyList(this, result.common)
    }

    private fun renderKeyList(sb: StringBuilder, keys: List<String>) {
        if (keys.isEmpty()) {
            sb.appendLine("_None._")
        } else {
            keys.forEach { sb.appendLine("- `$it`") }
        }
    }
}
