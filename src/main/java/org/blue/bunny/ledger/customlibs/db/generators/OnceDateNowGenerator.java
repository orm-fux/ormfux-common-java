package org.blue.bunny.ledger.customlibs.db.generators;

import java.util.Date;

import org.blue.bunny.ledger.customlibs.utils.DateUtils;

public class OnceDateNowGenerator implements ValueGenerator<Date> {

    @Override
    public Date generate(Object previousValue) {
        if (previousValue == null) {
            return DateUtils.now();
        } else {
            return (Date) previousValue;
        }
    }
}
