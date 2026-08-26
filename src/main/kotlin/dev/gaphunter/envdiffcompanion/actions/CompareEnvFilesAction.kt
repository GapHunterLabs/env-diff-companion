package dev.gaphunter.envdiffcompanion.actions

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import dev.gaphunter.envdiffcompanion.diff.EnvDiffer
import dev.gaphunter.envdiffcompanion.report.DiffReportWriter
import dev.gaphunter.envdiffcompanion.review.ReviewPrompt

/**
 * Project-view context-menu entry point. Requires exactly two files
 * selected -- uses [CommonDataKeys.VIRTUAL_FILE_ARRAY], the same
 * fundamental, universally-populated key Bean Copy Companion switched
 * to after `LangDataKeys.PSI_ELEMENT_ARRAY` turned out to never be
 * populated by the real Project View -- never the unverified key again.
 */
class CompareEnvFilesAction : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        val project = e.project
        val files = resolveTwoFiles(e)
        e.presentation.isEnabledAndVisible = project != null && !DumbService.isDumb(project) && files != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val (fileA, fileB) = resolveTwoFiles(e) ?: return

        ApplicationManager.getApplication().executeOnPooledThread {
            val textA = String(fileA.contentsToByteArray(), fileA.charset)
            val textB = String(fileB.contentsToByteArray(), fileB.charset)
            val result = EnvDiffer.diff(fileA.name, textA, fileB.name, textB)
            val report = DiffReportWriter.render(result)

            ApplicationManager.getApplication().invokeLater {
                writeReport(project, fileA, report)
                // Every completed comparison is a real use -- no "no-op"
                // branch here (both "differences found" and "same keys"
                // are a real, useful answer to the question the user asked).
                ReviewPrompt.recordHit(project)
                val message = if (result.hasDifferences) {
                    "${result.onlyInA.size} key(s) missing from ${fileB.name}, ${result.onlyInB.size} missing from ${fileA.name} -- see env-diff-report.md."
                } else {
                    "${fileA.name} and ${fileB.name} have the same keys."
                }
                notify(project, message, if (result.hasDifferences) NotificationType.WARNING else NotificationType.INFORMATION)
            }
        }
    }

    /**
     * Re-running the comparison overwrites its own previous report
     * (same file name, same directory) rather than piling up
     * `env-diff-report (1).md`, `(2).md`, etc. -- this report is a
     * disposable, regenerate-anytime artifact, not something meant to
     * accumulate history.
     */
    private fun writeReport(project: Project, anchorFile: VirtualFile, reportText: String) {
        val directory = PsiManager.getInstance(project).findDirectory(anchorFile.parent ?: return) ?: return
        val fileName = "env-diff-report.md"

        WriteCommandAction.runWriteCommandAction(project, "Compare Env Files", null, {
            val existing = directory.findFile(fileName)
            val resultFile = if (existing != null) {
                PsiDocumentManager.getInstance(project).getDocument(existing)?.setText(reportText)
                existing
            } else {
                val newFile = PsiFileFactory.getInstance(project)
                    .createFileFromText(fileName, PlainTextFileType.INSTANCE.language, reportText)
                directory.add(newFile) as PsiFile
            }
            resultFile.virtualFile?.let { FileEditorManager.getInstance(project).openFile(it, true) }
        })
    }

    private fun resolveTwoFiles(e: AnActionEvent): Pair<VirtualFile, VirtualFile>? {
        val files = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY) ?: return null
        if (files.size != 2) return null
        if (files.any { it.isDirectory }) return null
        return files[0] to files[1]
    }

    private fun notify(project: Project, message: String, type: NotificationType) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("Env Diff Companion")
            .createNotification(message, type)
            .notify(project)
    }
}
