package jhc.figaro.webapps.uiengine.content
import java.io.InputStream

case class Content(path:String,
		   contentType:String,
		   status:Integer,
		   charset:String,
		   role:String,
		   content:Array[Byte]){
  def isHtml(): Boolean = {
    contentType != null && contentType.indexOf("html") > -1
  }
}
