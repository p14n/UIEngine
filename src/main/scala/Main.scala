import java.util.Date
import java.io.File
import jhc.figaro.webapps.uiengine.EngineServer
import jhc.figaro.webapps.uiengine.OrientDBServer
import jhc.figaro.webapps.uiengine.PropertyStore
import jhc.figaro.webapps.uiengine.content.Crawler

object Main {

  def main(args : Array[String]) : Unit = { 

    PropertyStore.addOrDefault(if(args.length > 0) args(0) else null )
    val p = PropertyStore
    val db = new OrientDBServer()
    db.startup(p.get("db.config"),p.get("db.path","./database"),
				 p.get("db.user","admin"),p.get("db.pass","admin"))
    new EngineServer().start

  }
  def crawl = {

   val dir = "src/test/data/content/jhc/figaro/webapps/"+
			 "uiengine/CrawlerSuite/";
   def index = new File(dir + "index.html")
   if(index.exists()) index.delete()
   def crawler = new Crawler("http://localhost:9001/modx/",dir,"index.php?id=2",1)
   crawler.fetch()

  }
}
