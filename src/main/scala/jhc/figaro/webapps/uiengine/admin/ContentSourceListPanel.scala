package jhc.figaro.webapps.uiengine.admin

import jhc.figaro.webapps.uiengine.DBFunctions
import jhc.figaro.webapps.uiengine.content.ContentSource
import jhc.figaro.webapps.uiengine.admin.db.DBService
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink
import org.apache.wicket.markup.html.form.{Form, TextField}
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.repeater.Item
import org.apache.wicket.model.{IModel,Model}
import jhc.figaro.webapps.uiengine.ServiceLocator.{db => data}
import scala.actors.Futures._
import jhc.figaro.webapps.uiengine.content.Crawler
import java.io.Writer
import org.apache.wicket.request.cycle.RequestCycle
import scala.actors.Future
import java.io.StringWriter

abstract class ContentSourceListPanel(id:String,
			     model:IModel[java.util.List[ContentSource]]) extends Panel(id) {

  val parentPanel = this

  setOutputMarkupId(true)

  add(new ListView("list",model){

    override def populateItem(item:ListItem[ContentSource]){

      item.add(new ContentSourcePanel("contentSource",item.getModel()) {

       override def addFeeder(path:String){
         val cs = item.getModelObject()
         cs.feeders = if(cs.feeders==null) List(path) else path :: cs.feeders
         data.updateContentSource(cs)
       }

       override def feed(path:String,target:AjaxRequestTarget){

        val log = new StringWriter() {
          override def write(string:String){
            super.write(string+"<br/>")
          }
        }
        val rootUrl = item.getModelObject().rootUrl
        onFeedStart(target,log,future {
          val crawler = new Crawler(rootUrl,path,data)
          DBFunctions.doWithConnection(()=>{ crawler.fetch(log)})
          true
        })
       }

       override def removeSource(src:ContentSource,target:AjaxRequestTarget){
        data.deleteContentSource(src.rootUrl)
        model.detach()
        target.addComponent(parentPanel)
       }

       override def removeFeeder(path:String){
         val cs = item.getModelObject()
         cs.feeders = List(path) diff cs.feeders
         data.updateContentSource(cs)
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
      data.addContentSource(new ContentSource(urlModel.getObject(),null))
      target.add(ContentSourceListPanel.this)
    }

    override def onError(target:AjaxRequestTarget,form:Form[_]){
      target.add(ContentSourceListPanel.this)
    }

  })

  def onFeedStart(target:AjaxRequestTarget,log:Writer,f:Future[Boolean])

}
