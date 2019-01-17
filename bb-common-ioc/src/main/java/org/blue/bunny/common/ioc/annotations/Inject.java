package org.blue.bunny.common.ioc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.blue.bunny.common.ioc.InjectionContext;

/**
 * Marker for a property of a {@link Bean} where the property value can be injected during 
 * bean instantiation with the {@link InjectionContext}.
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {

}
