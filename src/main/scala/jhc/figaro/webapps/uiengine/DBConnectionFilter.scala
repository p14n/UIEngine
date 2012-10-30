package jhc.figaro.webapps.uiengine
import com.orientechnologies.orient.core.db.graph.OGraphDatabasePool
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

class DBConnectionFilter extends Filter {

  def doFilter(request:ServletRequest,response:ServletResponse,chain:FilterChain) {
      val p = PropertyStore
      val database = OGraphDatabasePool.global().acquire("local:"+
	p.get("db.path"),p.get("db.user"),p.get("db.pass"));
      try{
        chain.doFilter(request, response);
      } finally {
        database.close();
      }
  }

  def destroy() {
      OGraphDatabasePool.global().close();
  }
  def init(conf:FilterConfig){}
}
