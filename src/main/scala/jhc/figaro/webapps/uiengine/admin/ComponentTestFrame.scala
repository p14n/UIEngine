package jhc.figaro.webapps.uiengine.admin

import com.orientechnologies.orient.server.OServerMain

import java.lang.CharSequence
import jhc.figaro.webapps.uiengine.traits.UIWithTests
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.link.InlineFrame
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.model.IModel
import org.apache.wicket.request.cycle.RequestCycle
import org.apache.wicket.request.handler.PageProvider
import org.apache.wicket.request.handler.RenderPageRequestHandler

class ComponentTestFrame(id:String,model:IModel[Serializable]) extends Panel(id) {

    val component = model.getObject()

    add(new Label("warning","No tests found"){
      override def isVisible:Boolean = { !component.isInstanceOf[UIWithTests] }
    })

    val versions = new RepeatingView("versions")
    add(versions)

    if(component.isInstanceOf[UIWithTests]){
      val testComponent = component.asInstanceOf[UIWithTests]
      val uiTest = testComponent.createTest
      uiTest.versions foreach { version =>
	val data = if(uiTest.dataCreator == null) null else () => {uiTest.dataCreator(version)}
	val container = new WebMarkupContainer(versions.newChildId)
	versions.add(container)

	val pageUnderTest = constructTestPageAndGetUrl(uiTest.scriptDependencies.toArray,
						       uiTest.html)
	container.add(new Label("versionLabel","Version "+version))
	container.add(new InlineFrame("iframe",
	    new ComponentTestRunnerPage(uiTest.spec,pageUnderTest,data)))
      }
    }

  def constructTestPageAndGetUrl(dependencies:Array[String],html:() => String):CharSequence = {
    RequestCycle.get().urlFor(new RenderPageRequestHandler(new PageProvider(
      new ComponentTestPage(dependencies,html,null)
    )))
  }
}
