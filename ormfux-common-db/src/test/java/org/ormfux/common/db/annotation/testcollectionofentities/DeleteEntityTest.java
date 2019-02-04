package org.ormfux.common.db.annotation.testcollectionofentities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.ormfux.common.db.query.TypedQuery;
import org.ormfux.common.utils.ListUtils;

public class DeleteEntityTest extends AbstractCollectionOfEntitiesTest {
    
    public DeleteEntityTest() {
        super("deletecollectiondb");
    }
    
    @Test
    public void testDeleteViaJoinColumn() {
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_1', 0, 'mainmock')").executeUpdate();
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_2', 0, 'mainmock')").executeUpdate();
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_3', 0, null)").executeUpdate();
        
        queryManager.createQuery("insert into mock (id, version) values ('mainmock', 0)").executeUpdate();
        
        TypedQuery<MockEntity> query = queryManager.createQuery(MockEntity.class);
        
        MockEntity loadedEntity = query.load("mainmock");
        assertNotNull(loadedEntity);
        assertNotNull(loadedEntity.getMockList1());
        assertEquals(2, loadedEntity.getMockList1().size());
        assertNotNull(loadedEntity.getMockList2());
        assertEquals(0, loadedEntity.getMockList2().size());
        
        query.delete(loadedEntity);
        assertNull(query.load(loadedEntity.getId()));
        
        assertEquals(0, queryManager.createQuery("select mock from mock2 where mock = 'mainmock'").getResultList().size());
        
        TypedQuery<MockEntity2> query2 = queryManager.createQuery(MockEntity2.class);
        assertNotNull(query2.load("mock2_1"));
        assertNotNull(query2.load("mock2_2"));
        assertNotNull(query2.load("mock2_3"));
    }
    
    @Test
    public void testDeleteViaJoinTable() {
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_1', 0, null)").executeUpdate();
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_2', 0, null)").executeUpdate();
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_3', 0, null)").executeUpdate();
        
        queryManager.createQuery("insert into mock (id, version) values ('mainmock', 0)").executeUpdate();
        
        queryManager.createQuery("insert into mock1_mock2 (mocklink, mock2link) values ('mainmock', 'mock2_1')").executeUpdate();
        queryManager.createQuery("insert into mock1_mock2 (mocklink, mock2link) values ('mainmock', 'mock2_2')").executeUpdate();
        
        TypedQuery<MockEntity> query = queryManager.createQuery(MockEntity.class);
        
        MockEntity loadedEntity = query.load("mainmock");
        assertNotNull(loadedEntity);
        assertNotNull(loadedEntity.getMockList1());
        assertEquals(0, loadedEntity.getMockList1().size());
        assertNotNull(loadedEntity.getMockList2());
        assertEquals(2, loadedEntity.getMockList2().size());
        assertTrue(ListUtils.exists(loadedEntity.getMockList2(), mock -> "mock2_1".equals(mock.getId())));
        assertTrue(ListUtils.exists(loadedEntity.getMockList2(), mock -> "mock2_2".equals(mock.getId())));
        
        query.delete(loadedEntity);
        assertNull(query.load(loadedEntity.getId()));
        
        assertEquals(0, queryManager.createQuery("select mock2link from mock1_mock2 where mocklink = 'mainmock'").getResultList().size());
        
        TypedQuery<MockEntity2> query2 = queryManager.createQuery(MockEntity2.class);
        assertNotNull(query2.load("mock2_1"));
        assertNotNull(query2.load("mock2_2"));
        assertNotNull(query2.load("mock2_3"));
    }
    
}
