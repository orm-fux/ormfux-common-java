package org.blue.bunny.ledger.customlibs.db.generators;

import java.util.Date;

import org.blue.bunny.ledger.customlibs.utils.DateUtils;

/**
 * A generator that creates a date representing the current date and time.
 * The date is only created when there is not yet a previous value.
 */
public class OnceDateNowGenerator implements ValueGenerator<Date> {
    
    /** {@inheritDoc} */
    @Override
    public Date generate(final Object previousValue) {
        if (previousValue == null) {
            return DateUtils.now();
        } else {
            return (Date) previousValue;
        }
    }
}
