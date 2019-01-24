package org.ormfux.common.db.generators;

/**
 * Interface for generator that create entity ids.
 */
public interface IdGenerator {
    
    /**
     * Generates the id.
     */
    public Object generateId();
    
}
