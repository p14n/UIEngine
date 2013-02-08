package jhc.figaro.webapps.uiengine.admin
import jhc.figaro.webapps.uiengine.LDM
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.LoadableDetachableModel
import org.apache.wicket.model.Model
import org.apache.wicket.util.tester.ITestPanelSource
import org.apache.wicket.util.tester.WicketTester
import org.scalatest.FunSuite

class ComponentListPanelSuite extends FunSuite {
  test("should display list of components with name labels"){
    val tester = new WicketTester();
    tester.startPanel(new ITestPanelSource() {
      override def getTestPanel(id: String): Panel = {
	val nameObjectMap = Map[String,Serializable]("comp1" -> new ComponentForTestA())
	return new CatalogPanel(id,LDM.of(() => {nameObjectMap}));
      }
    })
    tester.assertLabel("panel:list:1:link:name","test.a")
    tester.clickLink("panel:list:1:link")
    tester.assertLabel("panel:list:1:details:description","A test component")
  }
}
