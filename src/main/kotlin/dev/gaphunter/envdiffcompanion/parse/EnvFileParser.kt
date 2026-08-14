package dev.gaphunter.envdiffcompanion.parse

/**
 * Plain-text `KEY=VALUE` parsing, no PSI, no assumptions about which
 * plugin (if any) provides `.env` language support. A blank line or a
 * line starting with `#` (after trimming) is a comment/separator, not
 * a key. `export KEY=VALUE` (a real, common shell-sourced `.env`
 * convention) is also recognized -- the leading `export ` is stripped
 * before the key is read, so it doesn't get counted as part of the
 * key text itself.
 */
object EnvFileParser {

    fun parseKeys(text: String): List<String> =
        text.lineSequence()
            .map { it.trim() }
            .filter { it.isNotEmpty() && !it.startsWith("#") }
            .map { it.removePrefix("export ").trim() }
            .mapNotNull { line -> line.substringBefore('=').trim().takeIf { it.isNotEmpty() } }
            .distinct()
            .toList()
}
