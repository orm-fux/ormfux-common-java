package org.ormfux.common.utils.testlistutils;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.ormfux.common.utils.ListUtils;

public class MaxIndexTest {
    
    @Test
    public void testNonEmpty() {
        assertEquals(2, ListUtils.maxIndex(Arrays.asList("1", "2", "3")));
        assertEquals(0, ListUtils.maxIndex(Arrays.asList("1")));
    }
    
    @Test
    public void testEmpty() {
        assertEquals(-1, ListUtils.maxIndex(new ArrayList<>()));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullSource() {
        ListUtils.maxIndex(null);
    }
    
}
