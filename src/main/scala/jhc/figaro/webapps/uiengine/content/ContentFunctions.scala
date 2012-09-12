package jhc.figaro.webapps.uiengine.content
import java.io.FileInputStream
import java.io.InputStream
import scala.io.Source
import scalax.file.Path
import scalax.io.Resource
import scala.io.BufferedSource

object ContentFunctions {

  def writeToFile(src: InputStream, filename: String) {
    val out = Resource.fromFile(filename)
    val stream = Source.fromInputStream(src)

    stream.getLines foreach { line => 
      out.append(line+"\n");
    }

  }

  def streamFromFile(filename: String): InputStream = {
    println("streaming from file "+filename)
    new FileInputStream(filename)
  }

}
