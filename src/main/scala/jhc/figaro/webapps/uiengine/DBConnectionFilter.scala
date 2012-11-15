package jhc.figaro.webapps.uiengine
import com.orientechnologies.orient.core.db.graph.OGraphDatabasePool
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal

class DBConnectionFilter extends Filter {

  def doFilter(request:ServletRequest,response:ServletResponse,chain:FilterChain) {
    DBFunctions.doWithConnection(()=>{
      chain.doFilter(request, response)
    })
  }

  def destroy() {
    DBFunctions.closeGlobal
  }
  def init(conf:FilterConfig){}
}
