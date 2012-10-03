package jhc.figaro.webapps.uiengine.traits
import jhc.figaro.webapps.uiengine.PropertyStore
import org.apache.wicket.Application
import org.apache.wicket.protocol.http.WebApplication
import scripts.Scripts
import org.apache.wicket.request.cycle.RequestCycle
import org.apache.wicket.request.resource.PackageResourceReference

trait UIWithJavascriptDependencies {

  val scripts = classOf[Scripts]

  val mootoolsRef = new PackageResourceReference(scripts,
    "core/mootools/mootools.js")
  val mootoolsMoreRef = new PackageResourceReference(scripts,
    "core/mootools/mootoolsmore.js")
  val angularJs = new PackageResourceReference(scripts,
    "angular/angular.js")
  val angularJsMin = new PackageResourceReference(scripts,
    "angular/angular.min.js")
  val angularResourceJs = new PackageResourceReference(scripts,
    "angular/angular-resource.js")
  val angularResourceJsMin = new PackageResourceReference(scripts,
    "angular/angular-resource-min.js")
  val angularMocksJs = new PackageResourceReference(scripts,
    "angular/angular-mocks.js")


  def urlFor(ref: PackageResourceReference): String = {
    RequestCycle.get().mapUrlFor(ref, null).toString
  }
  def mootools: String = {
    urlFor(mootoolsRef)
  }
  def mootoolsMore: String = {
    urlFor(mootoolsMoreRef)
  }
  def angular: String = {
    urlFor(if(isDevelopment) angularJs else angularJsMin)
  }
  def angularResource: String = {
    urlFor(if(isDevelopment) angularResourceJs else angularResourceJsMin)
  }
  def angularMocks:String = {
    urlFor(angularMocksJs)
  }
  def isDevelopment: Boolean = {
    try {
      return WebApplication.get().usesDevelopmentConfig()
    } catch {
      case _ => return true
    }
  }

  def classpathJs(fileInScriptsPackage: String): String = {
    if(PropertyStore.has("resource.dir"))
      return "scripts/"+fileInScriptsPackage
    urlFor(new PackageResourceReference(scripts,fileInScriptsPackage))
  }


  def listJavascriptDependencies: Array[String]


}
