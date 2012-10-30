package jhc.figaro.webapps.uiengine
import org.apache.wicket.request.Url
import org.apache.wicket.request.handler.IPageProvider
import org.apache.wicket.request.handler.RenderPageRequestHandler

case class RenderPageRequestHandlerWithUrl(pageProvider: IPageProvider, url: Url) 
  extends RenderPageRequestHandler(pageProvider)
