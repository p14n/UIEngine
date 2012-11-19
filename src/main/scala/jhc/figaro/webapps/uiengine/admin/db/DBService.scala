package jhc.figaro.webapps.uiengine.admin.db
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal
import com.orientechnologies.orient.core.db.graph.OGraphDatabase
import com.orientechnologies.orient.core.db.record.ODatabaseRecord
import com.orientechnologies.orient.core.db.record.ODatabaseRecordTx
import com.orientechnologies.orient.core.record.impl.ODocument
import com.orientechnologies.orient.core.record.impl.ORecordBytes
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery
import jhc.figaro.webapps.uiengine.content.Content
import jhc.figaro.webapps.uiengine.content.ContentWriter
import jhc.figaro.webapps.uiengine.content.ContentSource
import scala.collection.JavaConversions._
import org.slf4j.LoggerFactory
import java.util.ArrayList

class DBService extends ContentWriter {

  val log = LoggerFactory.getLogger(classOf[DBService])

  def conn():OGraphDatabase = {
    new OGraphDatabase (ODatabaseRecordThreadLocal.INSTANCE
			.get().asInstanceOf[ODatabaseRecordTx]);
  }
  def qry(sql:String):List[ODocument] = {
    try {
      val results:ArrayList[ODocument] = conn().query(new OSQLSynchQuery[ODocument](sql));
      if(results!=null) return results.toList
    } catch {
      case e:Throwable => log.error("failed to execute query "+sql, 
        e)
    }
    List[ODocument]()
  }

  def getContentSources():List[ContentSource] = {
    qry("select from ContentSource").map((c:ODocument) => {

      val feeders:String = c.field("feeders")
      val splitFeeders = (
        if( feeders == null || feeders.length() == 0 ) null 
        else (feeders.toString().split(",").toList))

      new ContentSource(c.field("rootUrl"),splitFeeders)
    })
  }

  def addContentSource(c:ContentSource){
    val doc = conn().createVertex("ContentSource");
    doc.field("rootUrl",c.rootUrl)
    doc.field("feeders",if(c.feeders==null) null else c.feeders.mkString(","))
    doc.save()
  }
  def updateContentSource(cs:ContentSource){
    qry("select from ContentSource where rootUrl = '"+cs.rootUrl+"'").foreach(c => {
      c.field("feeders",if(cs.feeders==null) null else cs.feeders.mkString(","))
      c.save()
    })
  }
  def deleteContentSource(rootPath:String){
    qry("select from ContentSource where rootUrl = '"+rootPath+"'").foreach(c => {
      conn().removeVertex(c)
    })
  }
  def getCurrentVersion():Int = {
    qry("select from ContentVersion where current = 'true'").foreach(c => {
      return c.field("versionNumber").toString().toInt
    })
    newVersion()
  }
  def newVersion():Int = {
    var currentVersionNumber = 0
    qry("select from ContentVersion where current = 'true'").foreach(c => {
      currentVersionNumber = c.field("versionNumber").toString().toInt
      c.field("current","false")
      c.save()
    })
    val doc = conn().createVertex("ContentVersion")
    doc.field("versionNumber",currentVersionNumber+1)
    doc.field("current","true")
    doc.save()
    currentVersionNumber+1
  }
  def addContent(content:Content){
    val doc = conn().createVertex("Content")
    doc.field("path",content.path)
    doc.field("role",content.role)
    doc.field("status",content.status)
    doc.field("charset",content.charset)
    doc.field("contentType",content.contentType)
    if(content.content!=null){
      val record = new ORecordBytes(conn(), content.content);
      doc.field("content",record)
    }
    doc.save()
    qry("select from ContentVersion where current = 'true'").foreach(c => {
      val edge = conn.createEdge(doc,c)
      edge.field("type","belongsto")
      edge.save()
    })
  }
  def updateContent(content:Content):Boolean = {
    val debug = log.isDebugEnabled()
    if(debug)
      log.debug("Saving content for path "+content.path)
    qry("select from Content where out[type = 'belongsto'].in.current = 'true' "+
  "and path = '"+content.path+"'").foreach(doc => {
      if(debug) {
        log.debug("Content "+content.path+" saved for current version")
        log.debug(content.toString())
      }
      doc.field("role",content.role)
      doc.field("status",content.status)
      doc.field("charset",content.charset)
      doc.field("contentType",content.contentType)
      val record = new ORecordBytes(conn(), content.content);
      doc.field("content",record)
      doc.save()
      return true
    })
    false
  }
  def getContent():List[Content] = {
    qry("select from Content where out[type = 'belongsto'].in.current = 'true' order by path")
    .map(doc => {
      val record:ORecordBytes = doc.field("content");
      new Content(doc.field("path"),doc.field("contentType"),
        doc.field("status"),doc.field("charset"),doc.field("role"),
        if(record!=null)record.toStream() else null)
    })
  }
  def putContent(content:Content) {
    if(!updateContent(content)) {
      addContent(content)
    }
  }
}
