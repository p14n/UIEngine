package jhc.figaro.webapps.uiengine
import java.lang.annotation.Annotation
import java.lang.reflect.Method

import org.apache.wicket.Component
import org.reflections.Reflections
import scala.Function
import scala.collection.JavaConversions.asSet

object UIComponentCreator {

  val annotationClass = classOf[UIComponent];

  def findComponentObjectsOnClasspath(packagePrefix: String):Map[String,Class[_]] = {

    val reflections = new Reflections(packagePrefix);
    val annotated = asSet(reflections.getTypesAnnotatedWith(annotationClass));
    //Now turn this set of classes into a map of annotation property 'name' and the class
    annotated.groupBy( 
      _.getAnnotation(annotationClass).asInstanceOf[UIComponent].name())
	.mapValues(_.head)

  }
  def findCreatorMethodOnClass(uiClass: Class[_]): String => Component = {
    val method = findFirstMethodWithAnnotation(uiClass,classOf[UIComponentCreationMethod])
    if(method != null){
      val params = method.getParameterTypes();
      if (!(params != null && params.length==1 && params(0) == classOf[String]
	  && method.getReturnType() == classOf[Component]))
	  throw new IllegalArgumentException(method.getName()+
		" does not have a String as the sole argument and returns a Component");
      return method.asInstanceOf[String => Component];
    }
    a => null
  }
  def findFirstMethodWithAnnotation(uiClass: Class[_],
				   annotationClass: Class[_ <: Annotation]): Method = {
    val methods = uiClass.getDeclaredMethods();
    for(val method <- methods) {
      if(method.getAnnotation(annotationClass) != null){
	return method
      }
    }
    null

  }
  def findDynamicJavascriptMethodOnClass(uiClass: Class[_]): String => String = {
    val method = findFirstMethodWithAnnotation(uiClass,classOf[UIDynamicJavaScriptMethod])
    if(method != null){
      val params = method.getParameterTypes();
      if (!(params != null && params.length==1 && params(0) == classOf[String]
	  && method.getReturnType() == classOf[String]))
	  throw new IllegalArgumentException(method.getName()+
		" does not have a String as the sole argument and returns a String");
      return method.asInstanceOf[String => String]
    }
    null
  }
}
class UIComponentCreator(availableComponents: Map[String,Class[_]]) {
  def getDefinition(uiType: String):Class[_] = {
    if (!availableComponents.contains(uiType))
      throw new IllegalArgumentException(uiType+" is not known as a component name");
    availableComponents(uiType)
  }
  def createComponent(uiType:String, id:String): Component = {
    val definition = getDefinition(uiType)
    UIComponentCreator.findCreatorMethodOnClass(definition)(id)
  }
  def createDynamicJavascript(uiType: String, id: String): String = {
    val definition = getDefinition(uiType)
    val method = UIComponentCreator.findDynamicJavascriptMethodOnClass(definition)
    if (method == null) return null
    method(id)
  }
}

