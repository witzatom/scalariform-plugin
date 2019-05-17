package ai.blindspot.intellij

import java.awt.GridLayout

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.{DumbService, Project}
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.changes.ChangeListManager
import com.intellij.openapi.vcs.checkin.{CheckinHandler, CheckinMetaHandler}
import com.intellij.openapi.vcs.ui.RefreshableOnComponent
import com.intellij.ui.NonFocusableCheckBox
import javax.swing.{JCheckBox, JComponent, JPanel}

import scala.collection.JavaConverters._

class ScalariformReformatCheckingHandler(project: Project, projectPanel: CheckinProjectPanel)
  extends CheckinHandler with CheckinMetaHandler {
  val REFORMAT_BEFORE_PROJECT_COMMIT = "scalariform_formatter_reformat"

  override def getBeforeCheckinConfigurationPanel: RefreshableOnComponent = {
    val reformatBox = new NonFocusableCheckBox("Reformat with scalariform")
    disableWhenDumb(project, reformatBox, "Impossible until indices are up-to-date")
    new RefreshableOnComponent() {
      override def getComponent: JComponent = {
        val panel = new JPanel(new GridLayout(1, 0))
        panel.add(reformatBox)
        panel
      }

      override def refresh(): Unit = {
      }

      override def saveState(): Unit = {
        getSettings.setValue(REFORMAT_BEFORE_PROJECT_COMMIT, reformatBox.isSelected)
      }

      override def restoreState(): Unit = {
        reformatBox.setSelected(getSettings.getBoolean(REFORMAT_BEFORE_PROJECT_COMMIT))
      }
    }
  }

  override def runCheckinHandlers(finishAction: _root_.java.lang.Runnable): Unit = {
    val shouldReformatBeforeCheckin = getSettings.getBoolean(REFORMAT_BEFORE_PROJECT_COMMIT)

    if (shouldReformatBeforeCheckin && !DumbService.isDumb(project)) {
      val maybePreferences = Preferences.fromProject(project.getBasePath)
      val filesToFormat = ChangeListManager.getInstance(project)
        .getAffectedFiles
        .asScala

      maybePreferences.foreach(Formatter.formatFiles(filesToFormat, _))
      finishAction.run()
    } else new Runnable {
      def run(): Unit = {
        FileDocumentManager.getInstance.saveAllDocuments()
        finishAction.run()
      }
    }.run()
  }

  protected def getSettings: PropertiesComponent = PropertiesComponent.getInstance(project)

  private def disableWhenDumb(project: Project, checkBox: JCheckBox, tooltip: String): Unit = {
    val dumb = DumbService.isDumb(project)
    checkBox.setEnabled(!dumb)
    checkBox.setToolTipText(if (dumb) tooltip
    else "")
  }
}

