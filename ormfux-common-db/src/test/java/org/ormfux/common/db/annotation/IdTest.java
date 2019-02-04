package org.ormfux.common.db.annotation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;
import org.ormfux.common.db.annotation.Column;
import org.ormfux.common.db.annotation.Entity;
import org.ormfux.common.db.annotation.Id;
import org.ormfux.common.db.annotation.Version;
import org.ormfux.common.db.exception.SQLException;
import org.ormfux.common.db.generators.RandomIdGenerator;
import org.ormfux.common.db.query.TypedQuery;

public class IdTest extends AbstractAnnotationTest {
    
    public IdTest() {
        super("iddb");
    }
    
    @Before
    public void beforeTest() {
        super.beforeTest();
        
        queryManager.createQuery("create table identity (id varchar(255) not null, version bigint)").executeUpdate();
        queryManager.createQuery("insert into identity (id, version) values ('idvalue', 0)").executeUpdate();
    }
    
    @Test(expected = SQLException.class)
    public void testMissingIdCreate() {
        MissingIdEntity entity = new MissingIdEntity();
        
        queryManager.createQuery(MissingIdEntity.class).update(entity);
        
    }

    @Test(expected = SQLException.class)
    public void testMissingIdUpdate() {
        MissingIdEntity entity = new MissingIdEntity();
        entity.setId("idvalue");
        
        queryManager.createQuery(MissingIdEntity.class).update(entity);
        
    }
    
    @Test(expected = SQLException.class)
    public void testMissingIdDelete() throws java.sql.SQLException {
        MissingIdEntity entity = new MissingIdEntity();
        entity.setId("idvalue");
        
        try {
            queryManager.createQuery(MissingIdEntity.class).delete(entity);
        } catch (SQLException e) {
            Connection connection = getDbConnection();
            Statement statement = connection.createStatement();
            
            ResultSet result = statement.executeQuery("select id from identity where id = 'idvalue'");
            assertTrue(result.next());
            assertFalse(result.next());
            
            result.close();
            statement.close();
            connection.close();
            
            throw e;
        }
    }
    
    @Test
    public void testIdCreate() {
        IdEntity entity = new IdEntity();
        
        queryManager.createQuery(IdEntity.class).update(entity);
        assertNotNull(entity.getId());
        assertTrue(entity.getId().matches("[a-f0-9]{8}\\-([a-f0-9]{4}\\-){3}[a-f0-9]{12}"));
        
    }

    @Test
    public void testIdUpdate() {
        IdEntity entity = new IdEntity();
        entity.setId("idvalue");
        
        queryManager.createQuery(IdEntity.class).update(entity);
        
    }
    
    @Test
    public void testIdDelete() throws java.sql.SQLException {
        IdEntity entity = new IdEntity();
        entity.setId("idvalue");
        
        TypedQuery<IdEntity> query = queryManager.createQuery(IdEntity.class);
        query.delete(entity);
        assertNull(query.load("idvalue"));
    }
    
    @Entity(table = "identity")
    public static class MissingIdEntity {
        
        @Column(columnName = "id", columnLabel = "id")
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
        
    }
    
    @Entity(table = "identity")
    public static class IdEntity {
        
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
