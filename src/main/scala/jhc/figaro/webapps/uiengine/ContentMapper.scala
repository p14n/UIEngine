package jhc.figaro.webapps.uiengine

import jhc.figaro.webapps.uiengine.content.ContentResolver
import org.apache.wicket.Component
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.request.IRequestHandler
import org.apache.wicket.request.Request
import org.apache.wicket.request.Url
import org.apache.wicket.request.handler.resource.ResourceRequestHandler
import org.apache.wicket.request.mapper.AbstractMapper
import org.apache.wicket.request.resource.ByteArrayResource
import org.slf4j.LoggerFactory

class ContentMapper(
  resolver: ContentResolver,
  headComponentCreator: (String,String) => Behavior, 
  componentCreator: (String,String,String) => Component) extends AbstractMapper {

  override def mapRequest(req: Request) : IRequestHandler = {

    val log = new ConditionalLogger(classOf[ContentMapper])
    val path = pathFromRequest(req.getUrl())
    val start = System.nanoTime()
    val content = resolver.resolve(path)

    RequestInfo.get().addTimeSpent(start,RequestInfo.RESOLVE_CONTENT)
    log.ifInfo(() => {"Request for path "+path+" found "+
        (if(content==null) "nothing" else content.path+"("+content.contentType+")")})

    if(content == null) return null;

    if(content.isHtml()){
      val charset = if(content.charset == null) "utf-8" else (content.charset)
      val contentAsString = new String(content.content,charset)
      val htmlProvider = new DynamicHtmlPageProvider(() => {contentAsString}
	,headComponentCreator, componentCreator);

      return new RenderPageRequestHandlerWithUrl(htmlProvider,req.getUrl())
    }
    return new ResourceRequestHandler(
      new ByteArrayResource(content.contentType,content.content),null)
  }
  override def mapHandler(handler: IRequestHandler): Url = {
    if (handler.isInstanceOf[RenderPageRequestHandlerWithUrl])
      return handler.asInstanceOf[RenderPageRequestHandlerWithUrl].url
    null
  }
  override def getCompatibilityScore(request: Request): Int = {
    /*val path = pathFromRequest(request.getUrl())
    val content = resolver.resolve(path)
    if(content == null) return 0*/
    9
  }
   def pathFromRequest(url: Url): String = {
     val path = url.getPath()
     val query = url.getQueryString()
     if("" != query )
	return path + query
     path
   }


}
