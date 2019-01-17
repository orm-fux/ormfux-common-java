package org.blue.bunny.common.db.generators;

import java.util.Date;

import org.blue.bunny.common.utils.DateUtils;

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
