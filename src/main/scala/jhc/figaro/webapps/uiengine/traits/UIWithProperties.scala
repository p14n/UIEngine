package jhc.figaro.webapps.uiengine.traits

import jhc.figaro.webapps.uiengine.admin.ComponentProperty

trait UIWithProperties {
	def getProperties:List[ComponentProperty]

	def prop(uikey:String,default:String,desc:String,lang:String):ComponentProperty = {
		new ComponentProperty(uikey,default,desc,lang);
	}
	def prop(uikey:String,default:String,desc:String):ComponentProperty = {
		prop(uikey,default,desc,null);
	}

}