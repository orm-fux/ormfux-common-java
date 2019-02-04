package org.ormfux.common.utils.testnullableutils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.function.Predicate;

import org.junit.Test;
import org.ormfux.common.utils.NullableUtils;

public class EqualToTest {
    
    @Test
    public void testPredicateFunctionality() {
        Predicate<String> predicate = NullableUtils.equalTo("123");
        assertNotNull(predicate);
        
        assertTrue(predicate.test("123"));
        assertFalse(predicate.test("other"));
        assertFalse(predicate.test(null));
        
        predicate = NullableUtils.equalTo(null);
        assertNotNull(predicate);
        
        assertFalse(predicate.test("123"));
        assertFalse(predicate.test("other"));
        assertTrue(predicate.test(null));
        
    }
    
}
