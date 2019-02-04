package org.ormfux.common.utils.testlistutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.ormfux.common.utils.ListUtils;

public class SplitTest {
    
    @Test
    public void testMultiChunks() {
        List<List<Integer>> result = ListUtils.split(Arrays.asList(1,2,3,4,5,6,7,8,9,0), 2);
        assertNotNull(result);
        assertEquals(5, result.size());
        assertEquals(Arrays.asList(1,2), result.get(0));
        assertEquals(Arrays.asList(3,4), result.get(1));
        assertEquals(Arrays.asList(5,6), result.get(2));
        assertEquals(Arrays.asList(7,8), result.get(3));
        assertEquals(Arrays.asList(9,0), result.get(4));
        
        result = ListUtils.split(Arrays.asList(1,2,3,4,5,6,7,8,9,0), 3);
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(Arrays.asList(1,2,3), result.get(0));
        assertEquals(Arrays.asList(4,5,6), result.get(1));
        assertEquals(Arrays.asList(7,8,9), result.get(2));
        assertEquals(Arrays.asList(0), result.get(3));
        
    }
    
    @Test
    public void testSingleChunk() {
        List<List<Integer>> result = ListUtils.split(Arrays.asList(1,2,3,4,5,6,7,8,9,0), 10);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Arrays.asList(1,2,3,4,5,6,7,8,9,0), result.get(0));
        
        result = ListUtils.split(Arrays.asList(1,2,3,4,5,6,7,8,9,0), 11);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Arrays.asList(1,2,3,4,5,6,7,8,9,0), result.get(0));
    }
    
    @Test
    public void testEmptySource() {
        List<List<Object>> result = ListUtils.split(new ArrayList<Object>(), 10);
        assertNotNull(result);
        assertEquals(0, result.size());
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullSource() {
        ListUtils.split(null, 1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testZeroChunkSize() {
        ListUtils.split(new ArrayList<>(), 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeChunkSize() {
        ListUtils.split(new ArrayList<>(), -1);
    }
    
}
