package jhc.figaro.webapps.uiengine.content

trait ContentReader {

 def getContent(path:String):Content
 def getContent():List[Content]

	
}