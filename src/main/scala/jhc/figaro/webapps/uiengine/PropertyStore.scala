package jhc.figaro.webapps.uiengine
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.util.Properties

object PropertyStore {

  val props: Properties = new Properties()
  var filename:String = null

  def add(fileName:String):Boolean = {
    if(fileName == null) return false
    val file = new File(fileName)
    filename = fileName
    if(!file.exists) return true
    val is = new FileInputStream(file)
    props.load(is)
    is.close()
    println("Properties loaded from "+fileName)
    props.list(System.out)
    true
  }
  def addOrDefault(file:String){
    if(!add(file)) add("./site.properties")
  }
  def get(name:String):String = { props.getProperty(name) }
  def get(name:String, default:String):String = { 
    if(!props.containsKey(name)) {
      this.synchronized {
	props.put(name,default)
	props.store(new FileWriter(filename),"UTF-8");
      }
    }
    props.getProperty(name)
  }
  def has(name:String):Boolean = { props.containsKey(name) }
}
