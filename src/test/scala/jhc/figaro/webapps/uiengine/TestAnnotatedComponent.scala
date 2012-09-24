package jhc.figaro.webapps.uiengine;
import jhc.figaro.webapps.uiengine.traits.UIWithDynamicJavascript
import jhc.figaro.webapps.uiengine.traits.UIWithJavascriptDependencies
import jhc.figaro.webapps.uiengine.traits.UIWithPageComponent
import org.apache.wicket.Component
import org.apache.wicket.markup.html.basic.Label

@UIComponent(name="test_ui",description="test")
class TestAnnotatedComponent extends UIWithJavascriptDependencies 
  with UIWithDynamicJavascript with UIWithPageComponent {

  def createDynamicJavascript(id: String): String = {
    "dynamicjavascript"
  }
  def listJavascriptDependencies: Array[String] = {
    Array("script1.js","script2.js")
  }
  def createComponent(id:String): Component = {
    new Label(id)
  }

} 
