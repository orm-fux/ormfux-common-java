package org.ormfux.common.utils.testnullableutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.function.Function;

import org.junit.Test;
import org.ormfux.common.utils.NullableUtils;

public class RetrieveTest {
    
    @Test
    public void testNonNullSourceWithoutFallback() {
        assertNull(NullableUtils.retrieve(Integer.valueOf(3), val -> null));
        assertEquals(Integer.valueOf(6), NullableUtils.retrieve(Integer.valueOf(3), val -> val * 2));
    }
    
    @Test
    public void testNonNullSourceWithFallback() {
        assertNull(NullableUtils.retrieve(Integer.valueOf(3), val -> null, null));
        assertEquals(Integer.valueOf(6), NullableUtils.retrieve(Integer.valueOf(3), val -> val * 2, null));
        
        assertEquals(Integer.valueOf(42), NullableUtils.retrieve(Integer.valueOf(3), val -> null, Integer.valueOf(42)));
        assertEquals(Integer.valueOf(6), NullableUtils.retrieve(Integer.valueOf(3), val -> val * 2, Integer.valueOf(42)));
    }
    
    @Test
    public void testNullSource() {
        assertNull(NullableUtils.retrieve(null, Function.identity()));
        assertNull(NullableUtils.retrieve(null, Function.identity()), null);
        assertEquals("fallback", NullableUtils.retrieve(null, Function.identity(), "fallback"));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullFunctionFallback() {
        NullableUtils.retrieve(null, null, "fallback");
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullFunctionNullFallback() {
        NullableUtils.retrieve(null, null, null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullFunctionNoFallback() {
        NullableUtils.retrieve(null, null);
    }
    
}
