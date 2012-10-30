package jhc.figaro.webapps.uiengine.admin
import java.util.ArrayList
import jhc.figaro.webapps.uiengine.content.Content
import org.apache.wicket.model.IModel
import org.apache.wicket.model.util.ListModel
import org.apache.wicket.util.tester.WicketTester
import org.scalatest.FunSuite

class ContentTreePanelSuite extends FunSuite {

  test("should create"){

    val list = new ArrayList[Content]()
    list.add( new Content("other/things.html","text/html",200,"utf-8","users",null));
    list.add( new Content("other/stuff.html","text/html",200,"utf-8","users",null));

    val t = new WicketTester()
    t.startPanel(TPS.of((id:String)=>{
      new ContentTreePanel(id,new ListModel[Content](list)){
	override def contentChanged(model:IModel[Content]){}
      }
    }))

    t.assertLabel("panel:list:0:content:path","other/things.html")

  }
}
