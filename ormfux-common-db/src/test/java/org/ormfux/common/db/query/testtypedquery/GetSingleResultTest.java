package org.ormfux.common.db.query.testtypedquery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.ormfux.common.db.exception.NonMatchedParamException;
import org.ormfux.common.db.exception.NonUniqueResultException;
import org.ormfux.common.db.exception.SQLException;
import org.ormfux.common.db.query.TypedQuery;

public class GetSingleResultTest extends AbstractTypedQueryTest {
    
    public GetSingleResultTest() {
        super("singleresultdb");
    }

    @Test
    public void testGetSingleResultWithoutLimitation() {
        TypedQuery<MockEntity> typedQuery = queryManager.createQuery(MockEntity.class);
        MockEntity mock = typedQuery.getSingleResult();
        
        assertNotNull(mock);
        assertEquals("id", mock.getId());
    }
    
    @Test(expected = NonUniqueResultException.class)
    public void testMoreThanOneSingleResult() {
        queryManager.createQuery("insert into mock (id) values ('id2')").executeUpdate();
        
        TypedQuery<MockEntity> typedQuery = queryManager.createQuery(MockEntity.class);
        typedQuery.getSingleResult();
    }
    
    @Test
    public void testGetSingleResultWithLimitation() {
        queryManager.createQuery("insert into mock (id) values ('id2')").executeUpdate();
        
        TypedQuery<MockEntity> typedQuery = queryManager.createQuery(MockEntity.class, "where mock.id = :id ");
        typedQuery.addParameter("id", "id");
        
        MockEntity mock = typedQuery.getSingleResult();
        
        assertNotNull(mock);
        assertEquals("id", mock.getId());
    }
    
    @Test
    public void testNoResult() {
        TypedQuery<MockEntity> typedQuery = queryManager.createQuery(MockEntity.class, "where 1<>1");
        
        MockEntity mock = typedQuery.getSingleResult();
        assertNull(mock);
    }
    
    @Test(expected = SQLException.class)
    public void testParamValueMissing() {
        TypedQuery<MockEntity> typedQuery = queryManager.createQuery(MockEntity.class, "where mock.id = :id ");
        
        typedQuery.getSingleResult();
    }
    
    @Test(expected = NonMatchedParamException.class)
    public void testTooManyParams() {
        TypedQuery<MockEntity> typedQuery = queryManager.createQuery(MockEntity.class, "where mock.id = :id ");
        typedQuery.addParameter("id", "id");
        typedQuery.addParameter("id2", "id");
        
        typedQuery.getSingleResult();
    }
    
}
