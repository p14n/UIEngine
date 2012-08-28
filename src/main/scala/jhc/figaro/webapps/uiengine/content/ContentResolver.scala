package jhc.figaro.webapps.uiengine.content

abstract class ContentResolver {

  def resolve(path: String): Content

}
