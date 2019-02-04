package org.ormfux.common.db.query.testtypedquery;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.ormfux.common.db.annotation.Column;
import org.ormfux.common.db.annotation.Entity;
import org.ormfux.common.db.annotation.Id;
import org.ormfux.common.db.generators.RandomIdGenerator;
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
                   .append("id varchar(255) not null ")
                   .append(");");
        
        queryManager.createQuery(createQuery.toString()).executeUpdate();
        
        String dataQuery = "insert into mock (id) values ('id')";
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
    
    @Entity(table = "mock")
    public static class MockEntity {
        
        @Column(columnName = "id", columnLabel = "id")
        @Id(RandomIdGenerator.class)
        private String id;
        
        
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
    }
}
