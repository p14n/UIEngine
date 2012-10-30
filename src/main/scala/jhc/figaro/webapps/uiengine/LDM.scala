package jhc.figaro.webapps.uiengine
import org.apache.wicket.model.IModel
import org.apache.wicket.model.LoadableDetachableModel

object LDM {
   def of[T](objectFunction:() => T):IModel[T] = {
    new LoadableDetachableModel[T]() {
      override def load:T = {
      	val result = objectFunction()
      	result
      }
     }
   }
}
