package jhc.figaro.webapps.uiengine

import com.orientechnologies.orient.core.db.graph.OGraphDatabasePool

object DBFunctions {
	def doWithConnection(f:()=>Unit) = {
      val p = PropertyStore
      val database = OGraphDatabasePool.global().acquire("local:"+
       p.get("db.path"),p.get("db.user"),p.get("db.pass"));
      try{
        f()
      } finally {
        database.close();
      }
	}
	def closeGlobal {
      OGraphDatabasePool.global().close();
	}
}