package org.ormfux.common.utils.testlistutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.ormfux.common.utils.ListUtils;

public class LastTest {
    
    @Test
    public void testNonEmpty() {
        assertEquals("lastValue", ListUtils.last(Arrays.asList("lastValue")));
        assertEquals("3", ListUtils.last(Arrays.asList("lastValue", "secondValue", "3")));
    }
    
    @Test
    public void testEmpty() {
        assertNull(ListUtils.last(Collections.emptyList()));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullSource() {
        assertNull(ListUtils.last(null));
    }
    
}
