package jhc.figaro.webapps.uiengine
import org.scalatest.FunSuite
import jhc.figaro.webapps.uiengine.UIComponentCreator.findComponentObjectsOnClasspath
import jhc.figaro.webapps.uiengine.UIComponentCreator.findDynamicJavascriptMethodOnObject

class UIComponentCreatorSuite extends FunSuite {
  test("should find a test component with an annotation"){
    val map = findComponentObjectsOnClasspath("jhc.figaro.webapps.uiengine")
    assert(classOf[TestAnnotatedComponent] == map("test_ui").getClass()) 
  }
  test("should create dynamic javascript from component"){
    val map = findComponentObjectsOnClasspath("jhc.figaro.webapps.uiengine")
    assert("dynamicjavascript" == findDynamicJavascriptMethodOnObject(
      map("test_ui").asInstanceOf[AnyRef])(""))
  }
}
