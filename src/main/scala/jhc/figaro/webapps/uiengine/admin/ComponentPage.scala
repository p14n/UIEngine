package jhc.figaro.webapps.uiengine.admin
import org.apache.wicket.markup.html.WebPage
import jhc.figaro.webapps.uiengine.UIComponentCreator._
import org.apache.wicket.markup.html.WebMarkupContainer
import jhc.figaro.webapps.uiengine.UIComponent
import jhc.figaro.webapps.uiengine.LDM
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.Model

class ComponentPage extends WebPage {

	add(new AdminMenu("navigation",classOf[ComponentPage]))

	add(new ComponentPropertyPanel("components",LDM.of(
    () => { findComponentObjectsOnClasspath("jhc") })))
}
