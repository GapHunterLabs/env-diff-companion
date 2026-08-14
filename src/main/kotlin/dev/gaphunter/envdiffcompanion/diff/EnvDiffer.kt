package dev.gaphunter.envdiffcompanion.diff

import dev.gaphunter.envdiffcompanion.model.EnvDiffResult
import dev.gaphunter.envdiffcompanion.parse.EnvFileParser

object EnvDiffer {

    fun diff(fileAName: String, textA: String, fileBName: String, textB: String): EnvDiffResult {
        val keysA = EnvFileParser.parseKeys(textA).toSet()
        val keysB = EnvFileParser.parseKeys(textB).toSet()
        return EnvDiffResult(
            fileAName = fileAName,
            fileBName = fileBName,
            onlyInA = (keysA - keysB).sorted(),
            onlyInB = (keysB - keysA).sorted(),
            common = (keysA intersect keysB).sorted(),
        )
    }
}
