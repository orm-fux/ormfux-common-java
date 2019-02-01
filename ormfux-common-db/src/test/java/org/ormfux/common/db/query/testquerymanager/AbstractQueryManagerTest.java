package org.ormfux.common.db.query.testquerymanager;

import java.lang.reflect.Field;

import org.ormfux.common.db.query.QueryManager;
import org.ormfux.common.db.query.connection.AbstractDbConnectionProvider;

public abstract class AbstractQueryManagerTest {
    
    protected AbstractDbConnectionProvider getConnectionProvider(QueryManager queryManager) {
        try {
            Field connectionProviderField = QueryManager.class.getDeclaredField("connectionProvider");
            connectionProviderField.setAccessible(true);
            
            return (AbstractDbConnectionProvider) connectionProviderField.get(queryManager);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException("Cannot get connection provider");
        }
        
    }
    
}
