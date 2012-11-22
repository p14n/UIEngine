package jhc.figaro.webapps.uiengine

import java.util.concurrent.atomic.AtomicLong

object RequestInfo {

	val RESOLVE_CONTENT = "cont"
	val CREATE_COMPONENT =  "comp"
	
	private val thread = new ThreadLocal[RequestInfo]();

	def get():RequestInfo = {
		thread.get()
	}
	def renew():RequestInfo = {
		val info = new RequestInfo()
		thread.set(info)
		info
	}
}
class RequestInfo {

	private val begin = System.nanoTime()
	private val map:Map[String,AtomicLong] = Map(
		RequestInfo.RESOLVE_CONTENT -> new AtomicLong(0),
		RequestInfo.CREATE_COMPONENT -> new AtomicLong(0))

	def addTimeSpent(start:Long,on:String) : Long = {
		map.get(on).getOrElse(null).addAndGet(System.nanoTime()-start)
	}
	def timeSpent(on:String):Long = {
		map.get(on).getOrElse(new AtomicLong(0)).get()
	}
	def totalTime():Long = {
		System.nanoTime()-begin
	}
}