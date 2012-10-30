package jhc.figaro.webapps.uiengine.admin
import jhc.figaro.webapps.uiengine.content.Content
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.form.AjaxButton
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.markup.html.panel.FeedbackPanel
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.model.IModel

abstract class ContentPanel(id:String,contentModel:IModel[Content]) extends Panel(id) {

  setDefaultModel(new CompoundPropertyModel[Content](contentModel))

  add(new Label("path"))
  add(new Label("contentType"))
  add(new Label("status"))
  val f = new Form("form")
  add(f)
  f.add(new TextField("role"))
  val feed = new FeedbackPanel("feedback")
  add(feed)
  f.add(new AjaxButton("submit",f) {
    override def onSubmit(target:AjaxRequestTarget,form:Form[_]){
      onChanged(contentModel)
    }
    override def onError(target:AjaxRequestTarget,form:Form[_]){
      target.addComponent(feed)
    }
    
  })

  def onChanged(model:IModel[Content])

  
}
