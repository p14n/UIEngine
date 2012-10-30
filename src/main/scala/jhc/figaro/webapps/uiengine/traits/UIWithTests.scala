package jhc.figaro.webapps.uiengine.traits
import jhc.figaro.webapps.uiengine.admin.UITest
import jhc.figaro.webapps.uiengine.admin.UITestBuilder

trait UIWithTests {

  def createTest: UITest

  def testBuilder: UITestBuilder = {
    new UITestBuilder()
  }


}
