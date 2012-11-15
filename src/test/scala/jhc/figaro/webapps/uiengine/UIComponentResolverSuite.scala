package jhc.figaro.webapps.uiengine
import org.apache.wicket.markup.parser.filter.WicketTagIdentifier

import org.apache.wicket.util.tester.WicketTester
import org.apache.wicket.Page
import org.scalatest.FunSuite
import org.apache.wicket.util.tester.ITestPageSource
import org.apache.wicket.Component

class UIComponentResolverSuite extends FunSuite {

  test("should add component to page"){

    WicketTagIdentifier.registerWellKnownTagName("ui")

    val t = new WicketTester()

    def resolver(id:String,uitype:String): Component = { 
      new DynamicHtmlComponent(id,
			       {"<span>special</span>"})
    }


    t.startPage(new ITestPageSource(){
      def getTestPage():Page = { new DynamicHtmlPage(
	() => {"<html xmlns=\"http://www.w3.org/1999/xhtml\" "+
	 "xmlns:jhc=\"http://wicket.apache.org/dtds.data/wicket-xhtml1.4-strict.dtd\" "+
	 "xml:lang=\"en\" lang=\"en\"><head></head><body><jhc:ui type=\"a\"/></body></html>"},
	  { null },resolver) 
      }
    })

    val page = t.getLastResponseAsString()

    assert(page == "<html xmlns=\"http://www.w3.org/1999/xhtml\" "+
	 "xmlns:jhc=\"http://wicket.apache.org/dtds.data/wicket-xhtml1.4-strict.dtd\" "+
	 "xml:lang=\"en\" lang=\"en\"><body>"+
	   "<span id=\"a\" >special</span></body></html>")

  }
}
