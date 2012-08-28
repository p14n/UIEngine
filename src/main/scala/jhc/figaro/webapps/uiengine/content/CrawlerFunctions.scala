package jhc.figaro.webapps.uiengine.content

import scala.collection.mutable.MutableList

object CrawlerFunctions {

  def constructNameFor(rootContentUrl: String,
		       thisContentUrl: String): String = {
    thisContentUrl.substring(rootContentUrl.length())
  }
  def constructContentUrlFor(pageUrl: String, relativeUrl: String):String = {
    val traversals = countTraversals(relativeUrl)
    if(traversals == 0) 
      return pageUrl.substring(0,pageUrl.lastIndexOf("/")+1)+removeStartingSlash(relativeUrl)
    removePathsFromUrl(pageUrl, traversals) + 
      relativeUrl.substring(relativeUrl.lastIndexOf("../") + 3)
  }
  def removeStartingSlash(url: String):String = {
    if(url == null) return null;
    if(url.startsWith("/")) return url.substring(1)
    url
  }
  def countTraversals(url: String):Int = {
    url.split("/")
      .groupBy(x => x)
      .mapValues(x => x.length)
      .getOrElse( "..", { 0 } )
  }
  def removePathsFromUrl(url:String, toRemove: Int, lastPosition: Int = -100): String = {
    val currentPosition = if(lastPosition == -100) url.length() else lastPosition
    if(toRemove<1) return url.substring(0,url.lastIndexOf("/",currentPosition) + 1)
    val nextPosition = url.lastIndexOf("/",currentPosition) - 1
    removePathsFromUrl(url, toRemove - 1, nextPosition);
  }
  def getOtherPaths(content: String,tag: String, attr: String):Iterable[String] = {
    var index = 0
    var paths = MutableList[String]()

    while (index<content.length-1 && (paths.size==0 || index > 0 )){
      index = content.indexOf("<"+tag+" ",index) + 1
      if(index==0) return paths;

      val tagEnd = content.indexOf(">",index)
      val href = content.indexOf(attr,index)

      if(href > -1 && href < tagEnd){
	val pathStart = content.indexOf("\"",href)+1
	val path = content.substring(pathStart,content.indexOf("\"",pathStart))

	if(path.indexOf("://") == -1 && !path.startsWith("#"))
	  paths += path
      }
      index = tagEnd;

    }
    paths
  }
  def getImagesFromCSS(content: String):Iterable[String] = {
    var index = 0
    var paths = MutableList[String]()

    while (index<content.length-1 && (paths.size==0 || index > 0 )){
      index = content.indexOf("url(",index) + 4
      if(index==3) return paths;

      val tagEnd = content.indexOf(")",index)

      if(tagEnd > index){
	  paths += content.substring(index, tagEnd).replaceAll("'","");
      }
      index = tagEnd;

    }
    paths
  }

}
