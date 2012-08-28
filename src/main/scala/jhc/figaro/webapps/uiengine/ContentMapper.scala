package jhc.figaro.webapps.uiengine

import jhc.figaro.webapps.uiengine.content.ContentResolver
import org.apache.wicket.Component
import org.apache.wicket.request.IRequestHandler
import org.apache.wicket.request.Request
import org.apache.wicket.request.Url
import org.apache.wicket.request.handler.RenderPageRequestHandler
import org.apache.wicket.request.mapper.AbstractMapper

class ContentMapper(resolver: ContentResolver,headComponentCreator: (String,String) => Component) extends AbstractMapper {
  override def mapRequest(req: Request) : IRequestHandler = {

    val path = pathFromRequest(req.getUrl())

    val content = resolver.resolve(path)

    if(content.isHtml()){
      val htmlProvider = new DynamicHtmlPageProvider(
	{new String(content.content,content.contentType)}
	,headComponentCreator);

      return new RenderPageRequestHandler(htmlProvider)
    }
    null
  }
  override def mapHandler(handler: IRequestHandler): Url = {
    null
  }
  override def getCompatibilityScore(request: Request): Int = {
    val path = pathFromRequest(request.getUrl())
    val content = resolver.resolve(path)
    if(content == null) return 0
    9
  }
   def pathFromRequest(url: Url): String = {
     val path = url.getPath()
     val query = url.getQueryString()
     if("" != query )
	return path + "?" + query
     path
   }


}
