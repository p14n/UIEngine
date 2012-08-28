package jhc.figaro.webapps.uiengine
import java.lang.Class
import org.apache.wicket.Component
import org.apache.wicket.request.component.IRequestablePage
import org.apache.wicket.request.handler.IPageProvider
import org.apache.wicket.request.mapper.parameter.PageParameters

class DynamicHtmlPageProvider(sourceProvider: => String,
		    headComponentCreator: (String,String) => Component) extends IPageProvider { 

  override def getPageClass():Class[_<:IRequestablePage] = { 
    classOf[DynamicHtmlPage]
  }
  override def getPageInstance():IRequestablePage = { 
    new DynamicHtmlPage(sourceProvider,headComponentCreator)
  }
  override def getPageId():Integer = {0}
  override def detach(){ }
  override def getPageParameters():PageParameters = { null }
  override def getRenderCount():Integer = { 1 }
  override def isNewPageInstance():Boolean = { true }
}
