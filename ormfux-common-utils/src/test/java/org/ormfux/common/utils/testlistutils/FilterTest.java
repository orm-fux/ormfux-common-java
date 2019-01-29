package org.ormfux.common.utils.testlistutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.Test;
import org.ormfux.common.utils.ListUtils;

public class FilterTest {
    
    @Test
    public void testFilterWithMatch() {
        List<Integer> result = ListUtils.filter(Arrays.asList(1, 2, 3), value -> value == 1 || value == 2);
        
        assertEquals(2, result.size());
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
    }
    
    @Test
    public void testWithoutMatch() {
        List<Integer> result = ListUtils.filter(Arrays.asList(1, 2, 3), value -> value == 4);
        
        assertEquals(0, result.size());
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullFilteredList() {
        ListUtils.filter(null, value -> Objects.equals("4", value));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullPredicate() {
        ListUtils.filter(new ArrayList<>(), null);
    }
    
}
