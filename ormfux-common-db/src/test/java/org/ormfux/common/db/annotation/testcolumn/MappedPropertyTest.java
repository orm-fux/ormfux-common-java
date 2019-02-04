package org.ormfux.common.db.annotation.testcolumn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.ormfux.common.db.annotation.AbstractAnnotationTest;
import org.ormfux.common.db.annotation.Column;
import org.ormfux.common.db.annotation.Entity;
import org.ormfux.common.db.annotation.Id;
import org.ormfux.common.db.annotation.Version;
import org.ormfux.common.db.generators.RandomIdGenerator;
import org.ormfux.common.db.query.TypedQuery;
import org.ormfux.common.utils.DateUtils;
import org.ormfux.common.utils.object.Objects;

public class MappedPropertyTest extends AbstractAnnotationTest {
    
    public MappedPropertyTest() {
        super("mappedpropertydb");
    }
    
    @Before
    public void beforeTest() {
        super.beforeTest();
        
        queryManager.createQuery("create table entity (id varchar(255) not null, "
                                                    + "version bigint, "
                                                    + "stringprop varchar(255), "
                                                    + "enumprop varchar(255), "
                                                    + "entityprop varchar(255),"
                                                    + "dateprop timestamp,"
                                                    + "numberprop decimal(19,0))").executeUpdate();
        queryManager.createQuery("insert into entity (id, version) values ('idvalue', 0)").executeUpdate();
    }
    
    @Test
    public void testCreate() {
        SimplePropertiesEntity entity = new SimplePropertiesEntity();
        
        TypedQuery<SimplePropertiesEntity> query = queryManager.createQuery(SimplePropertiesEntity.class);
        
        query.update(entity);
        assertNotNull(entity.getId());
        assertEquals(0L, entity.getVersion());
        assertNull(entity.getStringProp());
        assertNull(entity.getEnumProp());
        assertNull(entity.getEntityProp());
        assertNull(entity.getDateProp());
        assertNull(entity.getNumberProp());
        
        SimplePropertiesEntity loadedEntity = query.load(entity.getId());
        assertNotNull(loadedEntity);
        assertEquals(0L, loadedEntity.getVersion());
        assertNull(loadedEntity.getStringProp());
        assertNull(loadedEntity.getEnumProp());
        assertNull(loadedEntity.getEntityProp());
        assertNull(loadedEntity.getDateProp());
        assertNull(loadedEntity.getNumberProp());
        
        entity = new SimplePropertiesEntity();
        entity.setEnumProp(TimeUnit.HOURS);
        entity.setStringProp("string value");
        entity.setEntityProp(loadedEntity);
        entity.setDateProp(DateUtils.getDate(2000, 1, 1));
        entity.setNumberProp(BigDecimal.TEN);
        
        query.update(entity);
        assertNotNull(entity.getId());
        assertEquals(0L, entity.getVersion());
        assertEquals("string value", entity.getStringProp());
        assertEquals(TimeUnit.HOURS, entity.getEnumProp());
        assertEquals(loadedEntity, entity.getEntityProp());
        assertEquals(DateUtils.getDate(2000, 1, 1), entity.getDateProp());
        assertEquals(BigDecimal.TEN, entity.getNumberProp());
        
        SimplePropertiesEntity loadedEntity2 = query.load(entity.getId());
        assertNotNull(loadedEntity2.getId());
        assertEquals(0L, loadedEntity2.getVersion());
        assertEquals("string value", loadedEntity2.getStringProp());
        assertEquals(TimeUnit.HOURS, loadedEntity2.getEnumProp());
        assertEquals(loadedEntity, loadedEntity2.getEntityProp());
        assertFalse(Objects.isSame(loadedEntity, loadedEntity2.getEntityProp()));
        assertEquals(DateUtils.getDate(2000, 1, 1), loadedEntity2.getDateProp());
        assertEquals(BigDecimal.TEN, loadedEntity2.getNumberProp());
    }

    @Test
    public void testLoad() {
        queryManager.createQuery("insert into entity (id, version, stringprop, enumprop, entityprop, dateprop, numberprop)"
                               + " values "
                               + "('id2', 5, 'string value', 'HOURS', 'idvalue', '2019-01-01 00:00:00', 5)")
                    .executeUpdate();
        
        TypedQuery<SimplePropertiesEntity> query = queryManager.createQuery(SimplePropertiesEntity.class);
        
        SimplePropertiesEntity entity = query.load("id2");
        assertNotNull(entity.getId());
        assertEquals(5L, entity.getVersion());
        assertEquals("string value", entity.getStringProp());
        assertEquals(TimeUnit.HOURS, entity.getEnumProp());
        assertEquals(DateUtils.getDate(2019, 1, 1), entity.getDateProp());
        assertEquals(new BigDecimal("5"), entity.getNumberProp());
        assertNotNull(entity.getEntityProp());
        
        SimplePropertiesEntity secondEntity = entity.getEntityProp();
        
        assertEquals(0L, secondEntity.getVersion());
        assertNull(secondEntity.getStringProp());
        assertNull(secondEntity.getEnumProp());
        assertNull(secondEntity.getEntityProp());
        assertNull(secondEntity.getDateProp());
        assertNull(secondEntity.getNumberProp());
        
    }

