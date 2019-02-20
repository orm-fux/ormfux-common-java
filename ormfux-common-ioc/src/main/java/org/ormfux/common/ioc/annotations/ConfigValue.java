package org.ormfux.common.ioc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for a bean constructor parameter, for which the value should
 * be loaded from a config value set.
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigValue {
    
    /**
     * The config value key.
     */
    String key() default "";
    
    /**
     * Fallback key definition.
     */
    String value() default "";
    
    /**
     * The set in which the value is defined. When not provided the value will be looked up 
     * in all defined sets and system properties.
     */
    String set() default "";
}
