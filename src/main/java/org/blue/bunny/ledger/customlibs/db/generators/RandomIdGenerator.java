package org.blue.bunny.ledger.customlibs.db.generators;

import java.util.UUID;

public class RandomIdGenerator implements IdGenerator {

    @Override
    public Object generateId() {
        return UUID.randomUUID().toString();
    }
    
}
