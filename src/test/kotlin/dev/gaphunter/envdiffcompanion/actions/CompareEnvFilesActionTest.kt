package dev.gaphunter.envdiffcompanion.actions

import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.impl.SimpleDataContext
import com.intellij.testFramework.TestActionEvent
import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Real `update()` path through a constructed
 * [com.intellij.openapi.actionSystem.DataContext] carrying
 * [CommonDataKeys.VIRTUAL_FILE_ARRAY] -- the SAME key Bean Copy
 * Companion had to switch to after `LangDataKeys.PSI_ELEMENT_ARRAY`
 * turned out to never be populated by the real Project View
 * (`SDK_GOTCHAS.md` §17). Written here from the start instead of
 * discovering the same bug live a second time.
 */
class CompareEnvFilesActionTest : BasePlatformTestCase() {

    fun testActionIsEnabledWithExactlyTwoFilesSelected() {
        val fileA = myFixture.addFileToProject(".env", "A=1").virtualFile
        val fileB = myFixture.addFileToProject(".env.example", "A=").virtualFile

        val dataContext = SimpleDataContext.builder()
            .add(CommonDataKeys.PROJECT, project)
            .add(CommonDataKeys.VIRTUAL_FILE_ARRAY, arrayOf(fileA, fileB))
            .build()
        val action = CompareEnvFilesAction()
        val event = TestActionEvent.createTestEvent(action, dataContext)
        action.update(event)

        assertTrue(event.presentation.isEnabledAndVisible)
    }

    fun testActionIsDisabledWithOnlyOneFileSelected() {
        val fileA = myFixture.addFileToProject(".env", "A=1").virtualFile

        val dataContext = SimpleDataContext.builder()
            .add(CommonDataKeys.PROJECT, project)
            .add(CommonDataKeys.VIRTUAL_FILE_ARRAY, arrayOf(fileA))
            .build()
        val action = CompareEnvFilesAction()
        val event = TestActionEvent.createTestEvent(action, dataContext)
        action.update(event)

        assertFalse(event.presentation.isEnabledAndVisible)
    }

    fun testActionIsDisabledWithNoSelectionInTheDataContext() {
        val dataContext = SimpleDataContext.builder().add(CommonDataKeys.PROJECT, project).build()
        val action = CompareEnvFilesAction()
        val event = TestActionEvent.createTestEvent(action, dataContext)
        action.update(event)

        assertFalse(event.presentation.isEnabledAndVisible)
    }
}
