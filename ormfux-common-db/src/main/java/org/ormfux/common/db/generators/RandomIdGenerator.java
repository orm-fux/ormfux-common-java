package org.ormfux.common.db.generators;

import java.util.UUID;

/**
 * Creates a random id. See {@link UUID#randomUUID()}.
 */
public class RandomIdGenerator implements IdGenerator {
    
    /** {@inheritDoc} */
    @Override
    public Object generateId() {
        return UUID.randomUUID().toString();
    }
    
}
