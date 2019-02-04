package org.ormfux.common.db.annotation.testcolumn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.ormfux.common.db.annotation.AbstractAnnotationTest;
import org.ormfux.common.db.annotation.Column;
import org.ormfux.common.db.annotation.Entity;
import org.ormfux.common.db.annotation.Id;
import org.ormfux.common.db.annotation.Version;
import org.ormfux.common.db.exception.SQLException;
import org.ormfux.common.db.generators.RandomIdGenerator;
import org.ormfux.common.db.query.QueryResult.QueryResultRow;
import org.ormfux.common.db.query.TypedQuery;

public class NonMappedPropertyTest extends AbstractAnnotationTest {
    
    public NonMappedPropertyTest() {
        super("mappedpropertydb");
    }
    
    @Before
    public void beforeTest() {
        super.beforeTest();
        
        queryManager.createQuery("create table entity (id varchar(255) not null, "
                                                    + "version bigint, "
                                                    + "stringprop varchar(255))").executeUpdate();
        queryManager.createQuery("insert into entity (id, version) values ('idvalue', 0)").executeUpdate();
    }
    
    @Test
    public void testCreateNotInDbSuccess() {
        PropertyNotInDbEntity entity = new PropertyNotInDbEntity();
        
        TypedQuery<PropertyNotInDbEntity> query = queryManager.createQuery(PropertyNotInDbEntity.class);
        
        query.update(entity);
        assertNotNull(entity.getId());
    }

    @Test(expected = SQLException.class)
    public void testCreateNotInDbFail() {
        PropertyNotInDbEntity entity = new PropertyNotInDbEntity();
        entity.setStringProp("string value");
        
        TypedQuery<PropertyNotInDbEntity> query = queryManager.createQuery(PropertyNotInDbEntity.class);
        
        query.update(entity);
    }

    @Test(expected = SQLException.class)
    public void testLoadNotInDb() {
        TypedQuery<PropertyNotInDbEntity> query = queryManager.createQuery(PropertyNotInDbEntity.class);
        
        query.load("idvalue");
    }

    @Test(expected = SQLException.class)
    public void testUpdateNotInDb() {
        PropertyNotInDbEntity entity = new PropertyNotInDbEntity();
        entity.setId("idvalue");
        
        TypedQuery<PropertyNotInDbEntity> query = queryManager.createQuery(PropertyNotInDbEntity.class);
        query.update(entity);
    }
    
    @Test
    public void testCreateNotMapped() {
        PropertyNotMappedEntity entity = new PropertyNotMappedEntity();
        
        TypedQuery<PropertyNotMappedEntity> query = queryManager.createQuery(PropertyNotMappedEntity.class);
        
        query.update(entity);
        assertNotNull(entity.getId());
        assertEquals(0L, entity.getVersion());
        
        PropertyNotMappedEntity loadedEntity = query.load(entity.getId());
        assertNotNull(loadedEntity);
        assertEquals(0L, loadedEntity.getVersion());
        
    }

    @Test
    public void testLoadNotMapped() {
        queryManager.createQuery("insert into entity (id, version, stringprop, )"
                               + " values "
                               + "('id2', 5, 'string value')")
                    .executeUpdate();
        
        TypedQuery<PropertyNotMappedEntity> query = queryManager.createQuery(PropertyNotMappedEntity.class);
        
        PropertyNotMappedEntity entity = query.load("id2");
        assertNotNull(entity.getId());
        assertEquals(5L, entity.getVersion());
        
    }

    @Test
    public void testUpdateNotMapped() {
        queryManager.createQuery("insert into entity (id, version, stringprop, )"
                               + " values "
                               + "('id2', 5, 'string value')")
                    .executeUpdate();
        
        PropertyNotMappedEntity entity = new PropertyNotMappedEntity();
        entity.setId("id2");
        entity.setVersion(5);
        
        TypedQuery<PropertyNotMappedEntity> query = queryManager.createQuery(PropertyNotMappedEntity.class);
        query.update(entity);
        assertNotNull(entity.getId());
        assertEquals(6L, entity.getVersion());
        
        QueryResultRow result = queryManager.createQuery("select stringprop from entity where id = 'id2'").getSingleResult();
        assertNotNull(result);
        assertEquals("string value", result.getValue(0));
        
    }
    
    @Entity(table = "entity")
    public static class PropertyNotInDbEntity {
        
        @Column(columnName = "id", columnLabel = "id")
        @Id(RandomIdGenerator.class)
        private String id;
        
        @Column(columnName = "version", columnLabel = "version")
        @Version
        private long version;
        
        @Column(columnName = "nonexisting", columnLabel = "nonexisting")
        private String stringProp;
        
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

        
        public String getStringProp() {
            return stringProp;
        }

        
        public void setStringProp(String stringProp) {
            this.stringProp = stringProp;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof PropertyNotInDbEntity) {
                return StringUtils.equals(this.id, ((PropertyNotInDbEntity) obj).id);
            } else {
                return super.equals(obj);
            }
        }
        
    }
    
    @Entity(table = "entity")
    public static class PropertyNotMappedEntity {
        
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

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof PropertyNotInDbEntity) {
                return StringUtils.equals(this.id, ((PropertyNotInDbEntity) obj).id);
            } else {
                return super.equals(obj);
            }
        }
        
    }
}
