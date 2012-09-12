package jhc.figaro.webapps.uiengine.traits
import org.apache.wicket.request.resource.PackageResourceReference

trait UIWithDynamicJavascript {

  def wrapInDocLoad(script: String): String = {
    "document.onload = function(e){"+script+"};" 
  }

   def createDynamicJavascript(id: String): String
}
