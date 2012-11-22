package jhc.figaro.webapps.uiengine

import javax.servlet.Filter
import org.slf4j.LoggerFactory
import javax.servlet.{ServletRequest,ServletResponse,FilterChain,FilterConfig}
import javax.servlet.http.HttpServletRequest

object RequestInfoFilter {
	val log = LoggerFactory.getLogger(classOf[RequestInfo])
}
class RequestInfoFilter extends Filter {

  def doFilter(request:ServletRequest,response:ServletResponse,chain:FilterChain) {
  	val info = RequestInfo.renew()
  	val url = request.asInstanceOf[HttpServletRequest].getRequestURL()
    chain.doFilter(request, response)
    RequestInfoFilter.log.info(url+" took "+info.totalTime()+
    	" ("+info.timeSpent(RequestInfo.RESOLVE_CONTENT)+" ns for content and "+
    		info.timeSpent(RequestInfo.CREATE_COMPONENT)+" for components)")
  }
  def init(conf:FilterConfig){}
  def destroy(){}
}