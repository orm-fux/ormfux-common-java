package org.ormfux.common.db.query.testtypedquery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.ormfux.common.db.annotation.Column;
import org.ormfux.common.db.annotation.Entity;
import org.ormfux.common.db.annotation.Id;
import org.ormfux.common.db.annotation.Version;
import org.ormfux.common.db.exception.StaleEntityException;
import org.ormfux.common.db.generators.RandomIdGenerator;
import org.ormfux.common.db.query.TypedQuery;

public class UpdateTest extends AbstractTypedQueryTest {
    
    public UpdateTest() {
        super("updatedb");
    }
    
    @Before
    public void beforeTest() {
        super.beforeTest();
        
        StringBuilder createQuery = new StringBuilder();
        createQuery.append("create table ")
                   .append("mock2 (")
                   .append("id varchar(255) not null, ")
                   .append("version bigint not null, ")
                   .append("value varchar(255) ")
                   .append(");");
        
        queryManager.createQuery(createQuery.toString()).executeUpdate();
        
        String dataQuery = "insert into mock2 (id, version) values ('id', 0)";
        queryManager.createQuery(dataQuery).executeUpdate();
    }
    
    @Test
    public void testUpdateExisting() {
        MockEntity2 entity = new MockEntity2();
        entity.setId("id");
        entity.setVersion(0);
        entity.setProperty("propvalue");
        
        TypedQuery<MockEntity2> query = queryManager.createQuery(MockEntity2.class);
        query.update(entity);
        
        MockEntity2 loadedEntity = query.load(entity.getId());
        assertNotNull(loadedEntity);
        assertEquals(1L, loadedEntity.getVersion());
        assertEquals("propvalue", loadedEntity.getProperty());
        assertEquals(1L, entity.getVersion()); //version is incremented in original as well.
        
    }
    
    @Test
    public void testCreateNew() {
        MockEntity2 entity = new MockEntity2();
        entity.setProperty("second entity");
        
        TypedQuery<MockEntity2> query = queryManager.createQuery(MockEntity2.class);
        query.update(entity);
        
        query = queryManager.createQuery(MockEntity2.class, "where value = 'second entity'");
        
        MockEntity2 loadedEntity = query.getSingleResult();
        assertNotNull(loadedEntity);
        assertNotNull(loadedEntity.getId());
        assertEquals(0L, loadedEntity.getVersion());
        assertEquals("second entity", loadedEntity.getProperty());
        
    }
    
    @Test(expected = StaleEntityException.class)
    public void testStaleData() {
        MockEntity2 entity = new MockEntity2();
        entity.setId("id");
        entity.setVersion(0);
        entity.setProperty("propvalue");
        
        TypedQuery<MockEntity2> query = queryManager.createQuery(MockEntity2.class);
        
        try {
            query.update(entity);
            
            MockEntity2 loadedEntity = query.load(entity.getId());
            assertNotNull(loadedEntity);
            assertEquals(1L, loadedEntity.getVersion());
            
        } catch (StaleEntityException e) {
            fail("The StaleEntityException should not occur yet.");
        }
        
        entity.setVersion(0);
        query.update(entity);
    }
    
    @Test(expected = StaleEntityException.class)
    public void testDataTooNew() {
        MockEntity2 entity = new MockEntity2();
        entity.setId("id");
        entity.setVersion(1);
        entity.setProperty("propvalue");
        
        TypedQuery<MockEntity2> query = queryManager.createQuery(MockEntity2.class);
        query.update(entity);
    }
    
    @Entity(table = "mock2")
    public static class MockEntity2 {
        
        @Column(columnName = "id", columnLabel = "id")
        @Id(RandomIdGenerator.class)
        private String id;
        
        @Column(columnName = "version", columnLabel = "version")
        @Version
        private long version;
        
        @Column(columnName = "value", columnLabel = "value")
        private String property;
        
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public long getVersion() {
            return version;
        }
        
        public void setVersion(long version) {
            this.version = version;
        }
        
        
        public String getProperty() {
            return property;
        }
        
        public void setProperty(String property) {
            this.property = property;
        }
    }
}
