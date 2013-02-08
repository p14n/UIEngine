package jhc.figaro.webapps.uiengine.admin.db
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal
import com.orientechnologies.orient.core.db.graph.OGraphDatabase
import com.orientechnologies.orient.core.db.record.{ODatabaseRecord,ODatabaseRecordTx}
import com.orientechnologies.orient.core.record.impl.{ODocument,ORecordBytes}
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery
import jhc.figaro.webapps.uiengine.content.{Content,ContentWriter,ContentSource}
import jhc.figaro.webapps.uiengine.ConditionalLogger
import scala.collection.JavaConversions._
import org.slf4j.LoggerFactory
import java.util.ArrayList
import jhc.figaro.webapps.uiengine.content.ContentReader
import jhc.figaro.webapps.uiengine.admin.{PropertyWriter,PropertyReader}

class DBService extends ContentWriter with ContentReader with PropertyReader with PropertyWriter {

  val log = new ConditionalLogger(classOf[DBService])

  def conn():OGraphDatabase = {
    new OGraphDatabase (ODatabaseRecordThreadLocal.INSTANCE
			.get().asInstanceOf[ODatabaseRecordTx]);
  }
  def qry(sql:String):List[ODocument] = {
    log.ifDebug(()=>{"Exec query "+sql})
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
      log.ifDebug(()=>{"Saving content for path "+content.path})
    qry("select from Content where out[type = 'belongsto'].in.current = 'true' "+
  "and path = '"+content.path+"'").foreach(doc => {
       log.ifDebug(()=>{"Content "+content.path+" saved for current version"})
       log.ifDebug(()=>{content.toString()})

      doc.field("role",content.role)
      doc.field("status",content.status)
      doc.field("charset",content.charset)
      doc.field("contentType",content.contentType)
      if(content.content!=null){
        val record = new ORecordBytes(conn(), content.content);
        doc.field("content",record)
      }
      doc.save()
      return true
    })
    false
  }
  def getContent(path:String):Content = {
    log.ifDebug(() => {"Searching content database for "+path})
    qry("select from Content where out[type = 'belongsto'].in.current = 'true' "+
      "and path = '"+path+"'").foreach(doc => {
        return contentFromDocument(doc)
      })
      null
  }
  def contentFromDocument(doc:ODocument):Content = {
    val record:ORecordBytes = doc.field("content");
    new Content(doc.field("path"),doc.field("contentType"),
      doc.field("status"),doc.field("charset"),doc.field("role"),
      if(record!=null)record.toStream() else null)

  }
  def saveComponentProperty(uitype:String,uikey:String,lang:String,value:String){
    qry("select from ComponentProperty where out[type = 'belongsto'].in.current = 'true' "+
      "and uitype = '"+uitype+"' and uikey = '"+uikey+"' "+(if(lang==null)"" else "lang='"+lang+"' ")).foreach(doc => {
        doc.field("value",value)
        doc.save()
        return;
    })
    val doc = conn().createVertex("ComponentProperty")
    doc.field("uitype",uitype)
    doc.field("uikey",uikey)
    if(lang!=null)doc.field("lang",lang)
    doc.field("value",value)
    doc.save()
    qry("select from ContentVersion where current = 'true'").foreach(c => {
      val edge = conn.createEdge(doc,c)
      edge.field("type","belongsto")
      edge.save()
    })
  }
  def getComponentProperties(uitype:String,lang:String):Map[String,String] = {
    qry("select from ComponentProperty where out[type = 'belongsto'].in.current = 'true' "+
      "and uitype = '"+uitype+"' and (lang = '"+lang+"' or lang is null)").map(
      (c:ODocument) => (c.field("uikey"),c.field("value"))
    ).toMap
  }
  def getContent():List[Content] = {
    qry("select from Content where out[type = 'belongsto'].in.current = 'true' order by path")
    .map(doc => {
      contentFromDocument(doc)
    })
  }
  def putContent(content:Content) {
    if(!updateContent(content)) {
      addContent(content)
    }
  }
}
