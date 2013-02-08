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
import jhc.figaro.webapps.uiengine.admin.ComponentProperty
import jhc.figaro.webapps.uiengine.UIComponentCreator

trait UIWithPageComponent extends Serializable {
  def createComponent(
    id: String,
    propertyApplier: (() => String,List[ComponentProperty]) => String ): Component

  def htmlComponent(id: String, html: String): Component = {
    new DynamicHtmlComponent(id, html)
  }
  def replacePropertiesInText(text:String,props:List[ComponentProperty]):String = {
      var map = UIComponentCreator.addMissingProperties(null,props)
      UIComponentCreator.applyProperties(text,map)
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
