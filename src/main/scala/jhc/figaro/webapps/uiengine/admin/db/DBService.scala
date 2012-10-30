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

class DBService extends ContentWriter {

  def conn():OGraphDatabase = {
    new OGraphDatabase (ODatabaseRecordThreadLocal.INSTANCE
			.get().asInstanceOf[ODatabaseRecordTx]);
  }
  def qry(sql:String):List[ODocument] = {
    List(conn().query(new OSQLSynchQuery[ODocument](sql)))
  }

  def getContentSources():List[ContentSource] = {
    qry("select from ContentSource").map(c => {
      val feeders = c.field("feeders")
      new ContentSource(c.field("rootUrl"),
	     (if(feeders!=null) (feeders.toString().split(",").toList) else null))
    })
  }

  def addContentSource(c:ContentSource){
    val doc = conn().createVertex("ContentSource");
    doc.field("rootUrl",c.rootUrl)
    doc.field("feeders",if(c.feeders==null) null else c.feeders.mkString(","))
    doc.save()
  }
  def updateContentSource(cs:ContentSource){
    qry("select from ContentSource where rootUrl='"+cs.rootUrl+"'").foreach(c => {
      c.field("feeders",if(cs.feeders==null) null else cs.feeders.mkString(","))
      c.save()
    })
  }
  def getCurrentVersion():Int = {
    qry("select from ContentVersion where current='true'").foreach(c => {
      return c.field("versionNumber").toString().toInt
    })
    newVersion()
  }
  def newVersion():Int = {
    var currentVersionNumber = 0
    qry("select from ContentVersion where current='true'").foreach(c => {
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
    val record = new ORecordBytes(conn(), content.content);
    doc.field("content",record)
    doc.save()
    qry("select from ContentVersion where current='true'").foreach(c => {
      val edge = conn.createEdge(doc,c)
      edge.field("type","belongsto")
      edge.save()
    })
  }
  def updateContent(content:Content):Boolean = {
    qry("select from Content where in[@type='belongsto'].out.current='true' "+
  "and path='"+content.path+"'").foreach(doc => {
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
    qry("select from Content where in[@type='belongsto'].out.current='true' ")
    .map(doc => {
      val record:ORecordBytes = doc.field("content");
      new Content(doc.field("path"),doc.field("contentType"),
        doc.field("status"),doc.field("charset"),doc.field("role"),
        record.toStream())
    })
  }
  def putContent(content:Content) {
    if(!updateContent(content)) {
      addContent(content)
    }
  }
}
