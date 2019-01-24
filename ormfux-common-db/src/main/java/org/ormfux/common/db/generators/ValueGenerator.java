package org.ormfux.common.db.generators;

/**
 * INterface that are used for automatic generation of values of entities
 * when their changes are persisted.
 *
 * @param <T> The type of generated value.
 */
public interface ValueGenerator<T> {
    
    /**
     * Generates the value.
     * 
     * @param previousValue The current value of the field.
     * @return The generated value.
     */
    public T generate(final Object previousValue);
    
    
}
