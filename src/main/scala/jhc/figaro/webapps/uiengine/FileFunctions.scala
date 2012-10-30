package jhc.figaro.webapps.uiengine
import java.io.FileInputStream
import java.io.InputStream

object FileFunctions {

  def streamFromSrcOrClasspath(file:String):InputStream = {
    val srcDir = PropertyStore.get("resource.dir");
    if(srcDir!=null) return new FileInputStream(srcDir+file)
    getClass().getResourceAsStream(file)
  }

}
