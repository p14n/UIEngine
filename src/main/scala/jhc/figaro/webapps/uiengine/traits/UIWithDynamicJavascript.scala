package jhc.figaro.webapps.uiengine.traits

trait UIComponentWithDynamicJavascript {
    def createDynamicJavascript(id: String): String
}
