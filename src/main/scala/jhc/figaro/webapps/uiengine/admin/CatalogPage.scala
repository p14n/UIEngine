package jhc.figaro.webapps.uiengine.admin
import jhc.figaro.webapps.uiengine.LDM
import org.apache.wicket.markup.html.WebPage
import jhc.figaro.webapps.uiengine.UIComponentCreator._
import org.apache.wicket.model.Model

class CatalogPage extends WebPage() {

	add(new AdminMenu("navigation",classOf[CatalogPage]))
	add(new ComponentListPanel("components",LDM.of(
    () => { findComponentObjectsOnClasspath("jhc") })))

}
