package org.ormfux.common.db.query.testtypedquery;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.ormfux.common.db.query.QueryManager;
import org.ormfux.common.db.query.connection.DbConnectionProvider;
import org.ormfux.common.db.query.connection.H2DbConnectionProvider;

public abstract class AbstractTypedQueryTest {
    
    protected QueryManager queryManager;
    
    private String dbName;
    
    public AbstractTypedQueryTest(String dbName) {
        this.dbName = dbName;
    }
    
    @Before
    public void beforeTest() {
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
                   .append("id varchar(255) not null, ")
                   .append("creationDate datetime, ")
                   .append("version bigint not null, ")
                   .append("gross decimal(19,2), ")
                   .append("primary key (id), ")
                   .append(");");
        
        createQuery.append("create table ")
                   .append("mock2 (")
                   .append("id varchar(255) not null, ")
                   .append("mock varchar(255), ")
                   .append("version bigint not null, ")
                   .append("primary key (id), ")
                   .append(");");
        
        createQuery.append("create table ")
                   .append("mock1_mock2 (")
                   .append("mock varchar(255) not null, ")
                   .append("mock2 varchar(255) not null ")
                   .append(")");
        
        queryManager.createQuery(createQuery.toString()).executeUpdate();
        
        String dataQuery = "insert into mock (id, creationDate, version, gross) values ('id', '2018-02-11', 1, 19.5)";
        queryManager.createQuery(dataQuery).executeUpdate();
        
        dataQuery = "insert into mock2 (id, version, mock) values ('id2', 0, 'id')";
        queryManager.createQuery(dataQuery).executeUpdate();
        
        dataQuery = "insert into mock2 (id, version, mock) values ('id3', 0, null)";
        queryManager.createQuery(dataQuery).executeUpdate();
        
        dataQuery = "insert into mock1_mock2 (mock, mock2) values ('id', 'id3')";
        queryManager.createQuery(dataQuery).executeUpdate();
    }
    
    @After
    public void afterTest() {
        try {
            Field connectionProviderField = QueryManager.class.getDeclaredField("connectionProvider");
            connectionProviderField.setAccessible(true);
            
            DbConnectionProvider connectionProvider = (DbConnectionProvider) connectionProviderField.get(queryManager);
            connectionProvider.closeAllConnections();
            
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException("Cannot close database");
        }
    }
}
