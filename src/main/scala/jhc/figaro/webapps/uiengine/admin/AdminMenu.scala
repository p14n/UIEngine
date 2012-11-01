package jhc.figaro.webapps.uiengine.admin
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.WebMarkupContainer

class AdminMenu(id:String,selected:Class[_<:WebPage]) extends Panel(id) {

  val list = new RepeatingView("nav")

  List(classOf[ContentPage],classOf[ComponentPage],classOf[CatalogPage]).foreach { page => 
    val listItem = new WebMarkupContainer(list.newChildId())
    val link = new Link("link"){
      override def onClick(){
       setResponsePage(page)
      }
    };
    val name = page.getSimpleName()
    link.add(new Label("page",name.substring(0,name.indexOf("Page"))))
    listItem.add(link)
    link.setEnabled(page != selected)
										   
  }
  add(list)
}

