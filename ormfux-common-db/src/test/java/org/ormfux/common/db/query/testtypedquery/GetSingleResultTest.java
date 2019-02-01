package org.ormfux.common.db.query.testtypedquery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.junit.Test;
import org.ormfux.common.db.mock.MockEntity;
import org.ormfux.common.db.query.TypedQuery;
import org.ormfux.common.utils.DateUtils;

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
        assertEquals(DateUtils.getDate(2018, 02, 11), mock.getCreationDate());
        assertEquals(1L, mock.getVersion());
        assertEquals(new BigDecimal("19.50"), mock.getGross());
        
        assertNotNull(mock.getMockList1());
        assertEquals(1, mock.getMockList1().size());
        assertEquals("id2", mock.getMockList1().get(0).getId());
        assertEquals(0L, mock.getMockList1().get(0).getVersion());
        
        assertNotNull(mock.getMockList2());
        assertEquals(1, mock.getMockList2().size());
        assertEquals("id3", mock.getMockList2().get(0).getId());
        assertEquals(0L, mock.getMockList2().get(0).getVersion());
        
    }
    
    @Test
    public void testGetSingleResultWithLimitation() {
        TypedQuery<MockEntity> typedQuery = queryManager.createQuery(MockEntity.class, 
                                                                     "where mock.id = :id and mock.version = :version and mock.creationDate = :creationDate ");
        typedQuery.addParameter("id", "id");
        typedQuery.addParameter("version", 1L);
        typedQuery.addParameter("creationDate", DateUtils.getDate(2018, 2, 11));
        
        MockEntity mock = typedQuery.getSingleResult();
        
        assertNotNull(mock);
        assertEquals("id", mock.getId());
        assertEquals(DateUtils.getDate(2018, 02, 11), mock.getCreationDate());
        assertEquals(1L, mock.getVersion());
        assertEquals(new BigDecimal("19.50"), mock.getGross());
        
        assertNotNull(mock.getMockList1());
        assertEquals(1, mock.getMockList1().size());
        assertEquals("id2", mock.getMockList1().get(0).getId());
        assertEquals(0L, mock.getMockList1().get(0).getVersion());
        
        assertNotNull(mock.getMockList2());
        assertEquals(1, mock.getMockList2().size());
        assertEquals("id3", mock.getMockList2().get(0).getId());
        assertEquals(0L, mock.getMockList2().get(0).getVersion());
        
    }
    
    @Test
    public void testNoResult() {
        TypedQuery<MockEntity> typedQuery = queryManager.createQuery(MockEntity.class, "where 1<>1");
        
        MockEntity mock = typedQuery.getSingleResult();
        assertNull(mock);
    }
    
}
