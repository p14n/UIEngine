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
import scala.actors.Future
import org.apache.wicket.markup.html.link.Link

class ContentPage extends WebPage {

	setOutputMarkupId(true)
	val feedMessage = Model.of("")
	val feedLabel = new Label("feedLabel",feedMessage);
	val feedDiv = new WebMarkupContainer("feedDiv"){
		override def isVisible():Boolean = {
			feedMessage.getObject() != ""
		}
	}
	feedDiv.setOutputMarkupPlaceholderTag(true);
	feedDiv.add(new Link("close"){
		override def onClick(){
			feedMessage.setObject("")
		}
	})
	feedDiv.add(feedLabel)
	feedDiv.setOutputMarkupId(true)
	val feedLog = Model.of("")
	val feedLogLabel = new Label("feedLog",feedLog)
	feedLogLabel.setEscapeModelStrings(false)
	feedDiv.add(feedLogLabel)

	add(feedDiv)

	add(new AdminMenu("navigation",classOf[ContentPage]))
	add(new ContentSourceListPanel("contentSourceList", 
		LDM.of(()=>{
			asList(data.getContentSources())
		})){
		override def onFeedStart(target:AjaxRequestTarget,log:Writer,@serializable future:Future[Boolean]) {

			feedMessage.setObject("Content being updated")

			feedDiv.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(3)){
				override def onPostProcessTarget(target:AjaxRequestTarget){
					feedLog.setObject(log.toString())
					if(future.isSet){
						stop()
						feedDiv.remove(this);
					}
					if(isStopped())
						target.addComponent(ContentPage.this)
				}
			})
			target.add(feedDiv)
			/*val divId = feedDiv.getMarkupId(true)

			target.appendJavaScript("var elem = document.getElementById('"+divId+"');"+
				"elem.scrollTop = elem.scrollHeight;");*/
		}
	})

	add(new ContentTreePanel("contentList",
		LDM.of(()=>{data.getContent()})){
		override def contentChanged(content:IModel[Content]){
			data.updateContent(content.getObject())
		}
	})
}


