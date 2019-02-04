package org.ormfux.common.db.query.connection.testh2dbconnectionprovider;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.ormfux.common.db.query.connection.H2DbConnectionProvider;

public class CloseAllConnectionsTest {
    
    @Test
    public void testCloseAll() throws SQLException {
        final String h2Url = "inmemcloseall";
        
        H2DbConnectionProvider connectionProvider = new H2DbConnectionProvider("jdbc:h2:mem:" + h2Url);
        Connection connection1 = connectionProvider.getConnection();
        Connection connection2 = connectionProvider.getConnection();
        Connection connection3 = connectionProvider.getConnection();
        
        connectionProvider.closeAllConnections();
        
        assertTrue(connection1.isClosed());
        assertTrue(connection2.isClosed());
        assertTrue(connection3.isClosed());
        
    }
    
}
