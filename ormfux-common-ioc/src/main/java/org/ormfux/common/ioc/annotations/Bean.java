package org.ormfux.common.ioc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.ormfux.common.ioc.InjectionContext;

/**
 * Marker for a class, which can by instantiated with {@link InjectionContext}.
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {
    
    /**
     * If the bean is a singleton or created eachnewly each time it is requested.
     * The default is {@code true} (singleton).
     */
    public boolean singleton() default true;
    
}
