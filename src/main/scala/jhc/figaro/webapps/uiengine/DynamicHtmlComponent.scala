package jhc.figaro.webapps.uiengine

import org.apache.wicket.MarkupContainer
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.Markup
import org.apache.wicket.markup.MarkupStream
import org.apache.wicket.markup.MarkupStream
import org.apache.wicket.Component
import org.apache.wicket.markup.html.IHeaderResponse
import org.apache.wicket.behavior.Behavior

class DynamicHtmlComponent(
  id: String,
  html: => String = { "" }) extends MarkupContainer(id) {

  setRenderBodyOnly(true)

  override def onComponentTagBody(markupStream: MarkupStream, openTag: ComponentTag) {
    replaceComponentTagBody(markupStream, openTag, "")
  }

  add(new Behavior() {
    override def afterRender(c: Component) {
      val htmlrender = html;
      c.getResponse().write(addIdToHtml(htmlrender))
    }
  })

  def findIdInsertionPoint(html: String): Int = {
    val startFirstTag = html.indexOf("<")
    val afterTagName = html.indexOf(" ", startFirstTag)
    val endFirstTag = html.indexOf(">")
    if (afterTagName > -1 && afterTagName < endFirstTag) afterTagName else endFirstTag;

  }
  def addIdToHtml(html: String): String = {
    val insertionPoint = findIdInsertionPoint(html)
    if (insertionPoint < 1) return html;
    val htmlWithId = html.substring(0, insertionPoint) +
      " id=\"" + id + "\" " +
      html.substring(insertionPoint);

    htmlWithId
  }
}
