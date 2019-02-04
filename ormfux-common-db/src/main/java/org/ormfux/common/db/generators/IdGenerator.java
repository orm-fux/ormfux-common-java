package org.ormfux.common.db.generators;

/**
 * Interface for generator that create entity ids.
 */
@FunctionalInterface
public interface IdGenerator {
    
    /**
     * Generates the id.
     */
    public Object generateId();
    
}
