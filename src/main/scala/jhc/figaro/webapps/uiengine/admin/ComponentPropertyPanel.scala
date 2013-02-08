package jhc.figaro.webapps.uiengine.admin

import org.apache.wicket.markup.html.panel.Panel
import jhc.figaro.webapps.uiengine.UIComponent
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.model.IModel
import jhc.figaro.webapps.uiengine.UIComponentCreator
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.basic.Label
import jhc.figaro.webapps.uiengine.LDM
import org.apache.wicket.model.Model
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink
import org.apache.wicket.ajax.AjaxRequestTarget
import jhc.figaro.webapps.uiengine.ServiceLocator
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.model.util.ListModel
import scala.collection.JavaConversions._
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.markup.html.panel.FeedbackPanel

class ComponentPropertyPanel(
     id: String,
     model: IModel[Map[String, Serializable]]) extends ComponentListPanel(id,model) {
    override def addComponentDetails(
        annotation:UIComponent,component:Serializable,details:WebMarkupContainer) {

    	val props  = UIComponentCreator.findPropertiesMethodOnObject(component)
        details.add(new FeedbackPanel("feedback"))

    	val list = new ListView("props",new ListModel(asList(props))){

            override def populateItem(item:ListItem[ComponentProperty]){

                val prop = item.getModelObject();
                val f = new Form("propform");
                item.add(f)

                val saved = ServiceLocator.db.getComponentProperties(annotation.name,"en");
                val current = Model.of(if(saved.contains(prop.uikey)) saved(prop.uikey) else prop.defaultValue);

                f.add(new Label("description",Model.of(prop.description)))
                f.add(new TextField("value",current))
                f.add(new AjaxSubmitLink("link"){
                    def onSubmit(target:AjaxRequestTarget,f:Form[_]){
                        val value = current.getObject();
                        if(!prop.defaultValue.equals(value)){
                            ServiceLocator.db.saveComponentProperty(
                                annotation.name, prop.uikey, "en", value)
                            info(value+" saved");
                        }
                        target.addComponent(details)
                    }
                    def onError(target:AjaxRequestTarget,f:Form[_]){
                        target.addComponent(details)
                    }
                })


            }
        };
        details.setOutputMarkupId(true)
        list.setReuseItems(true)
        details.add(list)

    }
}