    @Test
    public void testUpdate() {
        SimplePropertiesEntity entity = new SimplePropertiesEntity();
        entity.setId("idvalue");
        
        TypedQuery<SimplePropertiesEntity> query = queryManager.createQuery(SimplePropertiesEntity.class);
        query.update(entity);
        assertNotNull(entity.getId());
        assertEquals(1L, entity.getVersion());
        assertNull(entity.getStringProp());
        assertNull(entity.getEnumProp());
        assertNull(entity.getEntityProp());
        assertNull(entity.getDateProp());
        assertNull(entity.getNumberProp());
        
        SimplePropertiesEntity loadedEntity = query.load(entity.getId());
        assertNotNull(loadedEntity);
        assertEquals(1L, loadedEntity.getVersion());
        assertNull(loadedEntity.getStringProp());
        assertNull(loadedEntity.getEnumProp());
        assertNull(loadedEntity.getEntityProp());
        assertNull(loadedEntity.getDateProp());
        assertNull(loadedEntity.getNumberProp());
        
        entity.setEnumProp(TimeUnit.HOURS);
        entity.setStringProp("string value");
        entity.setEntityProp(loadedEntity);
        entity.setDateProp(DateUtils.getDate(2000, 1, 1));
        entity.setNumberProp(BigDecimal.TEN);
        
        query.update(entity);
        assertNotNull(entity.getId());
        assertEquals(2L, entity.getVersion());
        assertEquals("string value", entity.getStringProp());
        assertEquals(TimeUnit.HOURS, entity.getEnumProp());
        assertEquals(loadedEntity, entity.getEntityProp());
        assertEquals(DateUtils.getDate(2000, 1, 1), entity.getDateProp());
        assertEquals(BigDecimal.TEN, entity.getNumberProp());
        
        SimplePropertiesEntity loadedEntity2 = query.load(entity.getId());
        assertNotNull(loadedEntity2.getId());
        assertEquals(2L, loadedEntity2.getVersion());
        assertEquals("string value", loadedEntity2.getStringProp());
        assertEquals(TimeUnit.HOURS, loadedEntity2.getEnumProp());
        assertEquals(loadedEntity, loadedEntity2.getEntityProp());
        assertFalse(Objects.isSame(loadedEntity, loadedEntity2.getEntityProp()));
        assertTrue(Objects.isSame(loadedEntity2, loadedEntity2.getEntityProp()));
        assertEquals(DateUtils.getDate(2000, 1, 1), loadedEntity2.getDateProp());
        assertEquals(BigDecimal.TEN, loadedEntity2.getNumberProp());
    }
    
    @Entity(table = "entity")
    public static class SimplePropertiesEntity {
        
        @Column(columnName = "id", columnLabel = "id")
        @Id(RandomIdGenerator.class)
        private String id;
        
        @Column(columnName = "version", columnLabel = "version")
        @Version
        private long version;
        
        @Column(columnName = "stringprop", columnLabel = "stringprop")
        private String stringProp;
        
        @Column(columnName = "enumprop", columnLabel = "enumprop")
        private TimeUnit enumProp;
        
        @Column(columnName = "entityprop", columnLabel = "entityprop")
        private SimplePropertiesEntity entityProp;
        
        @Column(columnName = "dateprop", columnLabel = "dateprop")
        private Date dateProp;
        
        @Column(columnName = "numberprop", columnLabel = "numberprop")
        private BigDecimal numberProp;
        
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

        
        public TimeUnit getEnumProp() {
            return enumProp;
        }

        
        public void setEnumProp(TimeUnit enumProp) {
            this.enumProp = enumProp;
        }

        
        public SimplePropertiesEntity getEntityProp() {
            return entityProp;
        }

        
        public void setEntityProp(SimplePropertiesEntity entityProp) {
            this.entityProp = entityProp;
        }
        
        
        public Date getDateProp() {
            return dateProp;
        }

        
        public void setDateProp(Date dateProp) {
            this.dateProp = dateProp;
        }

        
        public BigDecimal getNumberProp() {
            return numberProp;
        }

        
        public void setNumberProp(BigDecimal numberProp) {
            this.numberProp = numberProp;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof SimplePropertiesEntity) {
                return StringUtils.equals(this.id, ((SimplePropertiesEntity) obj).id);
            } else {
                return super.equals(obj);
            }
        }
        
    }
}
