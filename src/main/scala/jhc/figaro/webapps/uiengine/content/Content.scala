package jhc.figaro.webapps.uiengine.content
import java.io.InputStream

case class Content(contentType: String, content: Array[Byte], charset: String){
  def isHtml(): Boolean = {
    contentType != null && contentType.indexOf("html") > -1
  }
}
