package org.ormfux.common.db.annotation.testcollectionofentities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.ormfux.common.db.query.TypedQuery;
import org.ormfux.common.utils.ListUtils;

public class CreateEntityTest extends AbstractCollectionOfEntitiesTest {
    
    public CreateEntityTest() {
        super("createcollectiondb");
    }
    
    @Test
    public void testCreateViaJoinColumn() {
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_1', 0, 'mainmock')").executeUpdate();
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_2', 0, 'mainmock')").executeUpdate();
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_3', 0, null)").executeUpdate();
        
        TypedQuery<MockEntity2> query2 = queryManager.createQuery(MockEntity2.class);
        MockEntity2 mock2 = query2.load("mock2_1");
        
        MockEntity entity = new MockEntity();
        entity.getMockList1().add(mock2);
        
        TypedQuery<MockEntity> query = queryManager.createQuery(MockEntity.class);
        query.update(entity);
        
        assertNotNull(mock2);
        assertEquals(0L, mock2.getVersion());
        
        MockEntity loadedEntity = query.load(entity.getId());
        assertNotNull(loadedEntity);
        assertNotNull(loadedEntity.getMockList1());
        assertEquals(1, loadedEntity.getMockList1().size());
        assertTrue(ListUtils.exists(loadedEntity.getMockList1(), mock -> "mock2_1".equals(mock.getId())));
        assertNotNull(loadedEntity.getMockList2());
        assertEquals(0, loadedEntity.getMockList2().size());
        
    }
    
    @Test
    public void testCreateViaJoinTable() {
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_1', 0, null)").executeUpdate();
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_2', 0, null)").executeUpdate();
        queryManager.createQuery("insert into mock2 (id, version, mock) values ('mock2_3', 0, null)").executeUpdate();
        
        TypedQuery<MockEntity2> query2 = queryManager.createQuery(MockEntity2.class);
        MockEntity2 mock2 = query2.load("mock2_1");
        
        MockEntity entity = new MockEntity();
        entity.getMockList2().add(mock2);
        
        TypedQuery<MockEntity> query = queryManager.createQuery(MockEntity.class);
        query.update(entity);
        
        assertNotNull(mock2);
        assertEquals(0L, mock2.getVersion());
        
        MockEntity loadedEntity = query.load(entity.getId());
        assertNotNull(loadedEntity);
        assertNotNull(loadedEntity.getMockList1());
        assertEquals(0, loadedEntity.getMockList1().size());
        assertNotNull(loadedEntity.getMockList2());
        assertEquals(1, loadedEntity.getMockList2().size());
        assertTrue(ListUtils.exists(loadedEntity.getMockList2(), mock -> "mock2_1".equals(mock.getId())));
        
    }
    
}
