package org.ormfux.common.utils.testcollectionutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.junit.Test;
import org.ormfux.common.utils.CollectionUtils;

public class AsMapTest {
    
    @Test
    public void testKeyValueFunction() {
        final List<String> list = Arrays.asList("1", "2", "3");
        
        final Map<String, String> map = CollectionUtils.asMap(list, value -> value + "key", value -> value + "value");
        assertNotNull(map);
        assertEquals(3, map.size());
        assertEquals("1value", map.get("1key"));
        assertEquals("2value", map.get("2key"));
        assertEquals("3value", map.get("3key"));
    }
    
    @Test
    public void testKeyOnlyFunction() {
        final List<String> list = Arrays.asList("1", "2", "3");
        
        final Map<String, String> map = CollectionUtils.asMap(list, value -> value + "key");
        assertNotNull(map);
        assertEquals(3, map.size());
        assertEquals("1", map.get("1key"));
        assertEquals("2", map.get("2key"));
        assertEquals("3", map.get("3key"));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullCollection() {
        CollectionUtils.asMap(null, Function.identity(), Function.identity());
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullKeyFunction() {
        final Map<Object, Object> map = CollectionUtils.asMap(new ArrayList<>(), null, Function.identity());
        assertNotNull(map);
        assertTrue(map.isEmpty());
        
        CollectionUtils.asMap(Arrays.asList(1), null, Function.identity());
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullValueFunction() {
        final Map<Object, Object> map = CollectionUtils.asMap(new ArrayList<>(), Function.identity(), null);
        assertNotNull(map);
        assertTrue(map.isEmpty());
        
        CollectionUtils.asMap(Arrays.asList(1), Function.identity(), null);
    }
    
}
