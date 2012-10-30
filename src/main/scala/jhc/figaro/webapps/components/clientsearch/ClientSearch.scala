package jhc.figaro.webapps.components.clientsearch
import jhc.figaro.webapps.uiengine.UIComponent
import jhc.figaro.webapps.uiengine.admin.UITest
import jhc.figaro.webapps.uiengine.traits.UIWithDynamicJavascript
import jhc.figaro.webapps.uiengine.traits.UIWithJavascriptDependencies
import jhc.figaro.webapps.uiengine.traits.UIWithPageComponent
import jhc.figaro.webapps.uiengine.traits.UIWithTests
import org.apache.wicket.Component

@UIComponent(name="client.search",description=
"""The advisor user can view a list of their clients using this component.
Clicking on one of the clients logs into that client account. 
Can be used in conjunction with the advisor.search component to give a provider access
to the underlying clients""")
class ClientSearch extends UIWithJavascriptDependencies
  with UIWithPageComponent
  with UIWithTests {

    def myHtml():String = {
      htmlFromFile("clientsearch/ClientSearch.html")
    }
    override def createComponent(id: String): Component = {
      htmlComponent(id,myHtml)
    }

    override def listJavascriptDependencies() : Array[String] = {
      Array(angular,angularResource,
	    classpathJs("core/person/personServices.js"),
	    classpathJs("clientsearch/ClientSearch.js"))
    }

    override def createTest: UITest = {

      testBuilder.withDependencies(listJavascriptDependencies())
	.withSpec(classpathJs("clientsearch/ClientSearchSpec.js"))
	.withAllVersions
	.withHtml(myHtml)
	.withData(createDataForTest)
	.build()
    }

    def createDataForTest(version:String):String = {
      """var clientSearchTestData = 
	[
		{name:"Dean",account:"1",number:"111",external:"E111"},
		{name:"Sean",account:"2",number:"222",external:"E112"},
		{name:"Aean",account:"3",number:"333",external:"E113"},
		{name:"Kean",account:"4",number:"444",external:"E114"},
		]"""
    }


  }
