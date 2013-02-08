package jhc.figaro.webapps.uiengine.admin

trait PropertyReader {
  def getComponentProperties(uitype:String,lang:String):Map[String,String]
}