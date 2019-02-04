package org.ormfux.common.db.query.testquerymanager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;

import org.junit.Test;
import org.ormfux.common.db.query.QueryManager;
import org.ormfux.common.db.query.connection.AbstractDbConnectionProvider;

public class IsCanBackupDatabaseTest extends AbstractQueryManagerTest {
    
    @Test
    public void testCanBackup() {
        QueryManager queryManager = new QueryManager();
        queryManager.setDatabase(MockConnectionProvider.class, "dburl");
        
        assertTrue(queryManager.isCanBackupDatabase());
        
    }
    
    @Test
    public void testCannotBackup() {
        QueryManager queryManager = new QueryManager();
        queryManager.setDatabase(MockConnectionProvider.class, "dburl");
        
        MockConnectionProvider connectionProvider = (MockConnectionProvider) getConnectionProvider(queryManager);
        connectionProvider.canbackup = false;
        assertFalse(queryManager.isCanBackupDatabase());
        
    }
    
    public static class MockConnectionProvider extends AbstractDbConnectionProvider {
        
        private boolean canbackup = true;
        
        public MockConnectionProvider(String databaseUrl, String[] connectionParams) {
            super(IsCanBackupDatabaseTest.class.getName(), databaseUrl, connectionParams);
        }
        
        @Override
        public boolean isCanBackupDatabase() {
            return canbackup;
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
