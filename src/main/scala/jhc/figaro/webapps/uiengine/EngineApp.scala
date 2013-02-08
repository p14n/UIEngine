package jhc.figaro.webapps.uiengine
import jhc.figaro.webapps.uiengine.content.FileContentResolver
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.parser.filter.WicketTagIdentifier
import org.apache.wicket.protocol.http.WebApplication
import jhc.figaro.webapps.uiengine.UIComponentCreator._
import jhc.figaro.webapps.uiengine.content.DBContentResolver
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException

class EngineApp extends WebApplication {

  override def getHomePage(): Class[_<:WebPage] = {
    throw new AbortWithHttpErrorCodeException(404);
  }
  override def init(){

    WicketTagIdentifier.registerWellKnownTagName("ui")

    val annotatedComponentMap = findComponentObjectsOnClasspath("jhc");
    val componentCreator = new UIComponentCreator(annotatedComponentMap,
      ServiceLocator.db.getComponentProperties);

    mount(new ContentMapper(
      new DBContentResolver(ServiceLocator.db),
			 componentCreator.createHeaderComponent, 
			    componentCreator.createComponent));
  }
}

 
