package org.ormfux.common.utils.testcollectionutils;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.ormfux.common.utils.CollectionUtils;

public class MapToArrayTest {
    
    @Test
    public void testMapNonEmpty() {
        Object[] result = CollectionUtils.mapToArray(Arrays.asList("1", "2", "3"), value -> value + "mapped");
        
        assertEquals(3, result.length);
        assertEquals("1mapped", result[0]);
        assertEquals("2mapped", result[1]);
        assertEquals("3mapped", result[2]);
    }
    
    @Test
    public void testMapEmpty() {
        Object[] result = CollectionUtils.mapToArray(new ArrayList<>(), value -> value + "mapped");
        
        assertEquals(0, result.length);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullSource() {
        CollectionUtils.mapToArray(null, value -> value + "mapped");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullMapFunction() {
        CollectionUtils.mapToArray(Arrays.asList("1", "2", "3"), null);
    }
    
}
