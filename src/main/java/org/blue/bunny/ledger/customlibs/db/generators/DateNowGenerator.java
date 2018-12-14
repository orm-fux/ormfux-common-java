package org.blue.bunny.ledger.customlibs.db.generators;

import java.util.Date;

import org.blue.bunny.ledger.customlibs.utils.DateUtils;

public class DateNowGenerator implements ValueGenerator<Date> {

    @Override
    public Date generate(Object previousValue) {
        return DateUtils.now();
    }
}
