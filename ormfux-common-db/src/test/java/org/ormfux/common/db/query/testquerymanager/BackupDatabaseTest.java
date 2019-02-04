package org.ormfux.common.db.query.testquerymanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Connection;

import org.junit.Test;
import org.ormfux.common.db.query.QueryManager;
import org.ormfux.common.db.query.connection.AbstractDbConnectionProvider;

public class BackupDatabaseTest extends AbstractQueryManagerTest {
    
    @Test
    public void testSuccessfulBackup() {
        QueryManager queryManager = new QueryManager();
        queryManager.setDatabase(MockConnectionProvider.class, "dburl");
        
        MockConnectionProvider connectionProvider = (MockConnectionProvider) getConnectionProvider(queryManager);
        
        assertNull(connectionProvider.backup);
        queryManager.backupDatabase("backupText");
        assertEquals("backupText", connectionProvider.backup);
        
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testBackupNotPossible() {
        QueryManager queryManager = new QueryManager();
        queryManager.setDatabase(MockConnectionProvider.class, "dburl");
        
        MockConnectionProvider connectionProvider = (MockConnectionProvider) getConnectionProvider(queryManager);
        connectionProvider.canbackup = false;
        
        queryManager.backupDatabase("backupText");
    }
    
    public static class MockConnectionProvider extends AbstractDbConnectionProvider {
        
        private boolean canbackup = true;
        
        private String backup;
        
        public MockConnectionProvider(String databaseUrl, String[] connectionParams) {
            super(BackupDatabaseTest.class.getName(), databaseUrl, connectionParams);
        }
        
        @Override
        protected void doBackupDatabase(CharSequence databaseVersion) {
            this.backup = databaseVersion.toString();
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
