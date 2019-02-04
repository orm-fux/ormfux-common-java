package org.ormfux.common.db.query.testquerymanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.ormfux.common.db.query.QueryManager;
import org.ormfux.common.db.query.connection.AbstractDbConnectionProvider;

public class SetDatabaseTest extends AbstractQueryManagerTest {
    
    @Test
    public void testWithoutConnectionParams() {
        QueryManager queryManager = new QueryManager();
        
        queryManager.setDatabase(MockConnectionProvider.class, "dburl");
        checkConnectionProviderValues(queryManager, MockConnectionProvider.class, "dburl", null);
        
        MockConnectionProvider connectionProvider = (MockConnectionProvider) getConnectionProvider(queryManager);
        assertFalse(connectionProvider.closed);
        
    }
    
    @Test
    public void testWithConnectionParams() {
        QueryManager queryManager = new QueryManager();
        
        queryManager.setDatabase(MockConnectionProvider.class, "dburl","param1","param2=value2");
        checkConnectionProviderValues(queryManager, MockConnectionProvider.class, "dburl", Arrays.asList("param2=value2", "param1"));
        
        MockConnectionProvider connectionProvider = (MockConnectionProvider) getConnectionProvider(queryManager);
        assertFalse(connectionProvider.closed);
    }
    
    @Test
    public void testConnectionProviderReplace() {
        QueryManager queryManager = new QueryManager();
        
        queryManager.setDatabase(MockConnectionProvider.class, "dburl");
        
        MockConnectionProvider connectionProvider = (MockConnectionProvider) getConnectionProvider(queryManager);
        assertFalse(connectionProvider.closed);
        
        queryManager.setDatabase(MockConnectionProvider.class, "dburl2");
        
        MockConnectionProvider connectionProvider2 = (MockConnectionProvider) getConnectionProvider(queryManager);
        assertTrue(connectionProvider.closed);
        assertFalse(connectionProvider2.closed);
    }
    
    private void checkConnectionProviderValues(QueryManager queryManager, Class<?> providerType, String providerUrl, List<String> params) {
        AbstractDbConnectionProvider connectionProvider = getConnectionProvider(queryManager);
        assertNotNull(connectionProvider);
        
        assertEquals(providerType, connectionProvider.getClass());
        
        try {
            Field databaseUrlField = AbstractDbConnectionProvider.class.getDeclaredField("databaseUrl");
            databaseUrlField.setAccessible(true);
            assertEquals(providerUrl, databaseUrlField.get(connectionProvider));
            
            Field paramsField = AbstractDbConnectionProvider.class.getDeclaredField("connectionParams");
            paramsField.setAccessible(true);
            
            assertNotNull(paramsField.get(connectionProvider));
            
            if (params == null) {
                assertEquals(Collections.emptyList(), paramsField.get(connectionProvider));
            } else {
                assertEquals(params.size(), ((List<?>) paramsField.get(connectionProvider)).size());
                assertTrue(params.containsAll((List<?>) paramsField.get(connectionProvider)));
            }
            
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new RuntimeException("Some field not found");
        }
        
    }
    
    public static class MockConnectionProvider extends AbstractDbConnectionProvider {
        
        private boolean closed;
        
        public MockConnectionProvider(String databaseUrl, String[] connectionParams) {
            super(SetDatabaseTest.class.getName(), databaseUrl, connectionParams);
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
            closed = true;
        }
        
    }
    
}
