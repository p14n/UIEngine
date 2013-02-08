package jhc.figaro.webapps.uiengine
import com.orientechnologies.orient.core.db.graph.OGraphDatabase
import com.orientechnologies.orient.core.exception.OStorageException
import com.orientechnologies.orient.server.OServer
import com.orientechnologies.orient.server.OServerMain
import java.io.File
import com.orientechnologies.orient.core.metadata.schema.OSchema
import jhc.figaro.webapps.uiengine.admin.db.DBService

class OrientDBServer {

  var server:OServer=null
  var db:OGraphDatabase=null

  def startup(configLocation:String,dbLocation:String,dbUser:String,dbPass:String){
    server = OServerMain.create()
    if(configLocation!=null){
      server.startup(new File(configLocation))
    } else {
      server.startup(classOf[OrientDBServer].getResourceAsStream("/orient.db.xml"))
    }
    server.activate
    db = new OGraphDatabase("local:"+dbLocation)
    try{ 
      db.open(dbUser,dbPass)
    } catch {
      case e:OStorageException => {
       db.create()
      }
    }
    addClasses(db)
    DBFunctions.doWithConnection(new DBService().getCurrentVersion)
  }
  def addClasses(db:OGraphDatabase){
    val schema = db.getMetadata().getSchema()
    List("Content","ContentSource","ContentVersion","ComponentProperty").foreach( n => {
      if(!schema.existsClass(n)) db.createVertexType(n)
    })
  }

  def shutdown(){
    db.close()
    server.shutdown()
  }

}
