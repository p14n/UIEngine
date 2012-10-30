package jhc.figaro.webapps.uiengine.admin
import jhc.figaro.webapps.uiengine.UIComponent
import jhc.figaro.webapps.uiengine.traits.UIWithPageComponent
import org.apache.wicket.Component

@UIComponent(name="test.a",description="A test component")
class ComponentForTestA extends UIWithPageComponent {

    override def createComponent(id: String): Component = {
      htmlComponent(id,htmlFromFile("clientsearch/ClientSearch.html"))
    }


}
