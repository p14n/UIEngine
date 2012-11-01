package jhc.figaro.webapps.uiengine

import jhc.figaro.webapps.uiengine.admin.db.DBService

object ServiceLocator {

	val dbService:DBService = new DBService()
	def db:DBService = {dbService}
	
}
