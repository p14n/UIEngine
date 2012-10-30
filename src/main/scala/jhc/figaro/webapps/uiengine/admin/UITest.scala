package jhc.figaro.webapps.uiengine.admin

case class UITest (
 spec:String,
 scriptDependencies:Array[String],
 versions:List[String],
 dataCreator: String => String,
 html:() => String) {
}
