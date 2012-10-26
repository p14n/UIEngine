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

class Crawler(rootUrl: String, startPage: String = "index.html", 
	      writer:ContentWriter,depth: Int = -1) {

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

    log.write("Response code "+responseCode+" from "+name)
    if(conn.getResponseCode() != 200 ) {
      writer.putContent(new Content(name,null,responseCode,null,null,null))
      return
    }

    val contentType = conn.getContentType().toLowerCase();
    val encoding = conn.getContentEncoding()
    val bytes = Resource.fromInputStream(conn.getInputStream()).byteArray
    writer.putContent(new Content(name,contentType,responseCode,encoding,null,bytes))

    try {
      if(contentType.indexOf("text/html") > -1){
	val page = Source.fromFile(name).mkString
	parseAndFetch(page,"a","href",url,visited,log)
	parseAndFetch(page,"img","src",url,visited,log)
	parseAndFetch(page,"link","href",url,visited,log)
      } else if (contentType.indexOf("css") > -1 ){
	val page = Source.fromFile(name).mkString
	getImagesFromCSS(page) foreach { path => 
	  println(url+" "+path);
	  fetchFrom(constructContentUrlFor(url, path), visited,log);
	}
      }
    } catch {
      case fnf: FileNotFoundException => println("File not saved "+name)
    }
    
  }  
  def parseAndFetch(page: String, tag: String, attr: String, parentUrl: String, visited: Set[String],log:Writer){
    getOtherPaths(page,tag,attr) foreach { path =>
      fetchFrom(constructContentUrlFor(parentUrl,path), visited,log);
    }
  }
}
