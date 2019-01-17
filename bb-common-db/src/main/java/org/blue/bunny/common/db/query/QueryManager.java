package org.blue.bunny.common.db.query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.blue.bunny.common.db.exception.SQLException;
import org.blue.bunny.common.ioc.annotations.Bean;
import org.h2.Driver;

/**
 * A manager, which allows to execute queries on an H2 database.
 */
@Bean
public class QueryManager {
    
    /**
     * The database URL.
     */
    private String databaseUrl;
    
    /**
     * Loads the database driver.
     */
    public QueryManager() throws SQLException {
        try {
            Class.forName(Driver.class.getName());
        } catch (final ClassNotFoundException e) {
            throw new SQLException("Could not load database driver.", e);
        }
    }
    
    /**
     * Creates a new query for this manager's database.
     *
     * @param queryString The query.
     */
    public Query createQuery(final String queryString) {
        return new Query(databaseUrl, queryString);
    }
    
    /**
     * Creates a new "select all" query for the entity type. The type must be annotated with {@code @Entity}
     * 
     * @param entityType The type fo entity to query.
     */
    public <T> TypedQuery<T> createQuery(final Class<T> entityType) {
        return new TypedQuery<>(databaseUrl, null, entityType);
    }
    
    /**
     * Creates a new query for the entity type. Adds the suffix to the query. The suffix can consist of joins, 
     * where conditions, etc.
     * 
     * @param entityType The type fo entity to query.
     * @param querySuffix The suffix for the query.
     */
    public <T> TypedQuery<T> createQuery(final Class<T> entityType, final String querySuffix) {
        return new TypedQuery<>(databaseUrl, querySuffix, entityType);
    }
    
    /**
     * Creates a new query for the entity type. Adds the suffix to the query. The suffix can consist of joins, 
     * where conditions, etc.
     * 
     * @param entityType The type fo entity to query.
     * @param querySuffix The suffix for the query.
     * @param entityAlias Alias for the entity in the query when the auto-generated one should not be used.
     */
    public <T> TypedQuery<T> createQuery(final Class<T> entityType, final String querySuffix, final String entityAlias) {
        return new TypedQuery<>(databaseUrl, querySuffix, entityType, entityAlias);
    }
    
    /**
     * The database URL.
     */
    public void setDatabaseUrl(final String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }
    
    /**
     * Closes all connections to the database and releases the lock on the database file. 
     */
    public void closeAllDbConnections() {
        try {
            final Connection closeConnection = DriverManager.getConnection(databaseUrl);
            final Statement closeStatement = closeConnection.createStatement();
            closeStatement.execute("SHUTDOWN");
            closeStatement.close();
            closeConnection.close();
        } catch (java.sql.SQLException e) {
            throw new SQLException("Cannot connect to database.", e);
        }
    }
    
}
