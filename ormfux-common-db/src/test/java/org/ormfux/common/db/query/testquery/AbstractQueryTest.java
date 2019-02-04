package org.ormfux.common.db.query.testquery;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.ormfux.common.db.query.QueryManager;
import org.ormfux.common.db.query.connection.DbConnectionProvider;
import org.ormfux.common.db.query.connection.H2DbConnectionProvider;

public abstract class AbstractQueryTest {
    
    protected QueryManager queryManager;
    
    private String dbName;
    
    public AbstractQueryTest(String dbName) {
        this.dbName = dbName;
    }
    
    @Before
    public void beforeTest() throws SQLException {
        queryManager = new QueryManager();
        queryManager.setDatabase(H2DbConnectionProvider.class, 
                                 "jdbc:h2:mem:" + dbName, 
                                 "DB_CLOSE_DELAY=-1", 
                                 "MODE=MYSQL", 
                                 "DATABASE_TO_UPPER=false", 
                                 "AUTOCOMMIT=false");
        
        queryManager.createQuery("DROP ALL OBJECTS").executeUpdate();
        
        StringBuilder createQuery = new StringBuilder();
        createQuery.append("create table ")
                   .append("mock (")
                   .append("id varchar(255) not null ")
                   .append(");");
        
        Connection connection = getDbConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(createQuery.toString());
        connection.commit();
        
        statement.executeUpdate("insert into mock (id) values ('idvalue')");
        connection.commit();
        connection.close();
    }

    protected Connection getDbConnection() {
        return getConnectionProvider().getConnection();
    }
    
    @After
    public void afterTest() {
        DbConnectionProvider connectionProvider = getConnectionProvider();
        connectionProvider.closeAllConnections();
    }

    protected DbConnectionProvider getConnectionProvider() {
        try {
            Field connectionProviderField = QueryManager.class.getDeclaredField("connectionProvider");
            connectionProviderField.setAccessible(true);
            
            DbConnectionProvider connectionProvider = (DbConnectionProvider) connectionProviderField.get(queryManager);
            
            return connectionProvider;
            
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException("Cannot retrieve connection provider.");
        }
    }
}
