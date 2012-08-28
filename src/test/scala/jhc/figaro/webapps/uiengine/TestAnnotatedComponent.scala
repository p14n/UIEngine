package jhc.figaro.webapps.uiengine;
import jhc.figaro.webapps.uiengine.traits.UIComponentWithJavascriptDependencies

class TestAnnotatedComponent extends UIComponent with UIComponentWithJavascriptDependencies {

  @UIDynamicJavaScriptMethod 
  def createDynamicJavascript(id: String): String = {
    "dynamicjavascript"
  }
  def listJavascriptDependencies(): Array[String] = {
    Array("script1.js","script2.js")
  }

} 
