package org.ormfux.common.db.annotation.testcollectionofentities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.ormfux.common.db.query.TypedQuery;
import org.ormfux.common.utils.ListUtils;

public class LoadEntityTest extends AbstractCollectionOfEntitiesTest {
    
    public LoadEntityTest() {
        super("loadcollectiondb");
    }
    
    @Test
    public void testLoadViaJoinColumn() {
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_1', 0, 'mainmock')").executeUpdate();
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_2', 0, 'mainmock')").executeUpdate();
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_3', 0, null)").executeUpdate();
        
        queryManager.createQuery("insert into mock (id, version) values ('mainmock', 0)").executeUpdate();
        
        TypedQuery<MockEntity> query = queryManager.createQuery(MockEntity.class);
        
        MockEntity loadedEntity = query.load("mainmock");
        assertNotNull(loadedEntity);
        assertNotNull(loadedEntity.getMockList1());
        assertEquals(2, loadedEntity.getMockList1().size());
        assertTrue(ListUtils.exists(loadedEntity.getMockList1(), mock -> "mock2_1".equals(mock.getId())));
        assertTrue(ListUtils.exists(loadedEntity.getMockList1(), mock -> "mock2_2".equals(mock.getId())));
        assertNotNull(loadedEntity.getMockList2());
        assertEquals(0, loadedEntity.getMockList2().size());
        
    }
    
    @Test
    public void testLoadEmpty() {
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_1', 0, null)").executeUpdate();
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_2', 0, null)").executeUpdate();
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_3', 0, null)").executeUpdate();
        
        queryManager.createQuery("insert into mock (id, version) values ('mainmock', 0)").executeUpdate();
        
        TypedQuery<MockEntity> query = queryManager.createQuery(MockEntity.class);
        
        MockEntity loadedEntity = query.load("mainmock");
        assertNotNull(loadedEntity);
        assertNotNull(loadedEntity.getMockList1());
        assertEquals(0, loadedEntity.getMockList1().size());
        assertNotNull(loadedEntity.getMockList2());
        assertEquals(0, loadedEntity.getMockList2().size());
        
    }
    
    @Test
    public void testLoadViaJoinTable() {
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
        
    }
    
}
