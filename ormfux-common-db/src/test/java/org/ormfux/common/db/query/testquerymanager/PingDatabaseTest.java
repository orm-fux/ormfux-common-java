package org.ormfux.common.db.query.testquerymanager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;

import org.junit.Test;
import org.ormfux.common.db.query.QueryManager;
import org.ormfux.common.db.query.connection.AbstractDbConnectionProvider;

public class PingDatabaseTest extends AbstractQueryManagerTest {
    
    @Test
    public void testAvailable() {
        QueryManager queryManager = new QueryManager();
        queryManager.setDatabase(MockConnectionProvider.class, "dburl");
        
        assertTrue(queryManager.pingDatabase());
        
    }
    
    @Test
    public void testNotAvailable() {
        QueryManager queryManager = new QueryManager();
        queryManager.setDatabase(MockConnectionProvider.class, "dburl");
        
        MockConnectionProvider connectionProvider = (MockConnectionProvider) getConnectionProvider(queryManager);
        connectionProvider.ping = false;
        assertFalse(queryManager.pingDatabase());
        
    }
    
    public static class MockConnectionProvider extends AbstractDbConnectionProvider {
        
        private boolean ping = true;
        
        public MockConnectionProvider(String databaseUrl, String[] connectionParams) {
            super(PingDatabaseTest.class.getName(), databaseUrl, connectionParams);
        }
        
        @Override
        public boolean isCanBackupDatabase() {
            return false;
        }

        @Override
        public boolean ping() {
            return ping;
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
