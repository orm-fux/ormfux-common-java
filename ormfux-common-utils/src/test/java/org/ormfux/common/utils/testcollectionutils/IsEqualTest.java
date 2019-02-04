package org.ormfux.common.utils.testcollectionutils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;
import org.ormfux.common.utils.CollectionUtils;

public class IsEqualTest {
    
    @Test
    public void testEqualEmpty() {
        assertTrue(CollectionUtils.isEqual(new HashSet<>(), new ArrayList<>()));
    }
    
    @Test
    public void testEqualNotEmpty() {
        final Collection<Object> collection1 = new ArrayList<>();
        final Collection<Object> collection2 = new HashSet<>();
        
        collection1.add("value 1");
        collection1.add(1);
        collection1.add(null);
        
        collection2.add("value 1");
        collection2.add(1);
        collection2.add(null);
        
        assertTrue(CollectionUtils.isEqual(collection1, collection2));
        assertTrue(CollectionUtils.isEqual(collection1, collection1));
        assertTrue(CollectionUtils.isEqual(collection2, collection1));
    }
    
    @Test
    public void testNotEqualNotEmpty() {
        final Collection<Object> collection1 = new ArrayList<>();
        final Collection<Object> collection2 = new HashSet<>();
        
        collection1.add("value 1");
        collection1.add("value 2");
        collection1.add(1);
        collection1.add(null);
        
        collection2.add("value 1");
        collection2.add(1);
        collection2.add(null);
        
        assertFalse(CollectionUtils.isEqual(collection1, collection2));
        assertFalse(CollectionUtils.isEqual(collection2, collection1));
        
        collection1.remove("value 1");
        
        assertFalse(CollectionUtils.isEqual(collection1, collection2));
        assertFalse(CollectionUtils.isEqual(collection2, collection1));
    }
    
    @Test
    public void testEqualNull() {
        assertTrue(CollectionUtils.isEqual(null, null));
    }
    
    @Test
    public void testNotEqualNull() {
        assertFalse(CollectionUtils.isEqual(null, new ArrayList<>()));
        assertFalse(CollectionUtils.isEqual(new HashSet<>(), null));
    }
    
}
