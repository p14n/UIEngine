package jhc.figaro.webapps.uiengine
import org.apache.wicket.MarkupContainer
import org.apache.wicket.markup.WicketTag

class ComponentFunctions {

  def retrieveOrSetId(container: MarkupContainer, tag: WicketTag, uitype: String): String = {
    null
        /* if (tag.getId() == null) {
            tag.setId("ui_" + uitype + "_" + container.getAutoIndex());
	    tag.setModified(true)
          }
	  */
  }

}
