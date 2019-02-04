package org.ormfux.common.db.query.testquerymanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.sql.Connection;

import org.junit.Before;
import org.junit.Test;
import org.ormfux.common.db.annotation.Entity;
import org.ormfux.common.db.query.AbstractQuery;
import org.ormfux.common.db.query.Query;
import org.ormfux.common.db.query.QueryManager;
import org.ormfux.common.db.query.TypedQuery;
import org.ormfux.common.db.query.connection.AbstractDbConnectionProvider;

public class CreateQueryTest extends AbstractQueryManagerTest {
    
    private QueryManager queryManager;
    
    private MockConnectionProvider connectionProvider;
    
    @Before
    public void beforeTest() {
        queryManager = new QueryManager();
        queryManager.setDatabase(MockConnectionProvider.class, "dburl");
        
        connectionProvider = (MockConnectionProvider) getConnectionProvider(queryManager);
    }
    
    @Test
    public void testCreateNativeQuery() {
        Query query = queryManager.createQuery("query string");
        assertNotNull(query);
        checkQueryValues(query, "query string", connectionProvider, null, null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testCreateNativeQueryWithoutString() {
        String nullString = null;
        queryManager.createQuery(nullString);
    }
    
    @Test
    public void testCreateTypedQueryWithoutSuffix() {
        TypedQuery<MockEntity> query = queryManager.createQuery(MockEntity.class);
        assertNotNull(query);
        checkQueryValues(query, null, connectionProvider, MockEntity.class, null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testCreateTypedQueryNullEntityType() {
        Class<?> nullType = null;
        queryManager.createQuery(nullType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateTypedQueryNonEntityType() {
        queryManager.createQuery(CreateQueryTest.class);
    }
    
    @Test
    public void testCreateTypedQueryWithSuffix() {
        TypedQuery<MockEntity> query = queryManager.createQuery(MockEntity.class, "query suffix");
        assertNotNull(query);
        checkQueryValues(query, "query suffix", connectionProvider, MockEntity.class, null);
        
        query = queryManager.createQuery(MockEntity.class, null);
        assertNotNull(query);
        checkQueryValues(query, null, connectionProvider, MockEntity.class, null);
    }
    
    @Test
    public void testCreateTypedQueryWithSuffixAndAlias() {
        TypedQuery<MockEntity> query = queryManager.createQuery(MockEntity.class, "query suffix", "alias");
        assertNotNull(query);
        checkQueryValues(query, "query suffix", connectionProvider, MockEntity.class, "alias");
        
        query = queryManager.createQuery(MockEntity.class, null, "alias");
        assertNotNull(query);
        checkQueryValues(query, null, connectionProvider, MockEntity.class, "alias");
        
        query = queryManager.createQuery(MockEntity.class, "query suffix", null);
        assertNotNull(query);
        checkQueryValues(query, "query suffix", connectionProvider, MockEntity.class, null);
        
        query = queryManager.createQuery(MockEntity.class, null, null);
        assertNotNull(query);
        checkQueryValues(query, null, connectionProvider, MockEntity.class, null);
    }
    
    private void checkQueryValues(AbstractQuery query, 
                                  String queryString, 
                                  MockConnectionProvider connectionProvider, 
                                  Class<?> entityType,
                                  String entityAlias) {
        try {
            Field queryStringField = AbstractQuery.class.getDeclaredField("queryString");
            queryStringField.setAccessible(true);
            assertEquals(queryString, queryStringField.get(query));
            
            Field connectionProviderField = AbstractQuery.class.getDeclaredField("dbConnectionProvider");
            connectionProviderField.setAccessible(true);
            assertEquals(connectionProvider, connectionProviderField.get(query));
            
            if (query instanceof TypedQuery) {
                Field entityTypeField = TypedQuery.class.getDeclaredField("entityType");
                entityTypeField.setAccessible(true);
                assertEquals(entityType, entityTypeField.get(query));
                
                Field entityAliasField = TypedQuery.class.getDeclaredField("entityAlias");
                entityAliasField.setAccessible(true);
                assertEquals(entityAlias, entityAliasField.get(query));
                
            }
            
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException("Cannot access Query field.");
        }
    }
    
    @Entity(table = "mocktable")
    public static class MockEntity {
        
    }
    
    public static class MockConnectionProvider extends AbstractDbConnectionProvider {
        
        public MockConnectionProvider(String databaseUrl, String[] connectionParams) {
            super(CreateQueryTest.class.getName(), databaseUrl, connectionParams);
        }
        
        @Override
        public boolean isCanBackupDatabase() {
            return false;
        }

        @Override
        public boolean ping() {
            return false;
        }

        @Override
        public Connection getConnection() {
            return null;
        }

        @Override
        public void closeAllConnections() {
        }
        
    }
}
