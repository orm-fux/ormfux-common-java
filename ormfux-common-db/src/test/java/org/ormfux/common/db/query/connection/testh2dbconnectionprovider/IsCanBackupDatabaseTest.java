package org.ormfux.common.db.query.connection.testh2dbconnectionprovider;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.ormfux.common.db.query.connection.H2DbConnectionProvider;

public class IsCanBackupDatabaseTest {
    
    @Test
    public void testCanBackup() {
        assertTrue(new H2DbConnectionProvider("jdbc:h2:file:").isCanBackupDatabase());
        assertTrue(new H2DbConnectionProvider("jdbc:h2:file:test").isCanBackupDatabase());
    }
    
    @Test
    public void testCannotBackup() {
        assertFalse(new H2DbConnectionProvider("jdbc:h2:file").isCanBackupDatabase());
        assertFalse(new H2DbConnectionProvider("jdbc:h2:flie:test").isCanBackupDatabase());
        assertFalse(new H2DbConnectionProvider("jdbc:h2:mem:test").isCanBackupDatabase());
    }
    
}
