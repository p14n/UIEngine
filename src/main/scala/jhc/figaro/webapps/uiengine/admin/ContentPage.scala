package jhc.figaro.webapps.uiengine.admin
import org.apache.wicket.markup.html.WebPage
import jhc.figaro.webapps.uiengine.admin.db.DBService
import jhc.figaro.webapps.uiengine.LDM
import org.apache.wicket.model.IModel
import jhc.figaro.webapps.uiengine.content.Content
import scala.collection.JavaConversions._
import jhc.figaro.webapps.uiengine.ServiceLocator.{db => data}

class ContentPage extends WebPage {

	add(new AdminMenu("navigation",classOf[ContentPage]))
	add(new ContentSourceListPanel("contentSourceList", 
		LDM.of(()=>{
			asList(data.getContentSources())
		}),data))

	add(new ContentTreePanel("contentList",
		LDM.of(()=>{data.getContent()})){
		override def contentChanged(content:IModel[Content]){
			data.updateContent(content.getObject())
		}
	})
}


