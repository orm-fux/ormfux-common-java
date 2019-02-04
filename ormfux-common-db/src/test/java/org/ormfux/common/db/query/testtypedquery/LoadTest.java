package org.ormfux.common.db.query.testtypedquery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.ormfux.common.db.query.TypedQuery;

public class LoadTest extends AbstractTypedQueryTest {
    
    public LoadTest() {
        super("loaddb");
    }

    @Test
    public void testFound() {
        TypedQuery<MockEntity> typedQuery = queryManager.createQuery(MockEntity.class);
        MockEntity mock = typedQuery.load("id");
        
        assertNotNull(mock);
        assertEquals("id", mock.getId());
    }
    
    @Test
    public void testNotFound() {
        TypedQuery<MockEntity> typedQuery = queryManager.createQuery(MockEntity.class);
        assertNull(typedQuery.load("nonExisting"));
    }
    
    @Test
    public void testNullId() {
        TypedQuery<MockEntity> typedQuery = queryManager.createQuery(MockEntity.class);
        assertNull(typedQuery.load(null));
    }
    
}
