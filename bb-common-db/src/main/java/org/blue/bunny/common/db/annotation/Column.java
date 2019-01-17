package org.blue.bunny.common.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.blue.bunny.common.db.generators.NoValueGenerator;
import org.blue.bunny.common.db.generators.ValueGenerator;

/**
 * Annotation for a field that holds the value of a column.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    
    /**
     * The name of the column for which to map the value to the annotated property.
     */
    public String columnName();
    
    /**
     * Label for queries.
     */
    public String columnLabel();
    
    /**
     * Optional generator type for automatic value creation.
     */
    public Class<? extends ValueGenerator<?>> generator() default NoValueGenerator.class;
    
}
