package jhc.figaro.webapps.uiengine.content
import java.io.FileNotFoundException

import scala.collection.mutable.Set
import scala.io.Source
import java.io.InputStream
import java.net.URL
import java.net.HttpURLConnection
import java.io.FileWriter
import scala.collection.mutable.MutableList
import jhc.figaro.webapps.uiengine.content.CrawlerFunctions._
import jhc.figaro.webapps.uiengine.content.ContentFunctions._

class Crawler(rootUrl: String, val outputDirectory: String, 
	      startPage: String = "index.html", depth: Int = -1) {

  def fetch(){
    fetchFrom(rootUrl+startPage,Set())
  }
  def fetchFrom(url: String, visited: Set[String]){

    if(visited.contains(url)) return
    visited.add(url)

    val conn = new URL(url).openConnection().asInstanceOf[HttpURLConnection]

    conn.setRequestMethod("GET")

    if(conn.getResponseCode() != 200 ) return

    val output = conn.getInputStream()

    var name = outputDirectory + constructNameFor(rootUrl, url);
    writeToFile(output,name)

    try {
      val contentType = conn.getContentType().toLowerCase();
      if(contentType.indexOf("text/html") > -1){
	val page = Source.fromFile(name).mkString
	parseAndFetch(page,"a","href",url,visited)
	parseAndFetch(page,"img","src",url,visited)
	parseAndFetch(page,"link","href",url,visited)
      } else if (contentType.indexOf("css") > -1 ){
	val page = Source.fromFile(name).mkString
	getImagesFromCSS(page) foreach { path => 
	  println(url+" "+path);
	  fetchFrom(constructContentUrlFor(url, path), visited);
	}
      }
    } catch {
      case fnf: FileNotFoundException => println("File not saved "+name)
    }
    
  }  
  def parseAndFetch(page: String, tag: String, attr: String, parentUrl: String, visited: Set[String]){
    getOtherPaths(page,tag,attr) foreach { path =>
      fetchFrom(constructContentUrlFor(parentUrl,path), visited);
    }
  }
}
