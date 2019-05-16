package ai.blindspot.intellij

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile
import scalariform.formatter.ScalaFormatter
import scalariform.formatter.preferences.IFormattingPreferences

object Formatter {
  def formatFiles(files: Iterable[VirtualFile], preferences: IFormattingPreferences): Unit = {
    val docManager = FileDocumentManager.getInstance()

    files.filter(_.getExtension.toLowerCase == "scala")
      .foreach { file =>
        Option(docManager.getDocument(file)).foreach{ document =>
          val source = document.getText()
          val formatted = ScalaFormatter.format(source, formattingPreferences = preferences)
          if (source != formatted) {
            ApplicationManager.getApplication.runWriteAction(new Runnable {
              override def run(): Unit = {
                document.setText(formatted)
              }
            })
          }
        }
      }
  }
}
