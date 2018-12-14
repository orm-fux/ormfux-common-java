package org.blue.bunny.ledger.customlibs.db.generators;

public class LongIncrementGenerator implements ValueGenerator<Long> {

    @Override
    public Long generate(final Object previousValue) {
        if (previousValue == null) {
            return 0L;
        } else {
            return ((Long) previousValue) + 1;
        }
    }
    
}
