package jhc.figaro.webapps.uiengine.admin
import org.apache.wicket.markup.html.WebPage
import jhc.figaro.webapps.uiengine.admin.db.DBService
import jhc.figaro.webapps.uiengine.LDM
import org.apache.wicket.model.IModel
import jhc.figaro.webapps.uiengine.content.Content
import scala.collection.JavaConversions._

class ContentPage extends WebPage {
	val db = getDBService()
	add(new AdminMenu("navigation",classOf[ContentPage]))
	add(new ContentSourceListPanel("contentSourceList", LDM.of(
		()=>{asList(db.getContentSources())}),db))
	add(new ContentTreePanel("contentList",LDM.of(
		()=>{db.getContent()})){
		override def contentChanged(content:IModel[Content]){
			db.updateContent(content.getObject())
		}
	})
	def getDBService():DBService = {
		new DBService()
	}
}


