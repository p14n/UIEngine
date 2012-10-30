package jhc.figaro.webapps.uiengine.admin
import jhc.figaro.webapps.uiengine.content.ContentSource
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.model.Model
import org.apache.wicket.util.tester.WicketTester
import org.scalatest.FunSuite
import scala.collection.mutable.Set

class ContentSourcePanelSuite extends FunSuite {

  val contentModel = Model.of(new ContentSource("rooty",List("feeder1","feeder2")))
  var map = Set("feeder1","feeder2");
  var feeding = Set[String]()
  val tps = TPS.of((id)=>{new ContentSourcePanel(id,contentModel){
      override def removeFeeder(path:String){map.remove(path)}
      override def addFeeder(path:String){map.add(path)}
      override def feed(path:String,target:AjaxRequestTarget){feeding.add(path)}
    }})

  test("should display two feeders"){

    val t = new WicketTester()
    t.startPanel(tps)
    t.assertLabel("panel:root","rooty")
    t.assertLabel("panel:feeders:0:path","feeder1")
    t.assertLabel("panel:feeders:1:path","feeder2")

  }

  test("should add a feeder"){
    val t = new WicketTester()
    t.startPanel(tps)
    val f = t.newFormTester("panel:form")
    f.setValue("path","feeder3")
    t.clickLink("panel:form:submit")
    assert(map.contains("feeder3"))
  }
  test("should delete a feeder"){
    val t = new WicketTester()
    t.startPanel(tps)
    t.clickLink("panel:feeders:1:delete")
    assert(!map.contains("feeder2"))
  }
  test("should feed a feeder"){
    val t = new WicketTester()
    t.startPanel(tps)
    t.clickLink("panel:feeders:0:feed")
    assert(feeding.contains("feeder1"))
  }
}
