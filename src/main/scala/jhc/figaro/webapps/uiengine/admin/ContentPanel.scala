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
import org.apache.wicket.behavior.AttributeAppender
import jhc.figaro.webapps.uiengine.LDM

abstract class ContentPanel(id:String,contentModel:IModel[Content]) extends Panel(id) {

  setDefaultModel(new CompoundPropertyModel[Content](contentModel))
  val f = new Form("form")

  f.add(new Label("contentType"){
    override def isVisible():Boolean = {
      contentModel.getObject().contentType != null
    }
  })
  val status = new Label("status");
  status.add(new AttributeAppender("class",LDM.of(()=>{
    if(contentModel.getObject().status==200) "label-success" else "label-important"
  })," "))
  f.add(status)

  f.add(new Label("path"))
  add(f)
  f.add(new TextField("role"))
  f.add(new AjaxButton("submit",f) {
    override def onSubmit(target:AjaxRequestTarget,form:Form[_]){
      onChanged(contentModel)
    }
    override def onError(target:AjaxRequestTarget,form:Form[_]){
      onRoleError(target)
    }
    
  })
  def onRoleError(target:AjaxRequestTarget)
  def onChanged(model:IModel[Content])

  
}
