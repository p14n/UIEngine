package jhc.figaro.webapps.uiengine.admin
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.LoadableDetachableModel
import org.apache.wicket.model.Model
import org.apache.wicket.util.tester.ITestPanelSource
import org.apache.wicket.util.tester.WicketTester
import org.scalatest.FunSuite

class ComponentTestFrameTest extends FunSuite {
  test("should display a warning for a component with no tests"){
    val tester = new WicketTester();
    tester.startPanel(new ITestPanelSource() {
      override def getTestPanel(id: String): Panel = {
	return new ComponentTestFrame(id,Model.of(new ComponentForTestA()));
      }
    })
    tester.assertLabel("panel:warning","No tests found")

  }
}
