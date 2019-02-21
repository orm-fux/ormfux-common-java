package org.ormfux.common.ioc.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker for a constructor of a {@link Bean}. The marked constructor will be used to create 
 * the bean instance. The parameters of the constructor are instantiated as beans as well or 
 * loaded from configuration value sets.
 *
 */
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BeanConstructor {

}
