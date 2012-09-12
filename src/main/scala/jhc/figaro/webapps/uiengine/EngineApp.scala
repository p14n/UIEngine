package jhc.figaro.webapps.uiengine
import jhc.figaro.webapps.uiengine.content.FileContentResolver
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.parser.filter.WicketTagIdentifier
import org.apache.wicket.protocol.http.WebApplication
import jhc.figaro.webapps.uiengine.UIComponentCreator._

class EngineApp extends WebApplication {

 val rootFileContentPath = "/Users/ae589/dev/ws/scala/FWUIEngine/src/test/data/content/jhc/figaro/webapps/uiengine/CrawlerSuite/"

  override def getHomePage(): Class[_<:WebPage] = {
    null
  }
  override def init(){

    WicketTagIdentifier.registerWellKnownTagName("ui")

    val annotatedComponentMap = findComponentObjectsOnClasspath("jhc");
    val componentCreator = new UIComponentCreator(annotatedComponentMap);

    mount(new ContentMapper(new FileContentResolver(rootFileContentPath),
			 componentCreator.createHeaderComponent, 
			    componentCreator.createComponent));
  }
}

 
