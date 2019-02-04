package org.ormfux.common.utils.testlistutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.ormfux.common.utils.ListUtils;

public class MapTest {
    
    @Test
    public void testMapNonEmpty() {
        List<String> result = ListUtils.map(Arrays.asList("1", "2", "3"), value -> value + "mapped");
        
        assertEquals(3, result.size());
        assertTrue(result.contains("1mapped"));
        assertTrue(result.contains("2mapped"));
        assertTrue(result.contains("3mapped"));
    }
    
    @Test
    public void testMapEmpty() {
        List<String> result = ListUtils.map(new ArrayList<>(), value -> value + "mapped");
        
        assertEquals(0, result.size());
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullSource() {
        ListUtils.map(null, value -> value + "mapped");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullMapFunction() {
        ListUtils.map(new ArrayList<>(), null);
    }
    
}
