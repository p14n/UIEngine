package jhc.figaro.webapps.uiengine.content
import java.io.FileNotFoundException
import java.io.Writer
import scala.collection.mutable.Set
import scala.io.Source
import java.io.InputStream
import java.net.URL
import java.net.HttpURLConnection
import java.io.FileWriter
import scala.collection.mutable.MutableList
import jhc.figaro.webapps.uiengine.content.CrawlerFunctions._
import jhc.figaro.webapps.uiengine.content.ContentFunctions._
import scalax.io.Resource
import org.slf4j.{Logger,LoggerFactory}


class Crawler(rootUrl: String, startPage: String = "index.html", 
	      writer:ContentWriter,depth: Int = -1) {

  val syslog = LoggerFactory.getLogger(classOf[Crawler])

  def fetch(log:Writer){
    fetchFrom(rootUrl+startPage,Set(),log)
  }
  def fetchFrom(url: String, visited: Set[String],log:Writer){

    if(visited.contains(url)) return
    visited.add(url)

    val conn = new URL(url).openConnection().asInstanceOf[HttpURLConnection]

    conn.setRequestMethod("GET")

    val responseCode = conn.getResponseCode()
    var name = constructNameFor(rootUrl, url);

    info(log,"Response code "+responseCode+" from "+name)
    if(conn.getResponseCode() != 200 ) {
      writer.putContent(new Content(name,null,responseCode,null,null,null))
      info(log,"Saved missing content "+name)
      return
    }

    val contentType = conn.getContentType().toLowerCase();
    val encoding = conn.getContentEncoding()
    val bytes = Resource.fromInputStream(conn.getInputStream()).byteArray
    writer.putContent(new Content(name,contentType,responseCode,encoding,null,bytes))
    info(log,"Saved content type "+contentType)

    try {
      if(contentType.indexOf("text/html") > -1){
        info(log,"Parsing "+name+" for links")
      	val page = new String(bytes,if(encoding==null) "UTF-8" else encoding)
      	parseAndFetch(page,"a","href",url,visited,log)
      	parseAndFetch(page,"img","src",url,visited,log)
      	parseAndFetch(page,"link","href",url,visited,log)
      } else if (contentType.indexOf("css") > -1 ){
        info(log,"Parsing "+name+" for images")
        val page = new String(bytes,if(encoding==null) "UTF-8" else encoding)
      	getImagesFromCSS(page) foreach { path => 
      	  fetchFrom(constructContentUrlFor(url, path), visited,log);
      	}
      }
    } catch {
      case fnf: FileNotFoundException => {
        val text = "File not saved "+name; 
        log.write(text)
        syslog.error(text,fnf)
      }
    }
    
  }  
  def info(log:Writer,text:String){
        log.write(text)
        syslog.info(text)
  }
  def parseAndFetch(page: String, tag: String, attr: String, parentUrl: String, visited: Set[String],log:Writer){
    getOtherPaths(page,tag,attr) foreach { path =>
      fetchFrom(constructContentUrlFor(parentUrl,path), visited,log);
    }
  }
}
