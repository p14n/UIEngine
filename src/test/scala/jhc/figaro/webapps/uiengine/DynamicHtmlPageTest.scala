package jhc.figaro.webapps.uiengine

import org.scalatest.FunSuite
import org.apache.wicket.util.tester.WicketTester
import org.apache.wicket.util.tester.ITestPageSource
import org.apache.wicket.Page

class DynamicHtmlPageSuite extends FunSuite {

  val html = "<html><body><span>This is my html</span></body></html>"
  def htmlCreator = { html }

  test("should display html from supplied function"){
    
    val t = new WicketTester()
    t.startPage(new ITestPageSource(){
      def getTestPage(): Page = { new DynamicHtmlPage(htmlCreator,{ null }) }
    })

    val page = t.getLastResponseAsString()
    println(page)
    assert(page == html)
  }
}
