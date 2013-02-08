package jhc.figaro.webapps.uiengine.admin

import org.apache.wicket.model.IModel
import jhc.figaro.webapps.uiengine.UIComponent
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.Model

class CatalogPanel(id: String,model: IModel[Map[String, Serializable]]) extends ComponentListPanel(id,model) {
    override def addComponentDetails(annotation:UIComponent,component:Serializable,details:WebMarkupContainer) {
      details.add(new Label("description",annotation.description()))
      details.add(new ComponentTestFrame("test",Model.of(component)))
    }
}