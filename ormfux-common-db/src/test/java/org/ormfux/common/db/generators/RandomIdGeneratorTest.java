package org.ormfux.common.db.generators;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RandomIdGeneratorTest {
    
    @Test
    public void testGenerate() {
        Object id = new RandomIdGenerator().generateId();
        assertNotNull(id);
        assertTrue(id instanceof String);
        assertTrue(id.toString().matches("[a-f0-9]{8}\\-([a-f0-9]{4}\\-){3}[a-f0-9]{12}"));
    }
    
}
