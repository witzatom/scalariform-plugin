package ai.blindspot.intellij

import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.changes.CommitContext
import com.intellij.openapi.vcs.checkin._

class ScalariformReformatCheckinFactory extends ReformatCheckinHandlerFactory {
 override def createHandler(panel: CheckinProjectPanel, commitContext: CommitContext) =
   new ScalariformReformatCheckingHandler(panel.getProject, panel)
}
