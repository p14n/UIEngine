package jhc.figaro.webapps.uiengine

import org.slf4j.LoggerFactory

class ConditionalLogger(logClass:Class[_]) {

	val log = LoggerFactory.getLogger(logClass)

	def ifInfo(f:() => String){
		if(log.isInfoEnabled()){
			log.info(f())
		}
	}
	def ifDebug(f:() => String){
		if(log.isDebugEnabled()){
			log.debug(f())
		}
	}
	def error(text:String,error:Throwable){
		log.error(text,error)
	}
	
}