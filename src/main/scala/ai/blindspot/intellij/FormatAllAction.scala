package ai.blindspot.intellij

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.psi.search.FilenameIndex

import scala.collection.JavaConverters._

class FormatAllAction extends AnAction {
  override def actionPerformed(event: AnActionEvent) {
    val project = event.getProject

    val maybePreferences = Preferences.fromProject(project.getBasePath)
    val filesToFormat = FilenameIndex.getAllFilesByExt(project, "scala").asScala

    maybePreferences.foreach(Formatter.formatFiles(filesToFormat, _))
  }
}
