package org.blue.bunny.ledger.customlibs.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValueGenerator {
    
    public Class<? extends org.blue.bunny.ledger.customlibs.db.generators.ValueGenerator<?>> value();
    
}
