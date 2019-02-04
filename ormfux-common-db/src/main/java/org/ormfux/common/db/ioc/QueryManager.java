package org.ormfux.common.db.ioc;

import org.ormfux.common.db.query.Query;
import org.ormfux.common.db.query.TypedQuery;
import org.ormfux.common.db.query.connection.AbstractDbConnectionProvider;
import org.ormfux.common.db.query.connection.DbConnectionProvider;
import org.ormfux.common.ioc.annotations.Bean;

/**
 * A QueryManager that can be used as an injectable "service". By defining this separately 
 * we keep the IOC dependency optional.
 */
@Bean //This is the thing!
public class QueryManager {
    
    /**
     * The actual manager for query handling.
     */
    private final org.ormfux.common.db.query.QueryManager wrappedManager = new org.ormfux.common.db.query.QueryManager();
    
    /**
     * Creates a new query for this manager's database.
     *
     * @param queryString The query.
     */
    public Query createQuery(final String queryString) {
        return wrappedManager.createQuery(queryString);
    }
    
    /**
     * Creates a new "select all" query for the entity type. The type must be annotated with {@code @Entity}
     * 
     * @param entityType The type of entity to query.
     */
    public <T> TypedQuery<T> createQuery(final Class<T> entityType) {
        return wrappedManager.createQuery(entityType);
    }
    
    /**
     * Creates a new query for the entity type. Adds the suffix to the query. The suffix can consist of joins, 
     * where conditions, etc.
     * 
     * @param entityType The type of entity to query.
     * @param querySuffix The suffix for the query.
     */
    public <T> TypedQuery<T> createQuery(final Class<T> entityType, final String querySuffix) {
        return wrappedManager.createQuery(entityType, querySuffix);
    }
    
    /**
     * Creates a new query for the entity type. Adds the suffix to the query. The suffix can consist of joins, 
     * where conditions, etc.
     * 
     * @param entityType The type of entity to query.
     * @param querySuffix The suffix for the query.
     * @param entityAlias Alias for the entity in the query when the auto-generated one should not be used.
     */
    public <T> TypedQuery<T> createQuery(final Class<T> entityType, final String querySuffix, final String entityAlias) {
        return wrappedManager.createQuery(entityType, querySuffix, entityAlias);
    }
    
    /**
     * Sets the database to which to connect with this manager.
     * 
     * @param connectionProviderType The type of provider for the database connection.
     * @param databaseUrl The URL to the database.
     * @param connectionParams Parameters for the connection.
     */
    public void setDatabase(final Class<? extends AbstractDbConnectionProvider> connectionProviderType, 
                            final String databaseUrl, 
                            final String... connectionParams) {
        wrappedManager.setDatabase(connectionProviderType, databaseUrl, connectionParams);
    }
    
    /**
     * If the underlying {@link DbConnectionProvider} can create a database backup.
     */
    public boolean isCanBackupDatabase() {
        return wrappedManager.isCanBackupDatabase();
    }
    
    /**
     * Uses the underlying {@link DbConnectionProvider} to create a database backup.
     * 
     * @param databaseVersion A version indicator for the backed up database.
     */
    public void backupDatabase(final CharSequence databaseVersion) {
        wrappedManager.backupDatabase(databaseVersion);
    }
    
    /**
     * Checks, if the database is reachable.
     * 
     * @return {@code true} when reachable.
     */
    public boolean pingDatabase() {
        return wrappedManager.pingDatabase();
    }
}
