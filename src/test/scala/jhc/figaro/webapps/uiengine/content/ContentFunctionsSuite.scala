package jhc.figaro.webapps.uiengine.content
import java.io.ByteArrayInputStream
import org.scalatest.FunSuite
import scala.io.Source
import jhc.figaro.webapps.uiengine.content.ContentFunctions._
import scalax.file.Path
import scalax.io.Resource
import java.io.File

class ContentFunctionsSuite extends FunSuite {

  test("should save stream to file") {

    val filename = "src/test/data/jhc/figaro/webapps/uiengine/content/ContentFunctionsTest_write.txt"
    val file = new File(filename)
    if(file.exists())
      file.delete()

    val src = new ByteArrayInputStream("MYSTRING\nhekkl\nme\n".getBytes)
    writeToFile(src,filename)

    assert("MYSTRING\nhekkl\nme\n" === Source.fromFile(filename).mkString)
  }

  test("should return file as stream") {

    val stream = streamFromFile(
      "src/test/data/jhc/figaro/webapps/uiengine/content/ContentFunctionsTest_saved.txt")
    assert("MYSAVEDSTRING" === Source.fromInputStream(stream).mkString.trim)
  }


/*
 *
 * get from url, save to file and return string
 * get from url, return stream
 * get from file, return stream
*/
}
