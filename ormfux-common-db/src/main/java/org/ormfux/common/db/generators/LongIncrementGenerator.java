package org.ormfux.common.db.generators;

/**
 * A generator that increments a value of type "long" by {@code 1}.
 */
public class LongIncrementGenerator implements ValueGenerator<Long> {
    
    /** {@inheritDoc} */
    @Override
    public Long generate(final Object previousValue) {
        if (previousValue == null) {
            return 0L;
        } else {
            return ((Long) previousValue) + 1;
        }
    }
    
}
