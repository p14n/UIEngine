package jhc.figaro.webapps.uiengine.admin
import org.apache.wicket.markup.html.WebPage
import jhc.figaro.webapps.uiengine.UIComponentCreator._

class ComponentPage extends WebPage {

	add(new AdminMenu("navigation",classOf[ComponentPage]))

/*	add(new ComponentListPanel("componenents"),LDM.of(
		() => {
			    val annotatedComponentMap = findComponentObjectsOnClasspath("jhc");
		}))*/		
}
