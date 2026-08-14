package dev.gaphunter.envdiffcompanion.report

import dev.gaphunter.envdiffcompanion.diff.EnvDiffer
import junit.framework.TestCase

class DiffReportWriterTest : TestCase() {

    fun testReportMentionsAllMissingKeysAndFileNames() {
        val result = EnvDiffer.diff(".env", "A=1\nB=2\n", ".env.example", "A=\n")
        val report = DiffReportWriter.render(result)

        assertTrue(report.contains(".env"))
        assertTrue(report.contains(".env.example"))
        assertTrue(report.contains("`B`"))
        assertTrue(report.contains("`A`"))
    }

    fun testReportSaysNoneWhenASideHasNothingMissing() {
        val result = EnvDiffer.diff(".env", "A=1\n", ".env.example", "A=\n")
        val report = DiffReportWriter.render(result)

        assertTrue(report.contains("_None._"))
    }
}
