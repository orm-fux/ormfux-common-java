package org.ormfux.common.utils.testnullableutils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

import org.junit.Test;
import org.ormfux.common.utils.NullableUtils;

public class IsEmptyTest {
    
    @Test
    public void testPredicateFunctionality() {
        Predicate<Collection<? extends Object>> isEmptyPredicate = NullableUtils.isEmpty();
        assertNotNull(isEmptyPredicate);
        
        assertTrue(isEmptyPredicate.test(new ArrayList<>()));
        assertFalse(isEmptyPredicate.test(Arrays.asList("1")));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullCollectionParam() {
        Predicate<Collection<? extends Object>> isEmptyPredicate = NullableUtils.isEmpty();
        assertNotNull(isEmptyPredicate);
        
        isEmptyPredicate.test(null);
    }
    
}
