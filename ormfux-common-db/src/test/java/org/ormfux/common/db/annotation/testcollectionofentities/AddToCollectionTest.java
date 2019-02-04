package org.ormfux.common.db.annotation.testcollectionofentities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.ormfux.common.db.query.TypedQuery;
import org.ormfux.common.utils.ListUtils;

public class AddToCollectionTest extends AbstractCollectionOfEntitiesTest {
    
    public AddToCollectionTest() {
        super("addcollectiondb");
    }
    
    @Test
    public void testAddViaJoinColumn() {
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_1', 0, 'mainmock')").executeUpdate();
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_2', 0, null)").executeUpdate();
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_3', 0, null)").executeUpdate();
        
        queryManager.createQuery("insert into mock (id, version) values ('mainmock', 0)").executeUpdate();
        
        TypedQuery<MockEntity> query = queryManager.createQuery(MockEntity.class);
        
        MockEntity loadedEntity = query.load("mainmock");
        assertNotNull(loadedEntity);
        assertNotNull(loadedEntity.getMockList1());
        assertEquals(1, loadedEntity.getMockList1().size());
        assertEquals(0, loadedEntity.getMockList2().size());
        
        TypedQuery<MockEntity2> query2 = queryManager.createQuery(MockEntity2.class);
        MockEntity2 mock2 = query2.load("mock2_2");
        assertNotNull(mock2);
        assertEquals(0L, mock2.getVersion());
        
        loadedEntity.getMockList1().add(mock2);
        assertEquals(0L, mock2.getVersion());
        
        query.update(loadedEntity);
        assertEquals(0L, mock2.getVersion());
        assertEquals(1L, loadedEntity.getVersion());
        
        loadedEntity = query.load("mainmock");
        assertNotNull(loadedEntity);
        assertNotNull(loadedEntity.getMockList1());
        assertEquals(2, loadedEntity.getMockList1().size());
        assertTrue(ListUtils.exists(loadedEntity.getMockList1(), mock -> "mock2_1".equals(mock.getId())));
        assertTrue(ListUtils.exists(loadedEntity.getMockList1(), mock -> "mock2_2".equals(mock.getId())));
        assertEquals(0, loadedEntity.getMockList2().size());
        
        mock2 = query2.load("mock2_2");
        assertNotNull(mock2);
        assertEquals(0L, mock2.getVersion());
        
        
    }
    
    @Test
    public void testAddViaJoinTable() {
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_1', 0, null)").executeUpdate();
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_2', 0, null)").executeUpdate();
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_3', 0, null)").executeUpdate();
        
        queryManager.createQuery("insert into mock (id, version) values ('mainmock', 0)").executeUpdate();
        
        queryManager.createQuery("insert into mock1_mock2 (mocklink, mock2link) values ('mainmock', 'mock2_1')").executeUpdate();
        
        TypedQuery<MockEntity> query = queryManager.createQuery(MockEntity.class);
        
        MockEntity loadedEntity = query.load("mainmock");
        assertNotNull(loadedEntity);
        assertNotNull(loadedEntity.getMockList1());
        assertEquals(0, loadedEntity.getMockList1().size());
        assertNotNull(loadedEntity.getMockList2());
        assertEquals(1, loadedEntity.getMockList2().size());
        
        TypedQuery<MockEntity2> query2 = queryManager.createQuery(MockEntity2.class);
        MockEntity2 mock2 = query2.load("mock2_2");
        assertNotNull(mock2);
        assertEquals(0L, mock2.getVersion());
        
        loadedEntity.getMockList2().add(mock2);
        assertEquals(0L, mock2.getVersion());
        
        query.update(loadedEntity);
        assertEquals(0L, mock2.getVersion());
        assertEquals(1L, loadedEntity.getVersion());
        
        loadedEntity = query.load("mainmock");
        assertNotNull(loadedEntity);
        assertNotNull(loadedEntity.getMockList1());
        assertEquals(0, loadedEntity.getMockList1().size());
        assertEquals(2, loadedEntity.getMockList2().size());
        assertTrue(ListUtils.exists(loadedEntity.getMockList2(), mock -> "mock2_1".equals(mock.getId())));
        assertTrue(ListUtils.exists(loadedEntity.getMockList2(), mock -> "mock2_2".equals(mock.getId())));
        
        mock2 = query2.load("mock2_2");
        assertNotNull(mock2);
        assertEquals(0L, mock2.getVersion());
        
    }
    
}
