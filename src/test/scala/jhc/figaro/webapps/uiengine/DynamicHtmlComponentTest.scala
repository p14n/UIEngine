package jhc.figaro.webapps.uiengine

import org.apache.wicket.util.resource.IResourceStream
import org.apache.wicket.MarkupContainer
import org.scalatest.FunSuite
import org.apache.wicket.Page
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.util.tester.WicketTester
import org.junit.Assert._
import org.apache.wicket.markup.IMarkupResourceStreamProvider
import org.apache.wicket.util.resource.StringResourceStream

class JavascriptPage extends WebPage with IMarkupResourceStreamProvider {

  val jsCreator = { "myCall('myparam')" }
  val scriptCreator = { Array("/scripts/amazing.js") }
  val htmlCreator = { "<span id=\"test\"/>" }

  add(new HeaderComponent("headjscomp",jsCreator,scriptCreator))
  add(new DynamicHtmlComponent(
    "jscomp", htmlCreator))

  override def getMarkupResourceStream(con: MarkupContainer, cl: Class[_]): IResourceStream = {
    new StringResourceStream("<html><body><div wicket:id=\"headjscomp\"></div><div wicket:id=\"jscomp\"></div></body></html>")
  }
}

class DynamicHtmlComponentSuite extends FunSuite {

  def createPage(): String = {
    val t = new WicketTester()
    t.startPage(classOf[JavascriptPage])
    t.assertRenderedPage(classOf[JavascriptPage])
    t.getLastResponseAsString()
  }

  test("Adds dynamic script to the page") {

    val page = createPage()
    val wanted = 
"""<script type="text/javascript" id="js_headjscomp">
/*<![CDATA[*/
myCall('myparam')
/*]]>*/
</script>"""

    assertTrue("""Should find dynamic
	       script in the header""", page.indexOf(wanted) > -1)

  }
  
  test("Adds a javascript reference to the header") {

    val page = createPage()
    assertTrue("Should find script reference in the header",
	       page.indexOf(
	       """<script type="text/javascript" src="/scripts/amazing.js"></script>""") > -1)
  }

  test("Adds html to the page") {
    val page = createPage()
    assertTrue("Should find html appended to the tag",page.indexOf("<span id=\"test\"/>") > -1)
  }
}
