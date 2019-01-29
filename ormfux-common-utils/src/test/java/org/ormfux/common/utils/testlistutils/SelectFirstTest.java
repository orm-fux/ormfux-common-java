package org.ormfux.common.utils.testlistutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.ormfux.common.utils.ListUtils;

public class SelectFirstTest {
    
    @Test
    public void testNonEmptyHit() {
        assertEquals("1hit1", ListUtils.selectFirst(Arrays.asList("1nohit", "2hit", "1hit1", "1hit2", "3"), value -> value.startsWith("1hit")));
    }
    
    @Test
    public void testNonEmptyNoHit() {
        assertNull(ListUtils.selectFirst(Arrays.asList("1nohit", "2hit", "1hit", "3"), value -> value.startsWith("4hit")));
    }
    
    @Test
    public void testEmpty() {
        assertNull(ListUtils.selectFirst(new ArrayList<String>(), value -> value.startsWith("4hit")));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullSource() {
        ListUtils.selectFirst(null, value -> true);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullCondition() {
        ListUtils.selectFirst(new ArrayList<>(), null);
    }
    
}
