package jhc.figaro.webapps.uiengine.admin
import org.apache.wicket.Component
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.util.tester.ITestPanelSource

object TPS {

  def of(creator:(String) => Panel): ITestPanelSource = {
    new ITestPanelSource() {
      override def getTestPanel(id: String): Panel = {
	creator(id)
      }
    }
  }

}
