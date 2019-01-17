package org.blue.bunny.ledger.customlibs.ioc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for public, parameterless, non-static methods that are invoked when
 * a {@link Bean} instance is created. Invocations are top-down in the inheritance hierarchy.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Init {
    
}
