package dev.gaphunter.envdiffcompanion.diff

import junit.framework.TestCase

class EnvDifferTest : TestCase() {

    fun testFindsKeysMissingFromEachSide() {
        val result = EnvDiffer.diff(
            ".env", "API_KEY=x\nDB_HOST=y\nSECRET=z\n",
            ".env.example", "API_KEY=\nDB_HOST=\n",
        )

        assertEquals(listOf("SECRET"), result.onlyInA)
        assertEquals(emptyList<String>(), result.onlyInB)
        assertEquals(listOf("API_KEY", "DB_HOST"), result.common)
        assertTrue(result.hasDifferences)
    }

    fun testNoDifferencesWhenBothSidesHaveTheSameKeys() {
        val result = EnvDiffer.diff(".env", "A=1\nB=2\n", ".env.example", "A=\nB=\n")

        assertFalse(result.hasDifferences)
        assertEquals(emptyList<String>(), result.onlyInA)
        assertEquals(emptyList<String>(), result.onlyInB)
    }

    fun testDifferingValuesForTheSameKeyAreNotFlaggedAsMissing() {
        // Different values per environment (dev vs prod DB URL) are
        // usually intentional -- only presence/absence is a real signal.
        val result = EnvDiffer.diff(".env.development", "DB_URL=dev-host\n", ".env.production", "DB_URL=prod-host\n")

        assertFalse(result.hasDifferences)
        assertEquals(listOf("DB_URL"), result.common)
    }

    fun testResultsAreSortedForStableOutput() {
        val result = EnvDiffer.diff(".env", "Z=1\nA=1\n", ".env.example", "")

        assertEquals(listOf("A", "Z"), result.onlyInA)
    }
}
