package org.blue.bunny.ledger.customlibs.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for classes marking them as entities.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
    
    /**
     * The name of the database tabale, which holds the entity information.
     */
    public String table();
    
}
