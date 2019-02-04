package org.ormfux.common.db.annotation.testcollectionofentities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.ormfux.common.db.query.TypedQuery;

public class RemoveFromCollectionTest extends AbstractCollectionOfEntitiesTest {
    
    public RemoveFromCollectionTest() {
        super("removecollectiondb");
    }
    
    @Test
    public void testRemoveWithJoinColumn() {
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_1', 0, 'mainmock')").executeUpdate();
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_2', 0, 'mainmock')").executeUpdate();
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_3', 0, null)").executeUpdate();
        
        queryManager.createQuery("insert into mock (id, version) values ('mainmock', 0)").executeUpdate();
        
        TypedQuery<MockEntity> query = queryManager.createQuery(MockEntity.class);
        
        MockEntity loadedEntity = query.load("mainmock");
        assertNotNull(loadedEntity);
        assertEquals(2, loadedEntity.getMockList1().size());
        loadedEntity.getMockList1().removeIf(mock -> "mock2_1".equals(mock.getId()));
        
        query.update(loadedEntity);
        assertEquals(1L, loadedEntity.getVersion());
        assertEquals(0L, loadedEntity.getMockList1().get(0).getVersion());
        
        loadedEntity = query.load("mainmock");
        assertNotNull(loadedEntity);
        assertEquals(1L, loadedEntity.getVersion());
        assertEquals(1, loadedEntity.getMockList1().size());
        assertEquals(0L, loadedEntity.getMockList1().get(0).getVersion());
        
        TypedQuery<MockEntity2> query2 = queryManager.createQuery(MockEntity2.class);
        
        MockEntity2 mock2 = query2.load("mock2_1");
        assertNotNull(mock2);
        assertEquals(0L, mock2.getVersion());
        
        mock2 = query2.load("mock2_2");
        assertNotNull(mock2);
        assertEquals(0L, mock2.getVersion());
        
        mock2 = query2.load("mock2_3");
        assertNotNull(mock2);
        assertEquals(0L, mock2.getVersion());
    }
    
    @Test
    public void testRemoveViaJoinTable() {
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_1', 0, null)").executeUpdate();
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_2', 0, null)").executeUpdate();
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_3', 0, null)").executeUpdate();
        
        queryManager.createQuery("insert into mock (id, version) values ('mainmock', 0)").executeUpdate();
        queryManager.createQuery("insert into mock (id, version) values ('mainmock2', 0)").executeUpdate();
        
        queryManager.createQuery("insert into mock1_mock2 (mocklink, mock2link) values ('mainmock', 'mock2_1')").executeUpdate();
        queryManager.createQuery("insert into mock1_mock2 (mocklink, mock2link) values ('mainmock', 'mock2_2')").executeUpdate();
        queryManager.createQuery("insert into mock1_mock2 (mocklink, mock2link) values ('mainmock2', 'mock2_2')").executeUpdate();
        
        TypedQuery<MockEntity> query = queryManager.createQuery(MockEntity.class);
        
        MockEntity loadedEntity = query.load("mainmock");
        assertNotNull(loadedEntity);
        assertEquals(2, loadedEntity.getMockList2().size());
        loadedEntity.getMockList2().removeIf(mock -> "mock2_1".equals(mock.getId()));
        
        query.update(loadedEntity);
        assertEquals(1L, loadedEntity.getVersion());
        assertEquals(0L, loadedEntity.getMockList2().get(0).getVersion());
        
        loadedEntity = query.load("mainmock");
        assertNotNull(loadedEntity);
        assertEquals(1L, loadedEntity.getVersion());
        assertEquals(1, loadedEntity.getMockList2().size());
        assertEquals(0L, loadedEntity.getMockList2().get(0).getVersion());
        
        TypedQuery<MockEntity2> query2 = queryManager.createQuery(MockEntity2.class);
        
        MockEntity2 mock2 = query2.load("mock2_1");
        assertNotNull(mock2);
        assertEquals(0L, mock2.getVersion());
        
        mock2 = query2.load("mock2_2");
        assertNotNull(mock2);
        assertEquals(0L, mock2.getVersion());
        
        mock2 = query2.load("mock2_3");
        assertNotNull(mock2);
        assertEquals(0L, mock2.getVersion());
        
        assertNotNull(queryManager.createQuery("select mocklink from mock1_mock2 where mocklink = 'mainmock2'").getSingleResult());
        assertNotNull(queryManager.createQuery("select mocklink from mock1_mock2 where mocklink = 'mainmock'").getSingleResult());
        
    }
    
}
