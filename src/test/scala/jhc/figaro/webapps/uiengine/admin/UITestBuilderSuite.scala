package jhc.figaro.webapps.uiengine.admin
import org.scalatest.FunSuite

class UITestBuilderSuite extends FunSuite {

  test("Should build a test with spec and dependencies"){
    val test = new UITestBuilder()
      .withSpec("spec").withDependencies(Array[String]("dep1","dep2")).build()
    assert(test.scriptDependencies.size == 2)
    assert(test.spec == "spec")
  }
}










