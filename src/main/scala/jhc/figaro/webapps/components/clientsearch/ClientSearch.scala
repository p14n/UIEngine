package jhc.figaro.webapps.components.clientsearch
import jhc.figaro.webapps.uiengine.UIComponent
import jhc.figaro.webapps.uiengine.admin.UITest
import jhc.figaro.webapps.uiengine.traits._
import org.apache.wicket.Component
import jhc.figaro.webapps.uiengine.admin.ComponentProperty

@UIComponent(name="client.search",description=
"""The advisor user can view a list of their clients using this component.
Clicking on one of the clients logs into that client account. 
Can be used in conjunction with the advisor.search component to give a provider access
to the underlying clients""")
class ClientSearch extends UIWithJavascriptDependencies
  with UIWithPageComponent
  with UIWithTests
  with UIWithProperties {

    def myHtml():String = {
      htmlFromFile("clientsearch/ClientSearch.html")
    }
    override def createComponent(id: String,
      propertyApplier: (() => String,List[ComponentProperty]) => String): Component = {
      htmlComponent(id,propertyApplier(myHtml,getProperties()))
    }

    override def listJavascriptDependencies() : Array[String] = {
      Array(jquery,angular,angularResource,directives,
	    classpathJs("core/person/personServices.js"),
	    classpathJs("clientsearch/ClientSearch.js"))
    }

    override def createTest: UITest = {

      testBuilder.withDependencies(listJavascriptDependencies())
	.withSpec(classpathJs("clientsearch/ClientSearchSpec.js"))
	.withAllVersions
	.withHtml(
    () => {replacePropertiesInText(myHtml(),getProperties())})
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

    override def getProperties():List[ComponentProperty] = {
      List(
      prop("client.search.form.legend","Client search","The legend of the client search form","en"),
      prop("client.search.form.label","Search for","The form label in the client search form","en"),
      prop("client.name.label","Client name","The label next to the client name in results","en"),
      prop("client.number.label","Client number","The label next to the client number in results","en"),
      prop("client.account.label","Account number","The label next to the account number in results","en"),
      prop("client.external.ref.label","Client external reference","The label next to the client external reference in results","en"))
    }
  }
