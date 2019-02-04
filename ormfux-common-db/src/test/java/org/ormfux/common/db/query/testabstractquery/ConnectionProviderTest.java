package org.ormfux.common.db.query.testabstractquery;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.junit.Test;
import org.ormfux.common.db.query.AbstractQuery;

public class ConnectionProviderTest extends AbstractAbstractQueryTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testNoConnectionProvider() {
        new MockQuery(null);
    }
    
    @Test
    public void testWithConnectionProvider() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        final MockConnectionProvider connectionProvider = new MockConnectionProvider();
        
        MockQuery query = new MockQuery(connectionProvider);
        
        Field connectionProviderField = AbstractQuery.class.getDeclaredField("dbConnectionProvider");
        connectionProviderField.setAccessible(true);
        
        assertTrue(connectionProvider == connectionProviderField.get(query));
    }
    
}
