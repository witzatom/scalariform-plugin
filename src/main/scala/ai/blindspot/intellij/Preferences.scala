package ai.blindspot.intellij

import java.io.{File, FileInputStream}
import java.util.Properties
import scalariform.formatter.preferences.{
  PreferencesImporterExporter,
  IFormattingPreferences
}, PreferencesImporterExporter.getPreferences

object Preferences {
  def fromProject(projectHome: String): Option[IFormattingPreferences] =
    getPrefs(projectHome).flatMap { file =>
      val (input, properties) = (
        new FileInputStream(file), new Properties
      )
      properties.load(input)
      input.close()

      val maybePrefs =
        properties.size match {
          case 0 => None
          case _ => Some(
            getPreferences(properties)
          )
        }
      maybePrefs
    }

  private def getPrefs(path: String): Option[File] = {
    val s = path :: ".scalariform.conf" :: Nil
    Option(new File(s.mkString(File.separator))).filter(_.isFile)
  }
}
