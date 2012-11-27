package jhc.figaro.webapps.uiengine.admin
import java.lang.CharSequence
import jhc.figaro.webapps.uiengine.HeaderComponent
import jhc.figaro.webapps.uiengine.traits.UIWithJavascriptDependencies
import org.apache.wicket.Component
import org.apache.wicket.Page
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.MarkupStream
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.request.resource.PackageResourceReference
import scripts.Scripts

class ComponentTestRunnerPage(spec:String,pageUrl:CharSequence,data:() => String) 
  extends WebPage {

  val scriptFunction = () => {
    var dataResult = data()
    "function getTestUrl() { return 'hello/"+pageUrl+"'; };"+
    (if(dataResult==null) "" else dataResult)
  }

  val target = new HeaderComponent("scriptTarget",
				   scriptFunction,
				   Array(spec))

  add(target)

}
