package org.blue.bunny.ledger.customlibs.db.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.blue.bunny.ledger.customlibs.utils.DateUtils;
import org.junit.Before;
import org.junit.Test;

public class TypedQueryTest {
    
    private QueryManager queryManager;
    
    @Before
    public void beforeTest() {
        queryManager = new QueryManager();
        queryManager.setDatabaseUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=MYSQL;DATABASE_TO_UPPER=false;AUTOCOMMIT=false");
        
        queryManager.createQuery("DROP ALL OBJECTS").executeUpdate();
        
        StringBuilder createQuery = new StringBuilder();
        createQuery.append("create table ")
                   .append("mock (")
                   .append("id varchar(255) not null, ")
                   .append("creationDate datetime, ")
                   .append("version bigint not null, ")
                   .append("gross decimal(19,2), ")
                   .append("primary key (id), ")
                   .append(");");
        
        createQuery.append("create table ")
                   .append("mock2 (")
                   .append("id varchar(255) not null, ")
                   .append("mock varchar(255), ")
                   .append("version bigint not null, ")
                   .append("primary key (id), ")
                   .append(");");
        
        createQuery.append("create table ")
                   .append("mock1_mock2 (")
                   .append("mock varchar(255) not null, ")
                   .append("mock2 varchar(255) not null ")
                   .append(")");
        
        queryManager.createQuery(createQuery.toString()).executeUpdate();
        
        String dataQuery = "insert into mock (id, creationDate, version, gross) values ('id', '2018-02-11', 1, 19.5)";
        queryManager.createQuery(dataQuery).executeUpdate();
        
        dataQuery = "insert into mock2 (id, version, mock) values ('id2', 0, 'id')";
        queryManager.createQuery(dataQuery).executeUpdate();
        
        dataQuery = "insert into mock2 (id, version, mock) values ('id3', 0, null)";
        queryManager.createQuery(dataQuery).executeUpdate();
        
        dataQuery = "insert into mock1_mock2 (mock, mock2) values ('id', 'id3')";
        queryManager.createQuery(dataQuery).executeUpdate();
    }
    
    @Test
    public void testGetSingleResult() {
        TypedQuery<MockEntity> typedQuery = queryManager.createQuery(MockEntity.class);
        MockEntity mock = typedQuery.getSingleResult();
        assertNotNull(mock);
        assertEquals("id", mock.getId());
        assertEquals(DateUtils.getDate(2018, 02, 11), mock.getCreationDate());
        assertEquals(1L, mock.getVersion());
        assertEquals(new BigDecimal("19.50"), mock.getGross());
        
        assertNotNull(mock.getMockList1());
        assertEquals(1, mock.getMockList1().size());
        assertEquals("id2", mock.getMockList1().get(0).getId());
        assertEquals(0L, mock.getMockList1().get(0).getVersion());
        
        assertNotNull(mock.getMockList2());
        assertEquals(1, mock.getMockList2().size());
        assertEquals("id3", mock.getMockList2().get(0).getId());
        assertEquals(0L, mock.getMockList2().get(0).getVersion());
        
        typedQuery = queryManager.createQuery(MockEntity.class, "where mock.id = :id and mock.version = :version and mock.creationDate = :creationDate ");
        typedQuery.addParameter("id", "id");
        typedQuery.addParameter("version", 1L);
        typedQuery.addParameter("creationDate", DateUtils.getDate(2018, 2, 11));
        
        typedQuery.getSingleResult();
        
    }
    
    @Test
    public void testLoad() {
        TypedQuery<MockEntity> typedQuery = queryManager.createQuery(MockEntity.class);
        MockEntity mock = typedQuery.load("id");
        assertNotNull(mock);
        assertEquals("id", mock.getId());
        assertEquals(DateUtils.getDate(2018, 02, 11), mock.getCreationDate());
        assertEquals(1L, mock.getVersion());
        assertEquals(new BigDecimal("19.50"), mock.getGross());
        
        assertNotNull(mock.getMockList1());
        assertEquals(1, mock.getMockList1().size());
        assertEquals("id2", mock.getMockList1().get(0).getId());
        assertEquals(0L, mock.getMockList1().get(0).getVersion());
        
        assertNotNull(mock.getMockList2());
        assertEquals(1, mock.getMockList2().size());
        assertEquals("id3", mock.getMockList2().get(0).getId());
        assertEquals(0L, mock.getMockList2().get(0).getVersion());
        
    }
    
