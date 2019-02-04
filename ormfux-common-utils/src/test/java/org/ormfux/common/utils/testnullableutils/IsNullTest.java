package org.ormfux.common.utils.testnullableutils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.ormfux.common.utils.NullableUtils;

public class IsNullTest {
    
    @Test
    public void testSimpleNull() {
        assertTrue(NullableUtils.isNull(null));
    }
    
    @Test
    public void testSimpleNotNull() {
        assertFalse(NullableUtils.isNull(new Object()));
    }
    
    @Test
    public void testNullSourceObject() {
        assertTrue(NullableUtils.isNull(null, val -> new Object()));
        assertTrue(NullableUtils.isNull(null, val -> null));
    }
    
    @Test
    public void testNonNullSourceObject() {
        assertFalse(NullableUtils.isNull(new Object(), val -> new Object()));
        assertTrue(NullableUtils.isNull(new Object(), val -> null));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullFunction() {
        NullableUtils.isNull(null, null);
    }
    
}
