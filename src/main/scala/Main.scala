import java.util.Date
import java.io.File
import jhc.figaro.webapps.uiengine.content.Crawler

object Main {
  def greet(name : String) = "Hello "+name

  def main(args : Array[String]) : Unit = { 
   val dir = "src/test/data/content/jhc/figaro/webapps/"+
			 "uiengine/CrawlerSuite/";
   def index = new File(dir + "index.html")
   if(index.exists()) index.delete()
   def crawler = new Crawler("http://localhost:9001/modx/",dir,"index.php?id=2",1)
   crawler.fetch()
  }
}
