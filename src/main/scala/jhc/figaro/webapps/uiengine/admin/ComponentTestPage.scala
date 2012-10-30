package jhc.figaro.webapps.uiengine.admin
import jhc.figaro.webapps.uiengine.HeaderComponent
import jhc.figaro.webapps.uiengine.traits.UIWithJavascriptDependencies
import org.apache.wicket.Component
import org.apache.wicket.Page
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.MarkupStream
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.request.resource.PackageResourceReference
import scripts.Scripts

class ComponentTestPage(dependencies:Array[String],html:() => String,data:() => String) 
  extends WebPage 
  with UIWithJavascriptDependencies {

  val target = new HeaderComponent("htmlTarget",data,
				   listJavascriptDependencies ) {
    override def onComponentTagBody(markupStream: MarkupStream, openTag: ComponentTag) {
      replaceComponentTagBody(markupStream, openTag, html())
    }

  }

  add(target)

  override def listJavascriptDependencies:Array[String] = {
    //angular classpathJs("core/angularutils.js") +: dependencies
    dependencies
  } 

}
