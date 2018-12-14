package org.blue.bunny.ledger.customlibs.db.generators;

public interface ValueGenerator<T> {
    
    public T generate(final Object previousValue);
    
    
}
