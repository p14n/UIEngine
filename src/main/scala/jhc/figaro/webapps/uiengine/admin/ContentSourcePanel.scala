package jhc.figaro.webapps.uiengine.admin
import java.util.ArrayList
import jhc.figaro.webapps.uiengine.LDM
import jhc.figaro.webapps.uiengine.content.Content
import jhc.figaro.webapps.uiengine.content.ContentSource
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.markup.html.panel.FeedbackPanel
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.repeater.Item
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.model.PropertyModel
import scala.collection.JavaConversions._

abstract class ContentSourcePanel(id:String,
			 source:IModel[ContentSource]) extends Panel(id) {

  setOutputMarkupId(true)
  add(new Label("root",new PropertyModel(source,"rootUrl")))
  val panel = this;

  val feederModel = LDM.of(() => {
    val src = source.getObject()
    if(src.feeders == null) new ArrayList[String]() else asList(src.feeders)
  })

  val feeders = new ListView[String]("feeders",feederModel){
    override def populateItem(item:ListItem[String]){
      val path = item.getModelObject()
      item.add(new Label("path",path))
      item.add(new AjaxLink("delete",(t)=>{removeFeeder(path);refresh(t)}))
      item.add(new AjaxLink("feed",(t)=>{feed(path,t);}))
    }
  };
  add(feeders)
  val form = new Form("form")
  val newPath = Model.of("")
  form.add(new TextField("path",newPath))
  form.add(new FeedbackPanel("feed"))
  form.add(new AjaxSubmitLink("submit"){

    override def onSubmit(target:AjaxRequestTarget,form:Form[_]){
      addFeeder(newPath.getObject())
      refresh(target)
    }
    override def onError(target:AjaxRequestTarget,form:Form[_]){
      refresh(target)
    }

  })
  add(form)

  def refresh(target:AjaxRequestTarget){
    target.addComponent(this)
  }
  def feed(path:String,target:AjaxRequestTarget)
  def addFeeder(path:String)
  def removeFeeder(path:String)
}
