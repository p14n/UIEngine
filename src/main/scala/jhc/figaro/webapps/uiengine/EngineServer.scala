package jhc.figaro.webapps.uiengine
import jhc.figaro.webapps.uiengine.admin.EngineAdminApp
import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.protocol.http.WicketFilter
import org.mortbay.jetty.Connector
import org.mortbay.jetty.MimeTypes
import org.mortbay.jetty.Server
import org.mortbay.jetty.bio.SocketConnector
import org.mortbay.jetty.servlet.Context
import org.mortbay.jetty.servlet.DefaultServlet
import org.mortbay.jetty.servlet.HashSessionManager
import org.mortbay.jetty.servlet.SessionHandler

class EngineServer {

  def start() {

    try {

      val server = new Server()

      val connector = new SocketConnector();
      connector.setPort(8080);
      createApp(server,connector,"/",classOf[EngineApp].getName())

      val adminConnector = new SocketConnector();
      adminConnector.setPort(8082);
      createApp(server,adminConnector,"/",classOf[EngineAdminApp].getName())

      server.setConnectors(Array(connector,adminConnector));

      server.start();

    } catch {
      case e:Throwable => e.printStackTrace();
    }
  }

  private def createApp(server:Server,connector: SocketConnector,
			contextPath: String, appClass: String) {

    val context = new Context(server,contextPath);
    context.setConnectorNames(Array(appClass))
    connector.setName(appClass);
    val sess = new SessionHandler();
    sess.setSessionManager(new HashSessionManager());
    context.setSessionHandler(sess);

    val mt = new MimeTypes();
    mt.addMimeMapping("js", "application/javascript");
    context.setMimeTypes(mt);

    context.addFilter(classOf[RequestInfoFilter].getName(),"/*",1)
    context.addFilter(classOf[DBConnectionFilter].getName(),"/*",1)

    val holder = context.addFilter(classOf[WicketFilter].getName(), "/*", 1)
    holder.setInitParameter("applicationClassName", appClass)
    holder.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
    val servletHolder = context.addServlet(classOf[DefaultServlet].getName(), "/*");
    val resDir = PropertyStore.get("resource.dir")
    if(resDir!=null) {
      context.setResourceBase(resDir);
      servletHolder.setInitParameter("cacheControl","max-age=1,public")
    } else {
      context.setResourceBase(".");
    }
  }

}
