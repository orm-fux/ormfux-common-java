package org.blue.bunny.ledger.customlibs.db.generators;

/**
 * Interface for generator that create entity ids.
 */
public interface IdGenerator {
    
    /**
     * Generates the id.
     */
    public Object generateId();
    
}
