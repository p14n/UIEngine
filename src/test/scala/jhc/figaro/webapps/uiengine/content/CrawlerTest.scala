package jhc.figaro.webapps.uiengine.content
import org.scalatest.FunSuite

import java.io.File
import jhc.figaro.webapps.uiengine.content.CrawlerFunctions._
import scala.collection.mutable.MutableList

class CrawlerSuite extends FunSuite {

  ignore("should get google home page"){
   val dir = "src/test/data/content/jhc/figaro/webapps/"+
			 "uiengine/CrawlerSuite/";
   def index = new File(dir + "index.html")
   if(index.exists()) index.delete()
   def crawler = new Crawler("https://www.google.co.uk/","index.html",null,0)
   crawler.fetch(null)
   assert(index.exists())
 }

  test("should give meaningful name to page at root level"){
    assert("index.html" === constructNameFor("http://a.com/html/",
					    "http://a.com/html/index.html"))
  }
  test("should give image subdirectory for image in subdirectory"){
    assert("images/test.gif" === constructNameFor("http://a.com/",
						 "http://a.com/images/test.gif"))
  }
  test("should find full content url from relative url"){
    assert("http://a.com/images/test.gif" === constructContentUrlFor(
      "http://a.com/index.html","images/test.gif"))
  }
  test("should find full content url from relative root url"){
    assert("http://a.com/images/test.gif" === constructContentUrlFor(
      "http://a.com/index.html","/images/test.gif"))
  }
  test("should count traversals"){
    assert(2 === countTraversals("../../index.html"))
  }
  test("should find full content url from relative directory traversal"){
    assert("http://a.com/images/test.gif" === constructContentUrlFor(
      "http://a.com/html/index.html","../images/test.gif"))
  }
  test("should remove 2 paths from url"){
    assert("http://a.com/one/" === removePathsFromUrl("http://a.com/one/two/three/index.html",
						      2))
  }
  test("should find image source path"){
    assert(MutableList("images/test.gif") ===
      getOtherPaths("<span class=\"hi\"/><img src=\"images/test.gif\"></img>","img","src"))
  }
  test("should find link path"){
    assert(MutableList("html/other") ===
      getOtherPaths("<span class=\"hi\"/><a href=\"html/other\"></a>","a","href"))
  }
  test("should find images from css"){
    assert(MutableList("images/test.gif","images/test2.gif") ===
      getImagesFromCSS("url(images/test.gif) url('images/test2.gif')" ))
  }
}
