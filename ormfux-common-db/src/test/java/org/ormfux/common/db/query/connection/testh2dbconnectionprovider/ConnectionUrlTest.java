package org.ormfux.common.db.query.connection.testh2dbconnectionprovider;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import org.junit.Test;
import org.ormfux.common.db.query.connection.H2DbConnectionProvider;

public class ConnectionUrlTest {
    
    @Test
    public void testWithoutParams() {
        H2DbConnectionProvider connectionProvider = new H2DbConnectionProvider("dbfile");
        assertEquals("dbfile", getConnectionUrl(connectionProvider));
        
        connectionProvider = new H2DbConnectionProvider("dbfile", (String[]) null);
        assertEquals("dbfile", getConnectionUrl(connectionProvider));
        
        connectionProvider = new H2DbConnectionProvider("dbfile", new String[0]);
        assertEquals("dbfile", getConnectionUrl(connectionProvider));
    }
    
    @Test
    public void testWithParams() {
        H2DbConnectionProvider connectionProvider = new H2DbConnectionProvider("dbfile", "param1=value1", "param2=value2");
        assertEquals("dbfile;param1=value1;param2=value2", getConnectionUrl(connectionProvider));
    }
    
    private String getConnectionUrl(H2DbConnectionProvider connectionProvider) {
        try {
            final Field urlField = H2DbConnectionProvider.class.getDeclaredField("connectionUrl");
            urlField.setAccessible(true);
            
            return (String) urlField.get(connectionProvider);
            
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException("Error readon connection URL field");
        }
    }
    
}
