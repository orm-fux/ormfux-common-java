package org.ormfux.common.utils.testnullableutils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.function.Predicate;

import org.junit.Test;
import org.ormfux.common.utils.NullableUtils;

public class NotTest {
    
    @Test
    public void testNegated() {
        Predicate<Object> negatedPredicate = NullableUtils.not(val -> true);
        assertNotNull(negatedPredicate);
        assertFalse(negatedPredicate.test(null));
        
        negatedPredicate = NullableUtils.not(val -> false);
        assertNotNull(negatedPredicate);
        assertTrue(negatedPredicate.test(null));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullPredicate() {
        NullableUtils.not(null);
    }
}
