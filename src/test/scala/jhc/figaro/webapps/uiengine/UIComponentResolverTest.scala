package jhc.figaro.webapps.uiengine

import org.apache.wicket.util.tester.WicketTester
import org.apache.wicket.Page
import org.scalatest.FunSuite
import org.apache.wicket.util.tester.ITestPageSource
import org.apache.wicket.Component

class UIComponentResolverSuite extends FunSuite {

  test("should add component to page"){

    val t = new WicketTester()

    def resolver(id:String,uitype:String): Component = { 
      println("RESOLVING")
      new DynamicHtmlComponent(id,
			       {"<span>special</span>"})}

    t.getApplication().getPageSettings().addComponentResolver(
      new UIComponentResolver(resolver))

    t.startPage(new ITestPageSource(){
      def getTestPage():Page = { new DynamicHtmlPage(
	{"<html xmlns=\"http://www.w3.org/1999/xhtml\" "+
	 "xmlns:jhc=\"http://wicket.apache.org/dtds.data/wicket-xhtml1.4-strict.dtd\" "+
	 "xml:lang=\"en\" lang=\"en\"><head></head><body><jhc:ui type=\"a\"/></body></html>"},
	  { null }) 
      }
    })

    val page = t.getLastResponseAsString()
    println(page)
    assert(page == "<html xmlns=\"http://www.w3.org/1999/xhtml\" "+
	 "xmlns:jhc=\"http://wicket.apache.org/dtds.data/wicket-xhtml1.4-strict.dtd\" "+
	 "xml:lang=\"en\" lang=\"en\"><body>"+
	   "<span>special</span></body></html>")

  }
}