    @Test
    public void testDelete() {
        TypedQuery<MockEntity> typedQuery = queryManager.createQuery(MockEntity.class);
        
        MockEntity mock = typedQuery.load("id");
        assertNotNull(mock);
        
        typedQuery.delete(mock);
        
        mock = typedQuery.load("id");
        assertNull(mock);
        
        TypedQuery<MockEntity2> typedQuery2 = queryManager.createQuery(MockEntity2.class);
        MockEntity2 mock2 = typedQuery2.load("id2");
        assertNotNull(mock2);
        assertEquals(1L, mock2.getVersion()); //version went up
        
        typedQuery2 = queryManager.createQuery(MockEntity2.class);
        mock2 = typedQuery2.load("id3");
        assertNotNull(mock2);
        assertEquals(0L, mock2.getVersion());
        
        Query query = queryManager.createQuery("select mock from mock1_mock2 where mock = 'id'");
        assertNull(query.getSingleResult());
        
        query = queryManager.createQuery("select mock from mock2 where mock = 'id2'");
        assertNull(query.getSingleResult());
    }
    
    @Test
    public void testCreate() {
        TypedQuery<MockEntity2> typedQuery2 = queryManager.createQuery(MockEntity2.class);
        MockEntity2 mock2 = typedQuery2.load("id2");
        
        MockEntity newEntity = new MockEntity();
        newEntity.setGross(BigDecimal.TEN);
        newEntity.getMockList1().add(mock2);
        newEntity.getMockList2().add(mock2);
        
        TypedQuery<MockEntity> typedQuery = queryManager.createQuery(MockEntity.class);
        Object entityId = typedQuery.update(newEntity);
        assertNotNull(entityId);
        
        MockEntity persistedEntity = typedQuery.load(entityId);
        assertNotNull(persistedEntity);
        assertEquals(new BigDecimal("10.00"), persistedEntity.getGross());
        assertEquals(0L, persistedEntity.getVersion());
        assertEquals(1, persistedEntity.getMockList1().size());
        assertEquals(1, persistedEntity.getMockList2().size());
        assertEquals("id2", persistedEntity.getMockList1().get(0).getId());
        assertEquals("id2", persistedEntity.getMockList2().get(0).getId());
        
    }
    
    @Test
    public void testUpdate() {
        TypedQuery<MockEntity> typedQuery = queryManager.createQuery(MockEntity.class);
        MockEntity mock = typedQuery.load("id");
        assertNotNull(mock);
        
        String dataQuery = "insert into mock2 (id, version, mock) values ('id4', 0, null)";
        queryManager.createQuery(dataQuery).executeUpdate();
        
        TypedQuery<MockEntity2> typedQuery2 = queryManager.createQuery(MockEntity2.class);
        MockEntity2 mock2 = typedQuery2.load("id4");
        assertNotNull(mock2);
        
        mock.setGross(new BigDecimal("-500"));
        mock.getMockList1().clear();
        mock.getMockList2().clear();
        mock.getMockList1().add(mock2);
        mock.getMockList2().add(mock2);
        
        typedQuery.update(mock);
        
        mock2 = typedQuery2.load("id4");
        assertNotNull(mock2);
        assertEquals(1L, mock2.getVersion());
        
        mock = typedQuery.load("id");
        assertNotNull(mock);
        assertEquals(2L, mock.getVersion());
        assertEquals(new BigDecimal("-500.00"), mock.getGross());
        assertEquals(1, mock.getMockList1().size());
        assertEquals(1, mock.getMockList2().size());
        assertEquals("id4", mock.getMockList1().get(0).getId());
        assertEquals("id4", mock.getMockList2().get(0).getId());
    }
    
}
