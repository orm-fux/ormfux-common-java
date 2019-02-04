package org.ormfux.common.utils.testnullableutils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.Test;
import org.ormfux.common.utils.NullableUtils;

public class CheckTest {
    
    @Test
    public void testBooleanObject() {
        assertFalse(NullableUtils.check(null));
        assertFalse(NullableUtils.check(Boolean.FALSE));
        assertTrue(NullableUtils.check(Boolean.TRUE));
    }
    
    @Test
    public void testFallbackOnly() {
        assertTrue(NullableUtils.check(new Object(), () -> true));
        assertFalse(NullableUtils.check(new Object(), () -> false));
        
        assertFalse(NullableUtils.check(null, () -> true));
        assertFalse(NullableUtils.check(null, () -> false));
    }
    
    @Test
    public void testNullSourceNoFallback() {
        assertFalse(NullableUtils.check(null, val -> true));
        assertFalse(NullableUtils.check(null, val -> false));
        
        assertFalse(NullableUtils.check(null, Function.identity(), val -> true));
        assertFalse(NullableUtils.check(null, Function.identity(), val -> false));
    }
    
    @Test
    public void testNullSourceFallback() {
        assertTrue(NullableUtils.check(null, val -> true, true));
        assertFalse(NullableUtils.check(null, val -> true, false));
        assertTrue(NullableUtils.check(null, val -> false, true));
        assertFalse(NullableUtils.check(null, val -> false, false));
        
        assertTrue(NullableUtils.check(null, Function.identity(), val -> true, true));
        assertFalse(NullableUtils.check(null, Function.identity(), val -> true, false));
        assertTrue(NullableUtils.check(null, Function.identity(), val -> false, true));
        assertFalse(NullableUtils.check(null, Function.identity(), val -> false, false));
    }
    
    @Test
    public void testNonNullSourceNoFallback() {
        assertTrue(NullableUtils.check("test", val -> "test".equals(val)));
        assertFalse(NullableUtils.check("test", val -> !"test".equals(val)));
        
        assertTrue(NullableUtils.check("", source -> "test", val -> "test".equals(val)));
        assertFalse(NullableUtils.check("", source -> "test", val -> !"test".equals(val)));
    }
    
    @Test
    public void testNonNullSourceFallback() {
        assertTrue(NullableUtils.check("test", val -> "test".equals(val), true));
        assertTrue(NullableUtils.check("test", val -> "test".equals(val), false));
        assertFalse(NullableUtils.check("test", val -> !"test".equals(val), true));
        assertFalse(NullableUtils.check("test", val -> !"test".equals(val), false));
        
        assertTrue(NullableUtils.check("", source -> "test", val -> "test".equals(val), true));
        assertTrue(NullableUtils.check("", source -> "test", val -> "test".equals(val), false));
        assertFalse(NullableUtils.check("", source -> "test", val -> !"test".equals(val), true));
        assertFalse(NullableUtils.check("", source -> "test", val -> !"test".equals(val), false));
    }
    
    @Test
    public void testNonNullSourceRetrieveNullFallback() {
        assertTrue(NullableUtils.check("", source -> null, val -> "test".equals(val), true));
        assertFalse(NullableUtils.check("", source -> null, val -> "test".equals(val), false));
        assertTrue(NullableUtils.check("", source -> null, val -> !"test".equals(val), true));
        assertFalse(NullableUtils.check("", source -> null, val -> !"test".equals(val), false));
    }
    
    @Test
    public void testNonNullSourceRetrieveNullNoFallback() {
        assertFalse(NullableUtils.check("", source -> null, val -> "test".equals(val)));
        assertFalse(NullableUtils.check("", source -> null, val -> !"test".equals(val)));
    }
    @Test(expected = NullPointerException.class)
    public void testNullPredicate() {
        Predicate<?> nullPredicate = null;
        NullableUtils.check(null, nullPredicate);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullPredicateFallback() {
        NullableUtils.check(null, null, true);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullPredicateRetriever() {
        NullableUtils.check(null, Function.identity(), null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullPredicateRetrieverFallback() {
        NullableUtils.check(null, Function.identity(), null, true);
    }
    
}
