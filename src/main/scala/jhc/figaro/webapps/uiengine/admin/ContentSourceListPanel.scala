package jhc.figaro.webapps.uiengine.admin
import jhc.figaro.webapps.uiengine.content.ContentSource
import jhc.figaro.webapps.uiengine.admin.db.DBService
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.repeater.Item
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model

class ContentSourceListPanel(id:String,
			     model:IModel[java.util.List[ContentSource]],
			   db:DBService) extends Panel(id) {

  setOutputMarkupId(true)
  add(new ListView("list",model){
    override def populateItem(item:ListItem[ContentSource]){
      item.add(new ContentSourcePanel("contentSource",item.getModel()) {
       override def addFeeder(path:String){
         val cs = item.getModelObject();
         cs.feeders = path :: cs.feeders
         db.updateContentSource(cs)
       }
       override def feed(path:String,target:AjaxRequestTarget){
	  
       }
       override def removeFeeder(path:String){
         val cs = item.getModelObject();
         cs.feeders = List(path) diff cs.feeders
         db.updateContentSource(cs)
       }
      })
    }
  })
  val form = new Form("form");
  add(form)
  val urlModel = Model.of("")
  form.add(new TextField("rootUrl",urlModel))
  form.add(new AjaxSubmitLink("submit"){
    override def onSubmit(target:AjaxRequestTarget,form:Form[_]){
      db.addContentSource(new ContentSource(urlModel.getObject(),null))
      target.add(ContentSourceListPanel.this)
    }
    override def onError(target:AjaxRequestTarget,form:Form[_]){
      target.add(ContentSourceListPanel.this)
    }

  })

}
