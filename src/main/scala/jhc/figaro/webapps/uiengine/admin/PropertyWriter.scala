package jhc.figaro.webapps.uiengine.admin

trait PropertyWriter {
  def saveComponentProperty(uitype:String,uikey:String,lang:String,value:String)
}