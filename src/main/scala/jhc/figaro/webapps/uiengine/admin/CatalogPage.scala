package jhc.figaro.webapps.uiengine.admin
import jhc.figaro.webapps.uiengine.LDM
import org.apache.wicket.markup.html.WebPage
import jhc.figaro.webapps.uiengine.UIComponentCreator._
import org.apache.wicket.model.Model
import jhc.figaro.webapps.uiengine.UIComponent
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label

class CatalogPage extends WebPage() {

	add(new AdminMenu("navigation",classOf[CatalogPage]))
	add(new CatalogPanel("components",LDM.of(
    () => { findComponentObjectsOnClasspath("jhc") })))

}
