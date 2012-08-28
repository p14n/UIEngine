package jhc.figaro.webapps.uiengine

import org.apache.wicket.MarkupContainer
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.MarkupStream
import org.apache.wicket.Component
import org.apache.wicket.markup.html.IHeaderResponse
import org.apache.wicket.behavior.AbstractBehavior

class HeaderComponent(
  id: String,
  dynamicJavascript: => String = { "" },
  scripts: => Array[String] = { Array() }
) extends MarkupContainer(id) {

  add(new AbstractBehavior() {
    override def renderHead(component: Component, response: IHeaderResponse) {

      response renderJavaScript(dynamicJavascript, "js_"+id)
      scripts foreach( script =>
	response renderJavaScriptReference(script)
      )
    }
  })

}
