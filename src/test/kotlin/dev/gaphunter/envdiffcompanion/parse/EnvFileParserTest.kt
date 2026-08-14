package dev.gaphunter.envdiffcompanion.parse

import junit.framework.TestCase

class EnvFileParserTest : TestCase() {

    fun testParsesSimpleKeyValueLines() {
        val text = "API_KEY=abc123\nDB_HOST=localhost\n"
        assertEquals(listOf("API_KEY", "DB_HOST"), EnvFileParser.parseKeys(text))
    }

    fun testIgnoresCommentsAndBlankLines() {
        val text = """
            # This is a comment
            API_KEY=abc123

            # Another comment
            DB_HOST=localhost
        """.trimIndent()
        assertEquals(listOf("API_KEY", "DB_HOST"), EnvFileParser.parseKeys(text))
    }

    fun testStripsExportPrefix() {
        val text = "export API_KEY=abc123\n"
        assertEquals(listOf("API_KEY"), EnvFileParser.parseKeys(text))
    }

    fun testHandlesAKeyWithNoValue() {
        val text = "EMPTY_VAR=\n"
        assertEquals(listOf("EMPTY_VAR"), EnvFileParser.parseKeys(text))
    }

    fun testHandlesAValueThatContainsAnEqualsSign() {
        val text = "CONNECTION_STRING=host=localhost;port=5432\n"
        assertEquals(listOf("CONNECTION_STRING"), EnvFileParser.parseKeys(text))
    }

    fun testDeduplicatesARepeatedKey() {
        val text = "API_KEY=first\nAPI_KEY=second\n"
        assertEquals(listOf("API_KEY"), EnvFileParser.parseKeys(text))
    }

    fun testEmptyFileProducesNoKeys() {
        assertEquals(emptyList<String>(), EnvFileParser.parseKeys(""))
    }
}
