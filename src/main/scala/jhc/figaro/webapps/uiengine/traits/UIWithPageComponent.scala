package jhc.figaro.webapps.uiengine.traits
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import jhc.figaro.webapps.uiengine.DynamicHtmlComponent
import jhc.figaro.webapps.uiengine.FileFunctions
import jhc.figaro.webapps.uiengine.PropertyStore
import org.apache.wicket.Component
import scala.io.Source
import FileFunctions._

trait UIWithPageComponent extends Serializable {
  def createComponent(id: String): Component

  def htmlComponent(id: String, html: String): Component = {
    new DynamicHtmlComponent(id, html)
  }
  def htmlFromFile(fileName: String): String = {
    try {
      val is = streamFromSrcOrClasspath("/html/"+fileName)
      val html = Source.fromInputStream(is).mkString
      is.close()
      html
    } catch {
      case e => { e.printStackTrace(); return "<!-- " + fileName + " not found -->" }
    }
  }


}
