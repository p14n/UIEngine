package jhc.figaro.webapps.uiengine

import java.nio.charset.Charset
import jhc.figaro.webapps.uiengine.content.{Content, ContentResolver}
import org.apache.wicket.mock.MockWebRequest
import org.apache.wicket.request.Url
import org.apache.wicket.request.handler.RenderPageRequestHandler
import org.scalatest.FunSuite
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

class ContentMapperSuite extends FunSuite {

  def mockRequest(path: String): MockWebRequest = {
    new MockWebRequest(
      new Url(asList(ListBuffer(List(path): _*))
	,Charset.forName("UTF-8")));
  }
  def createResolver(expectedPath: String, contentType: String): ContentResolver = {
    new ContentResolver(){
      override def resolve(path: String): Content = {
	if(path == expectedPath) return Content(path,contentType,200,"UTF-8",null,"".getBytes())
	null
      }
    }
  }
  test("should return dynamic html page"){

    val resolver = createResolver("my_test_page","html");

    val request = mockRequest("my_test_page")
    val mapper = new ContentMapper(resolver,{null},{null});
    val handler = mapper.mapRequest(request).asInstanceOf[RenderPageRequestHandler];
    
    assert(handler.isInstanceOf[RenderPageRequestHandler]);
    println(handler.getPageClass())
    assert(handler.getPageClass() == classOf[DynamicHtmlPage])
  }

  test("should return resource"){
    
  }
}
