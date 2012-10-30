package jhc.figaro.webapps.uiengine.admin
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.protocol.http.WebApplication

class EngineAdminApp extends WebApplication {

  override def getHomePage(): Class[_<:WebPage] = {
    classOf[ContentPage]
  }
}
