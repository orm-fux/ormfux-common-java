package org.blue.bunny.ledger.customlibs.db.generators;

import java.util.Date;

import org.blue.bunny.ledger.customlibs.utils.DateUtils;

/**
 * A generator that creates a date representing the current date and time.
 */
public class DateNowGenerator implements ValueGenerator<Date> {
    
    /** {@inheritDoc} */
    @Override
    public Date generate(Object previousValue) {
        return DateUtils.now();
    }
}
