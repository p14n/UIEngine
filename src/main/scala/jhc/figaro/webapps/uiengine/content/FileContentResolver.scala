package jhc.figaro.webapps.uiengine.content
import java.io.InputStream

import jhc.figaro.webapps.uiengine.content.ContentFunctions._
import scalax.io.Resource

class FileContentResolver(rootPath: String) extends ContentResolver {

  val imageQualifiers = Set("gif","png")
  

  override def resolve(path: String): Content = {
    try {

      val extensionStart = path.lastIndexOf(".")
      val extension = if(extensionStart > 0) path.substring(extensionStart) else ""

      if(imageQualifiers.contains(extension))
	return content("image/"+extension,path)

      if("css" == extension)
	return content("text/"+extension,path)
	
      return content("text/html",path)

    } catch {
      case _ => ;
    }
    return null
  }
 def content(contentType: String, path: String): Content = {
   return new Content(contentType,createByteArray(streamFromFile(rootPath+path)),"UTF-8");
 }							     
 def createByteArray(in: InputStream): Array[Byte] = {
   Resource.fromInputStream(in).byteArray
 }
}
