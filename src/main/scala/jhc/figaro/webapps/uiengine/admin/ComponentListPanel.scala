package jhc.figaro.webapps.uiengine.admin
import jhc.figaro.webapps.uiengine.UIComponent
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.model.IModel
import org.apache.wicket.model.LoadableDetachableModel
import org.apache.wicket.model.Model
import scala.collection.immutable.TreeMap

class ComponentListPanel(id: String,model: IModel[Map[String, Serializable]]) extends Panel(id) {

  val annotationClass = classOf[UIComponent];

  val list = new RepeatingView("list");

  add(list)

  val sortedMap = TreeMap(model.getObject.toArray:_*)

  for ((key,value) <- sortedMap) {
    val annotation = value.getClass.getAnnotation(annotationClass).asInstanceOf[UIComponent];

    val componentPanel = new WebMarkupContainer(list.newChildId());
    list.add(componentPanel)

    var clicked = false

    val link = new Link("link"){
      def onClick = { clicked = !clicked }
    }

    link.add(new Label("name",annotation.name()))
    componentPanel.add(link);

    val details = new WebMarkupContainer("details"){
      override def isVisible: Boolean = { clicked }
    }

    componentPanel.add(details)

    details.add(new Label("description",annotation.description()))
    details.add(new ComponentTestFrame("test",Model.of(value)))

  }
}
