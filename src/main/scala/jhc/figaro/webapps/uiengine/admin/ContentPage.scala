package jhc.figaro.webapps.uiengine.admin
import org.apache.wicket.markup.html.WebPage
import jhc.figaro.webapps.uiengine.admin.db.DBService
import jhc.figaro.webapps.uiengine.LDM
import org.apache.wicket.model.IModel
import jhc.figaro.webapps.uiengine.content.Content
import scala.collection.JavaConversions._
import jhc.figaro.webapps.uiengine.ServiceLocator.{db => data}
import org.apache.wicket.model.Model
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.ajax.{AjaxRequestTarget,AjaxSelfUpdatingTimerBehavior}
import org.apache.wicket.util.time.Duration
import java.io.{StringWriter, Writer}
import org.apache.wicket.markup.html.WebMarkupContainer

class ContentPage extends WebPage {

	val feedMessage = Model.of("")
	val feedDiv = new WebMarkupContainer("feedDiv")
	feedDiv.add(new Label("feedLabel",feedMessage))

	add(feedDiv)
	var timer:AjaxSelfUpdatingTimerBehavior = null

	add(new AdminMenu("navigation",classOf[ContentPage]))
	add(new ContentSourceListPanel("contentSourceList", 
		LDM.of(()=>{
			asList(data.getContentSources())
		})){
		override def onFeedStart(target:AjaxRequestTarget):Writer = {
			feedMessage.setObject("Content being updated")
			timer = new AjaxSelfUpdatingTimerBehavior(Duration.seconds(3)){
				override def onPostProcessTarget(target:AjaxRequestTarget){
					if(isStopped())
						target.addComponent(ContentPage.this)
				}
			}
			feedDiv.add(timer)
			new StringWriter()
		}
		override def onFeedStop(){
			feedMessage.setObject("")
			timer.stop()
			ContentPage.this.remove(timer);
		}
	})

	add(new ContentTreePanel("contentList",
		LDM.of(()=>{data.getContent()})){
		override def contentChanged(content:IModel[Content]){
			data.updateContent(content.getObject())
		}
	})
}


