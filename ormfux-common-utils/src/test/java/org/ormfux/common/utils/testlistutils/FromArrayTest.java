package org.ormfux.common.utils.testlistutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.ormfux.common.utils.ListUtils;

public class FromArrayTest {
    
    @Test
    public void testNonEmpty() {
        List<Integer> result = ListUtils.fromArray(1,2,3);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(1), result.get(0));
        assertEquals(Integer.valueOf(2), result.get(1));
        assertEquals(Integer.valueOf(3), result.get(2));
        
    }
    
    @Test
    public void testEmpty() {
        List<Integer> result = ListUtils.fromArray(new Integer[0]);
        assertNotNull(result);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testModifyableResult() {
        List<Integer> result = ListUtils.fromArray(1,2,3);
        assertNotNull(result);
        assertEquals(3, result.size());
        result.add(4);
        assertEquals(4, result.size());
        assertEquals(Integer.valueOf(4), result.get(3));
    }
    
    @Test
    public void testNullSource() {
        List<Object> result = ListUtils.fromArray();
        assertNotNull(result);
        assertEquals(0, result.size());
    }
}
