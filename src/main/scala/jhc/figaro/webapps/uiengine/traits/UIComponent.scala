package jhc.figaro.webapps.uiengine.traits
import org.apache.wicket.Component

trait UIComponent {
  def createComponent(id: String): Component
  def name: String
  def description: String
}
