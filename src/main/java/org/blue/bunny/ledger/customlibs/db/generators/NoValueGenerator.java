package org.blue.bunny.ledger.customlibs.db.generators;

public final class NoValueGenerator implements ValueGenerator<Void> {

    @Override
    public Void generate(Object previousValue) {
        throw new UnsupportedOperationException();
    }
    
}
