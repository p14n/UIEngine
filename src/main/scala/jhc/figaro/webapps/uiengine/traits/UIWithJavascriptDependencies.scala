package jhc.figaro.webapps.uiengine.traits
import scripts.Scripts
import org.apache.wicket.request.cycle.RequestCycle
import org.apache.wicket.request.resource.PackageResourceReference

trait UIWithJavascriptDependencies {

  val scripts = classOf[Scripts]

  val mootoolsRef = new PackageResourceReference(scripts,
    "core/mootools/mootools.js")
  val mootoolsMoreRef = new PackageResourceReference(scripts,
    "core/mootools/mootoolsmore.js")

  def urlFor(ref: PackageResourceReference): String = {
    RequestCycle.get().mapUrlFor(ref, null).toString
  }
  def mootools: String = {
    urlFor(mootoolsRef)
  }
  def mootoolsMore: String = {
    urlFor(mootoolsMoreRef)
  }
  def classpathJs(fileInScriptsPackage: String): String = {
    urlFor(new PackageResourceReference(scripts,fileInScriptsPackage))
  }


  def listJavascriptDependencies: Array[String]


}
