package jhc.figaro.webapps.uiengine;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to define a dynamic component available to add to the page
 * @author dean
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.FIELD})
@Inherited
public @interface UIComponent {
	public String name();
	public String description();
}