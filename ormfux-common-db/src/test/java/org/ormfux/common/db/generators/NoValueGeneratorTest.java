package org.ormfux.common.db.generators;

import org.junit.Test;

public class NoValueGeneratorTest {
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGenerate() {
        new NoValueGenerator().generate(null);
    }
}
