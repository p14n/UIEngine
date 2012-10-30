package jhc.figaro.webapps.uiengine.admin
import org.apache.wicket.ajax.AjaxRequestTarget

class AjaxLink(id:String,clickFunction:(AjaxRequestTarget)=>Unit) extends org.apache.wicket.ajax.markup.html.AjaxLink(id) {
  override def onClick(target:AjaxRequestTarget){
    clickFunction(target)
  }
}
