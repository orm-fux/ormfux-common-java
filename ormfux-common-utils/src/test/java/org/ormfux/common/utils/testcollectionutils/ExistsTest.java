package org.ormfux.common.utils.testcollectionutils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import org.junit.Test;
import org.ormfux.common.utils.CollectionUtils;

public class ExistsTest {
    
    @Test
    public void testExists() {
        assertTrue(CollectionUtils.exists(Arrays.asList("1", "2", "3"), value -> Objects.equals("2", value)));
        assertTrue(CollectionUtils.exists(Arrays.asList("1", null, "2", "3"), value -> Objects.equals("2", value)));
    }
    
    @Test
    public void testNotExists() {
        assertFalse(CollectionUtils.exists(Arrays.asList("1", "2", "3"), value -> Objects.equals("4", value)));
        assertFalse(CollectionUtils.exists(Arrays.asList("1", null, "2", "3"), value -> Objects.equals("4", value)));
        assertFalse(CollectionUtils.exists(Collections.emptyList(), value -> Objects.equals("4", value)));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullCollection() {
        CollectionUtils.exists(null, value -> Objects.equals("4", value));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullPredicate() {
        CollectionUtils.exists(Collections.emptyList(), null);
    }
    
}
