package jhc.figaro.webapps.uiengine.admin
import java.util.List
import jhc.figaro.webapps.uiengine.content.Content
import org.apache.wicket.markup.html.IHeaderContributor
import org.apache.wicket.markup.html.IHeaderResponse
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.IModel
import org.apache.wicket.request.resource.PackageResource
import org.apache.wicket.request.resource.PackageResourceReference
import org.apache.wicket.markup.html.panel.FeedbackPanel
import org.apache.wicket.ajax.AjaxRequestTarget

abstract class ContentTreePanel(id:String,contentList:IModel[List[Content]]) 
	 extends Panel(id) with IHeaderContributor {

  val feed = new FeedbackPanel("feedback")
  add(feed)

  add(new ListView("list",contentList){
    override def populateItem(item:ListItem[Content]){
      item.add(new ContentPanel("content",item.getModel()){
    	override def onChanged(model:IModel[Content]){
    	  contentChanged(model)
    	}
      override def onRoleError(target:AjaxRequestTarget){
        target.addComponent(feed)
      }
    });
    }
  })

  override def renderHead(response:IHeaderResponse){
    response.renderJavaScriptReference(
      new PackageResourceReference(
	classOf[ContentTreePanel],classOf[ContentTreePanel].getSimpleName()+".js"));
    response.renderOnDomReadyJavaScript("ContentTreePanel.createTree('contentlist')");
  }
  def contentChanged(model:IModel[Content])

}
