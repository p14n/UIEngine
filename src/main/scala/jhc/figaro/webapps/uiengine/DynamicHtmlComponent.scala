package jhc.figaro.webapps.uiengine

import org.apache.wicket.MarkupContainer
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.MarkupStream
import org.apache.wicket.Component
import org.apache.wicket.markup.html.IHeaderResponse
import org.apache.wicket.behavior.AbstractBehavior

class DynamicHtmlComponent(
  id: String,
  html: => String = {""}) extends MarkupContainer(id) {

  setRenderBodyOnly(true)

  add(new AbstractBehavior(){
    override def onRendered(c:Component){
      c.getResponse().write(html)
    }
  })
}
