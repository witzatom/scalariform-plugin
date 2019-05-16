package ai.blindspot.intellij

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.vcs.changes.ChangeListManager

import scala.collection.JavaConverters._

class FormatChangedAction extends AnAction {
  override def actionPerformed(event: AnActionEvent) {
    val project = event.getProject

    val maybePreferences = Preferences.fromProject(project.getBasePath)
    val filesToFormat = ChangeListManager.getInstance(project)
      .getAffectedFiles
      .asScala

    maybePreferences.foreach(Formatter.formatFiles(filesToFormat, _))
  }
}
