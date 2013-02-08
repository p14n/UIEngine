package jhc.figaro.webapps.uiengine
import java.lang.annotation.Annotation
import java.lang.reflect.Method
import jhc.figaro.webapps.uiengine.traits._
import org.apache.wicket.Component
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.html.IHeaderResponse
import org.reflections.Reflections
import scala.Function
import scala.collection.JavaConversions.asScalaSet
import jhc.figaro.webapps.uiengine.admin.ComponentProperty
import scala.collection.mutable.{Map => MMap}

object UIComponentCreator {

  val annotationClass = classOf[UIComponent];

  def findComponentObjectsOnClasspath(packagePrefix: String): Map[String, Serializable] = {

    val reflections = new Reflections(packagePrefix);
    val annotated = asScalaSet(reflections.getTypesAnnotatedWith(annotationClass));
    //Now turn this set of classes into a map of annotation property 'name' and the class
    annotated.groupBy(
      _.getAnnotation(annotationClass).asInstanceOf[UIComponent].name())
      .mapValues(_.head.newInstance().asInstanceOf[Serializable])

  }
  def findCreatorMethodOnClass(uiObject: Object): 
  (String,(() => String,List[ComponentProperty]) => String ) => Component = {
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
  def findPropertiesMethodOnObject(uiObject: Object): List[ComponentProperty] = {
    if (uiObject.isInstanceOf[UIWithProperties])
      return uiObject.asInstanceOf[UIWithProperties].getProperties
    null
  }
  def createPropertyApplier(uiType:String,lang:String,
    propertyLookup:(String,String) => Map[String,String]):
  (() => String,List[ComponentProperty]) => String = {
    
    def f(html:() => String,
      properties:List[ComponentProperty]):String = {
      if(properties == null || properties.isEmpty)
       return html();
      var map = addMissingProperties(
        propertyLookup(uiType,lang),properties)
      applyProperties(html(),map)
    }
    f 
  }
  def addMissingProperties(fromDb:Map[String,String],
    toAdd:List[ComponentProperty]):MMap[String,String] = {
    val map = if(fromDb==null) MMap[String,String]() 
     else MMap[String,String](fromDb.toSeq: _*);
    toAdd.foreach ( prop => {
      if(!map.contains(prop.uikey)){
        map(prop.uikey) = prop.defaultValue
      }
    })
    map
  }
  def applyProperties(text:String,map:MMap[String,String]):String = {
    var tmp = text
      map.keys.foreach(key => {
        tmp = tmp.replace("${"+key+"}", map(key))
        println("Replace ${"+key+"} with "+map(key));
      })
      tmp
  }

}
class UIComponentCreator(availableComponents: Map[String, _],
  propertyLookup: (String,String) => Map[String,String]) {
  def getDefinition(uiType: String): Object = {
    if (!availableComponents.contains(uiType))
      throw new IllegalArgumentException(uiType + " is not known as a component name");
    availableComponents(uiType).asInstanceOf[AnyRef]
  }
  def createComponent(uiType: String, id: String, lang: String): Component = {
    val definition = getDefinition(uiType)
    val method = UIComponentCreator.findCreatorMethodOnClass(definition)
    if (method == null) return null

    method(id,UIComponentCreator.createPropertyApplier(
      uiType,lang,propertyLookup))
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
