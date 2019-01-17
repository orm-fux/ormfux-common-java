package org.blue.bunny.common.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValueGenerator {
    
    public Class<? extends org.blue.bunny.common.db.generators.ValueGenerator<?>> value();
    
}
