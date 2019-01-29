package org.ormfux.common.utils.testlistutils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import org.junit.Test;
import org.ormfux.common.utils.ListUtils;

public class ExistsTest {
    
    @Test
    public void testExists() {
        assertTrue(ListUtils.exists(Arrays.asList("1", "2", "3"), value -> Objects.equals("2", value)));
        assertTrue(ListUtils.exists(Arrays.asList("1", null, "2", "3"), value -> Objects.equals("2", value)));
    }
    
    @Test
    public void testNotExists() {
        assertFalse(ListUtils.exists(Arrays.asList("1", "2", "3"), value -> Objects.equals("4", value)));
        assertFalse(ListUtils.exists(Arrays.asList("1", null, "2", "3"), value -> Objects.equals("4", value)));
        assertFalse(ListUtils.exists(Collections.emptyList(), value -> Objects.equals("4", value)));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullList() {
        ListUtils.exists(null, value -> Objects.equals("4", value));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullPredicate() {
        ListUtils.exists(Collections.emptyList(), null);
    }}
