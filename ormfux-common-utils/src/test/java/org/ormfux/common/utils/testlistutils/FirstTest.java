package org.ormfux.common.utils.testlistutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.ormfux.common.utils.ListUtils;

public class FirstTest {
    
    @Test
    public void testNonEmpty() {
        assertEquals("firstValue", ListUtils.first(Arrays.asList("firstValue")));
        assertEquals("firstValue", ListUtils.first(Arrays.asList("firstValue", "secondValue", "3")));
    }
    
    @Test
    public void testEmpty() {
        assertNull(ListUtils.first(Collections.emptyList()));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullSource() {
        assertNull(ListUtils.first(null));
    }
    
}
