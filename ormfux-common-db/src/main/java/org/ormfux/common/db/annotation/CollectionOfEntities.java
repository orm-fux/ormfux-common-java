package org.ormfux.common.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotations for fields that represent a collection of entities. The field must be 
 * of a sub-type of {@code Collection}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CollectionOfEntities {
    
    /**
     * The table holding the "association" information.
     */
    public String joinTable() default "";
    
    /**
     * The column in the join table with the id of the entity that
     * has the property with this annotation.
     */
    public String joinColumn();
    
    /**
     * The column in the join table with the ids of the referenced
     * entities.
     */
    public String inverseJoinColumn() default "";
    
}
