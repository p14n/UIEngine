package jhc.figaro.webapps.uiengine

import org.apache.wicket.markup.resolver.IComponentResolver
import org.apache.wicket.Component
import org.apache.wicket.MarkupContainer
import org.apache.wicket.markup.MarkupStream
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.parser.filter.WicketTagIdentifier
import org.apache.wicket.markup.WicketTag

class UIComponentResolver(componentCreator: (String,String) => Component)
  extends IComponentResolver {

  WicketTagIdentifier.registerWellKnownTagName("ui")

  def resolve(container: MarkupContainer,markupstream: 
	      MarkupStream,tag: ComponentTag): Component = {

    if(tag.isInstanceOf[WicketTag]){
       val id = tag.getAttribute("id")
       val uitype = tag.getAttribute("type")
       return componentCreator(uitype,id)
    }
    null
  }

}
