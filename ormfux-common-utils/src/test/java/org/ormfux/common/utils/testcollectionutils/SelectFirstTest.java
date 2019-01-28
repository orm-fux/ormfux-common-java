package org.ormfux.common.utils.testcollectionutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.ormfux.common.utils.CollectionUtils;

public class SelectFirstTest {
    
    @Test
    public void testNonEmptyHit() {
        assertEquals("1hit", CollectionUtils.selectFirst(Arrays.asList("1nohit", "2hit", "1hit", "3"), value -> value.startsWith("1hit")));
    }
    
    @Test
    public void testNonEmptyNoHit() {
        assertNull(CollectionUtils.selectFirst(Arrays.asList("1nohit", "2hit", "1hit", "3"), value -> value.startsWith("4hit")));
    }
    
    @Test
    public void testEmpty() {
        assertNull(CollectionUtils.selectFirst(new ArrayList<String>(), value -> value.startsWith("4hit")));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullSource() {
        CollectionUtils.selectFirst(null, value -> true);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullCondition() {
        CollectionUtils.selectFirst(new ArrayList<>(), null);
    }
    
}
