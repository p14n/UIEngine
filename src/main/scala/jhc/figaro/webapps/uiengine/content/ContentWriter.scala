package jhc.figaro.webapps.uiengine.content

trait ContentWriter {
  def addContent(content:Content)
  def updateContent(content:Content):Boolean
  def putContent(content:Content)
}
