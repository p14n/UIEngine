package jhc.figaro.webapps.uiengine.admin
import jhc.figaro.webapps.uiengine.LDM
import jhc.figaro.webapps.uiengine.content.Content
import org.apache.wicket.model.IModel
import org.apache.wicket.util.tester.FormTester
import org.apache.wicket.util.tester.WicketTester
import org.scalatest.FunSuite

class ContentPanelSuite extends FunSuite {

  val content = new Content("other/things.html","text/html",200,"utf-8","users",null)

  val tps = TPS.of((id:String)=>{
      new ContentPanel(id,LDM.of(()=>{content})){
	override def onChanged(model:IModel[Content]){}
      }
    })

  test("should display content with path,type,status,role"){
    val t = new WicketTester()

    t.startPanel(tps)

    t.assertLabel("panel:path","other/things.html")
    t.assertLabel("panel:contentType","text/html")
    t.assertLabel("panel:status","200")
    val f = t.newFormTester("panel:form")
    assert(f.getTextComponentValue("role")=="users");
  }

  test("should update role value"){

    val t = new WicketTester()
    t.startPanel(tps)

    assert(content.role=="users")

    val f = t.newFormTester("panel:form")
    f.setValue("role","admin")
    f.submit("submit")
    assert(content.role=="admin")
  }

}
