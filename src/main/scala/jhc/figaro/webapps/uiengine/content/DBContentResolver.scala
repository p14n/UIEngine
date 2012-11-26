package jhc.figaro.webapps.uiengine.content

class DBContentResolver(reader:ContentReader) extends ContentResolver {

	  def resolve(path: String): Content = {
	  	reader.getContent(path)
	  }

}