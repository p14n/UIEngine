package jhc.figaro.webapps.uiengine

import org.apache.wicket.markup.WicketTag
import org.apache.wicket.markup.IMarkupResourceStreamProvider
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.request.component.IRequestablePage
import org.apache.wicket.util.resource.IResourceStream
import org.apache.wicket.util.resource.StringResourceStream
import org.apache.wicket.MarkupContainer
import org.apache.wicket.markup.MarkupStream
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.Component

class DynamicHtmlPage(sourceProvider: => String,
		    headComponentCreator: (String,String) => Component)
 extends WebPage with IMarkupResourceStreamProvider with IRequestablePage {

  override def onInitialize() {

    super.onInitialize()

    val markup = getMarkup()

    if (markup != null && markup.size() > 1) {

      val stream = new MarkupStream(markup)

      if (stream.skipUntil(classOf[ComponentTag]))
        stream.next()

      findAndAddHeaderComponents(stream)

    }

  }
  def findAndAddHeaderComponents(stream: MarkupStream) {

    while (stream.skipUntil(classOf[ComponentTag])) {

      val tag = stream.getTag()
      if (tag.isOpen()) {

        if (tag.isInstanceOf[WicketTag]) {

          val uitype = tag.getAttribute("type")
          if (tag.getId() == null) {
            tag.setId("ui_" + uitype + "_" + getAutoIndex());
	    tag.setModified(true)
          }

          val headComponent = headComponentCreator(uitype, tag.getId())
	  
	  if(headComponent!=null)
	    add(headComponent)
	  
	  stream.skipToMatchingCloseTag(tag)
        }
      }
      stream.next()
    }
  }

  override def getMarkupResourceStream(con: MarkupContainer, cl: Class[_]): IResourceStream = {
    new StringResourceStream(sourceProvider)
  }

}
