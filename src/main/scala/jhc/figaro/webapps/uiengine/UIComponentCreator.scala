package jhc.figaro.webapps.uiengine
import java.lang.annotation.Annotation
import java.lang.reflect.Method
import jhc.figaro.webapps.uiengine.traits.UIWithDynamicJavascript
import jhc.figaro.webapps.uiengine.traits.UIWithJavascriptDependencies
import jhc.figaro.webapps.uiengine.traits.UIWithPageComponent

import org.apache.wicket.Component
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.html.IHeaderResponse
import org.reflections.Reflections
import scala.Function
import scala.collection.JavaConversions.asScalaSet

object UIComponentCreator {

  val annotationClass = classOf[UIComponent];

  def findComponentObjectsOnClasspath(packagePrefix: String): Map[String, _] = {

    val reflections = new Reflections(packagePrefix);
    val annotated = asScalaSet(reflections.getTypesAnnotatedWith(annotationClass));
    //Now turn this set of classes into a map of annotation property 'name' and the class
    annotated.groupBy(
      _.getAnnotation(annotationClass).asInstanceOf[UIComponent].name())
      .mapValues(_.head.newInstance())

  }
  def findCreatorMethodOnClass(uiObject: Object): String => Component = {
    if (uiObject.isInstanceOf[UIWithPageComponent])
      return uiObject.asInstanceOf[UIWithPageComponent].createComponent
    null
  }

  def findDynamicJavascriptMethodOnObject(uiObject: Object): String => String = {
    if (uiObject.isInstanceOf[UIWithDynamicJavascript])
      return uiObject.asInstanceOf[UIWithDynamicJavascript].createDynamicJavascript
    null
  }
  def findJavascriptDependencyMethodOnObject(uiObject: Object): Array[String] = {
    if (uiObject.isInstanceOf[UIWithJavascriptDependencies])
      return uiObject.asInstanceOf[UIWithJavascriptDependencies].listJavascriptDependencies
    null
  }

}
class UIComponentCreator(availableComponents: Map[String, _]) {
  def getDefinition(uiType: String): Object = {
    if (!availableComponents.contains(uiType))
      throw new IllegalArgumentException(uiType + " is not known as a component name");
    availableComponents(uiType).asInstanceOf[AnyRef]
  }
  def createComponent(uiType: String, id: String): Component = {
    val definition = getDefinition(uiType)
    val method = UIComponentCreator.findCreatorMethodOnClass(definition)
    if (method == null) return null
    method(id)
  }
  def createJavascriptDependencies(uiType: String): Array[String] = {
    val definition = getDefinition(uiType)
    val method = UIComponentCreator.findJavascriptDependencyMethodOnObject(definition)
    if (method == null) return null
    method
  }
  def createDynamicJavascript(uiType: String, id: String): String = {
    val definition = getDefinition(uiType)
    val method = UIComponentCreator.findDynamicJavascriptMethodOnObject(definition)
    if (method == null) return null
    method(id)
  }
  def createHeaderComponent(uiType: String, id: String): Behavior = {
    val deps = createJavascriptDependencies(uiType)
    val script = () => { createDynamicJavascript(uiType, id) }
    new Behavior() {
      override def renderHead(component: Component, response: IHeaderResponse) {
	if(deps!=null){
	  deps foreach( script =>
	    response renderJavaScriptReference(script))
	}
	if(script != null) {
	  val str = script()
	  if(str!=null) response renderJavaScript(str, "js_"+id)
	}
      }
    }
    //new HeaderComponent(id, script, { deps })
  }
}
