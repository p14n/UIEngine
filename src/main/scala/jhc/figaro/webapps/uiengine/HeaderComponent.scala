package jhc.figaro.webapps.uiengine

import org.apache.wicket.MarkupContainer
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.MarkupStream
import org.apache.wicket.Component
import org.apache.wicket.markup.html.IHeaderResponse
import org.apache.wicket.behavior.Behavior

class HeaderComponent(
  id: String,
  dynamicJavascript:() => String = () => { null },
  scripts: => Array[String] = { Array() }
) extends MarkupContainer(id) {

  add(new Behavior() {
    override def renderHead(component: Component, response: IHeaderResponse) {
      if(dynamicJavascript != null) {
	val str = dynamicJavascript()
	if(str!=null) response renderJavaScript(str, "js_"+id)
      }
      if(scripts!=null){
	scripts foreach( script =>
	  response renderJavaScriptReference(script)
	)
      }
    }
  })

}
