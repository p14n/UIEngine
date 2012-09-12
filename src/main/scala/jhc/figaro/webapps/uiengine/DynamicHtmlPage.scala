package jhc.figaro.webapps.uiengine
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.IMarkupCacheKeyProvider
import org.apache.wicket.markup.IMarkupResourceStreamProvider
import org.apache.wicket.markup.Markup
import org.apache.wicket.markup.MarkupResourceStream

import org.apache.wicket.markup.WicketTag
import org.apache.wicket.markup.IMarkupResourceStreamProvider
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.resolver.IComponentResolver
import org.apache.wicket.request.component.IRequestablePage
import org.apache.wicket.util.resource.IResourceStream
import org.apache.wicket.util.resource.StringResourceStream
import org.apache.wicket.MarkupContainer
import org.apache.wicket.markup.MarkupStream
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.Component
import scala.collection.mutable.Queue

class DynamicHtmlPage(sourceProvider: () => String,
  headComponentCreator: (String, String) => Behavior,
  componentCreator: (String, String) => Component)
  extends WebPage with IMarkupResourceStreamProvider
  with IRequestablePage with IMarkupCacheKeyProvider with IComponentResolver {

  override def isBookmarkable: Boolean = { true }
  var idMap = Map[String, Queue[String]]()

  override def onInitialize() {

    setStatelessHint(true)

    var markup = Markup.of(sourceProvider())

    if (markup != null && markup.size() > 1) {

      val stream = new MarkupStream(markup)
      findAndAddHeaderComponents(stream)

    }
    super.onInitialize()

  }
  def findAndAddHeaderComponents(stream: MarkupStream) {

    while (stream.skipUntil(classOf[ComponentTag])) {

      val tag = stream.getTag()
      if (tag.isOpen() || tag.isOpenClose()) {

        if (tag.isInstanceOf[WicketTag]) {

          val uitype = tag.getAttribute("type")
          var markupId = tag.getAttribute("id")
          if (markupId == null) {
            markupId = uitype + "-" + getAutoIndex();
            storeMarkupIdForLaterUse(uitype, markupId)
          }

          if (headComponentCreator != null) {
            val headComponent = headComponentCreator(uitype, markupId)
            if (headComponent != null)
              add(headComponent)
          }
          if (tag.isOpen())
            stream.skipToMatchingCloseTag(tag)
        }
      }
      stream.next()
    }
  }

  override def getMarkupResourceStream(
    con: MarkupContainer, cl: Class[_]): IResourceStream = {
    val str = sourceProvider()
    new StringResourceStream(str)
  }
  override def getCacheKey(con: MarkupContainer, conClass: Class[_]): String = {
    null
  }

  def resolve(container: MarkupContainer, markupstream: MarkupStream, tag: ComponentTag): Component = {

    if (tag.isInstanceOf[WicketTag]) {

      var markupId = tag.getAttribute("id")
      val uitype = tag.getAttribute("type")
      if (markupId == null) {
        markupId = retrieveMarkupIdInOrder(uitype);
      }

      try {
        val c = componentCreator(uitype, markupId)
        return c;
      } catch {
        case e => e.printStackTrace()
      }
    }
    null
  }

  def storeMarkupIdForLaterUse(uitype: String, id: String) {
    if (!idMap.contains(uitype))
      idMap += uitype -> new Queue()
    idMap(uitype).enqueue(id)
  }
  def retrieveMarkupIdInOrder(uitype: String): String = {
    idMap(uitype).dequeue()
  }
}
