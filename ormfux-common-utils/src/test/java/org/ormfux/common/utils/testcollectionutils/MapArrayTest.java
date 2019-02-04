package org.ormfux.common.utils.testcollectionutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.ormfux.common.utils.CollectionUtils;

public class MapArrayTest {
    
    @Test
    public void testMapNonEmpty() {
        Collection<String> result = new ArrayList<>();
        
        CollectionUtils.map(new String[] {"1", "2", "3"}, result, value -> value + "mapped");
        
        assertEquals(3, result.size());
        assertTrue(result.contains("1mapped"));
        assertTrue(result.contains("2mapped"));
        assertTrue(result.contains("3mapped"));
        
        CollectionUtils.map(new String[] {"4", "5", "3"}, result, value -> value + "mapped");
        
        assertEquals(6, result.size());
        assertTrue(result.contains("1mapped"));
        assertTrue(result.contains("2mapped"));
        assertTrue(result.contains("3mapped"));
        assertTrue(result.contains("4mapped"));
        assertTrue(result.contains("5mapped"));
        result.remove("3mapped");
        assertTrue(result.contains("3mapped"));
    }
    
    @Test
    public void testMapEmpty() {
        Collection<String> result = new ArrayList<>();
        
        CollectionUtils.map(new String[0], result, value -> value + "mapped");
        
        assertEquals(0, result.size());
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullSource() {
        Collection<String> result = new ArrayList<>();
        String[] nullArray = null;
        
        CollectionUtils.map(nullArray, result, value -> value + "mapped");
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullResult() {
        CollectionUtils.map(new String[0], null, value -> value + "mapped");
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullMapFunction() {
        CollectionUtils.map(new String[0], new ArrayList<>(), null);
    }
    
}
