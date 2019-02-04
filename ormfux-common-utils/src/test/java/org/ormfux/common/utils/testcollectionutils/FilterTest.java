package org.ormfux.common.utils.testcollectionutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.Test;
import org.ormfux.common.utils.CollectionUtils;

public class FilterTest {
    
    @Test
    public void testFilterWithMatch() {
        List<Integer> result = new ArrayList<>();
        CollectionUtils.filter(Arrays.asList(1, 2, 3), result, value -> value == 1 || value == 2);
        
        assertEquals(2, result.size());
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
        
        CollectionUtils.filter(Arrays.asList(1, 2, 3), result, value -> value == 1 || value == 2);
        
        assertEquals(4, result.size());
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
        result.remove(Integer.valueOf(1));
        result.remove(Integer.valueOf(2));
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
    }
    
    @Test
    public void testWithoutMatch() {
        List<Integer> result = new ArrayList<>();
        CollectionUtils.filter(Arrays.asList(1, 2, 3), result, value -> value == 4);
        
        assertEquals(0, result.size());
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullFilteredCollection() {
        CollectionUtils.filter(null, new ArrayList<>(), value -> Objects.equals("4", value));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullResultCollection() {
        CollectionUtils.filter(new ArrayList<>(), null, value -> Objects.equals("4", value));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullPredicate() {
        CollectionUtils.filter(new ArrayList<>(), new ArrayList<>(), null);
    }
    
}
