package jhc.figaro.webapps.uiengine.admin

import org.scalatest.FunSuite
import org.apache.wicket.util.tester.WicketTester
import jhc.figaro.webapps.uiengine.content.ContentSource
import java.util.ArrayList
import org.apache.wicket.model.util.ListModel
import org.apache.wicket.ajax.AjaxRequestTarget
import scala.actors.Future
import java.io.Writer

class ContentSourceListPanelSuite extends FunSuite {
	
	test("should start panel"){
		val t = new WicketTester()
		val cs = new ContentSource("root/",List[String]())
		val listm = new ListModel(new ArrayList[ContentSource]())
		listm.getObject().add(cs)
		t.startPanel(TPS.of((id:String)=>{
			new ContentSourceListPanel(id, listm){
				override def onFeedStart(target:AjaxRequestTarget,log:Writer,f:Future[Boolean]){
					
				}
			}
		}));
	}
}