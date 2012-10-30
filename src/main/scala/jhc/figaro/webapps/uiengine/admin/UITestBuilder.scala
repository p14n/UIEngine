package jhc.figaro.webapps.uiengine.admin

class UITestBuilder {

  var scriptDependencies:Array[String] = null
  var versions:List[String] = null
  var dataCreator: String => String = null
  var spec: String = null
  var html: () => String = null

  def withDependencies(deps:Array[String]):UITestBuilder = {
    scriptDependencies = deps
    this
  }
  def withSpec(specification: String):UITestBuilder = {
    spec = specification
    this
  }
  def withAllVersions:UITestBuilder = {
    versions = List("v1","v2","v3")
    this
  }
  def except(version: String):UITestBuilder = {
    versions -= version
    this
  }
  def withData(data:(String) => String):UITestBuilder = {
    dataCreator = data
    this
  }
  def withHtml(h:() => String):UITestBuilder = {
    html = h
    this
  }
  def build(): UITest = {
    new UITest(spec,scriptDependencies,versions,dataCreator,html)
  }

}
