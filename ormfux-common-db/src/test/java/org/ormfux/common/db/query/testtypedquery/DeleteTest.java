package org.ormfux.common.db.query.testtypedquery;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.ormfux.common.db.query.TypedQuery;

public class DeleteTest extends AbstractTypedQueryTest {
    
    public DeleteTest() {
        super("deletedb");
    }

    @Test
    public void testFound() {
        TypedQuery<MockEntity> typedQuery = queryManager.createQuery(MockEntity.class);
        MockEntity mock = typedQuery.load("id");
        assertNotNull(mock);
        
        typedQuery.delete(mock);
        
        mock = typedQuery.load("id");
        assertNull(mock);
        
    }
    
    @Test
    public void testNotFound() {
        TypedQuery<MockEntity> typedQuery = queryManager.createQuery(MockEntity.class);
        MockEntity mock = typedQuery.load("id");
        assertNotNull(mock);
        
        MockEntity mock2 = new MockEntity();
        mock2.setId("id2");
        
        typedQuery.delete(mock2);
        
        mock = typedQuery.load("id");
        assertNotNull(mock);
        
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullEntity() {
        TypedQuery<MockEntity> typedQuery = queryManager.createQuery(MockEntity.class);
        typedQuery.delete(null);
    }
    
}
