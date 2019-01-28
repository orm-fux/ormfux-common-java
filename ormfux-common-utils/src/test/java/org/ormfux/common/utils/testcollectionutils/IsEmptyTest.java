package org.ormfux.common.utils.testcollectionutils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import org.junit.Test;
import org.ormfux.common.utils.CollectionUtils;

public class IsEmptyTest {
    
    @Test
    public void testEmpty() {
        assertTrue(CollectionUtils.isEmpty(null));
        assertTrue(CollectionUtils.isEmpty(new ArrayList<>()));
    }
    
    @Test
    public void testNotEmpty() {
        assertFalse(CollectionUtils.isEmpty(Arrays.asList(1)));
        
        final Collection<Object> collection = new LinkedList<>();
        collection.add(null);
        
        assertFalse(CollectionUtils.isEmpty(collection));
    }
    
}
