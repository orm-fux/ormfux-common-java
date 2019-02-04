package org.ormfux.common.db.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.ormfux.common.db.exception.SQLException;
import org.ormfux.common.db.generators.RandomIdGenerator;
import org.ormfux.common.db.query.TypedQuery;

public class VersionTest extends AbstractAnnotationTest {
    
    public VersionTest() {
        super("versiondb");
    }
    
    @Before
    public void beforeTest() {
        super.beforeTest();
        
        queryManager.createQuery("create table versionentity (id varchar(255) not null, version bigint)").executeUpdate();
        queryManager.createQuery("insert into versionentity (id, version) values ('idvalue', 0)").executeUpdate();
    }
    
    @Test
    public void testMissingVersionCreate() {
        MissingVersionEntity entity = new MissingVersionEntity();
        
        queryManager.createQuery(MissingVersionEntity.class).update(entity);
        assertNotNull(entity.getId());
        assertEquals(0L, entity.getVersion());
        
    }

    @Test(expected = SQLException.class)
    public void testMissingVersionUpdate() {
        MissingVersionEntity entity = new MissingVersionEntity();
        entity.setId("idvalue");
        
        queryManager.createQuery(MissingVersionEntity.class).update(entity);
        
    }
    
    @Test
    public void testMissingVersionDelete() {
        MissingVersionEntity entity = new MissingVersionEntity();
        entity.setId("idvalue");
        
        TypedQuery<MissingVersionEntity> query = queryManager.createQuery(MissingVersionEntity.class);
        query.delete(entity);
        assertNull(query.load("idvalue"));
    }
    
    @Test
    public void testVersionCreate() {
        VersionEntity entity = new VersionEntity();
        
        queryManager.createQuery(VersionEntity.class).update(entity);
        assertNotNull(entity.getId());
        assertEquals(0L, entity.getVersion());
        
    }

    @Test
    public void testVersionUpdate() {
        VersionEntity entity = new VersionEntity();
        entity.setId("idvalue");
        
        queryManager.createQuery(VersionEntity.class).update(entity);
        assertEquals(1L, entity.getVersion());
        
    }
    
    @Test
    public void testVersionDelete() throws java.sql.SQLException {
        VersionEntity entity = new VersionEntity();
        entity.setId("idvalue");
        
        TypedQuery<VersionEntity> query = queryManager.createQuery(VersionEntity.class);
        query.delete(entity);
        assertNull(query.load("idvalue"));
    }
    
    @Entity(table = "versionentity")
    public static class MissingVersionEntity {
        
        @Column(columnName = "id", columnLabel = "id")
        @Id(RandomIdGenerator.class)
        private String id;
        
        @Column(columnName = "version", columnLabel = "version")
        private long version;
        
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
        
    }
    
    @Entity(table = "versionentity")
    public static class VersionEntity {
        
        @Column(columnName = "id", columnLabel = "id")
        @Id(RandomIdGenerator.class)
        private String id;
        
        @Column(columnName = "version", columnLabel = "version")
        @Version
        private long version;
        
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
    }
}
