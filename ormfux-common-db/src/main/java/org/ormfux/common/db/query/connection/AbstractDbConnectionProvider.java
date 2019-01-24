package org.ormfux.common.db.query.connection;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.ormfux.common.db.exception.SQLException;

/**
 * Base for database connection providers.
 */
public abstract class AbstractDbConnectionProvider implements DbConnectionProvider {
    
    /**
     * The URL to the database.
     */
    private final String databaseUrl;
    
    /**
     * Parameters for the connection.
     */
    private final List<String> connectionParams;
    
    /**
     * @param driverName The fully qualified class name of the database driver.
     * @param databaseUrl The URL to the database.
     */
    public AbstractDbConnectionProvider(final String driverName, final String databaseUrl, final String... connectionParams) {
        this.databaseUrl = databaseUrl;
        
        if (connectionParams == null) {
            this.connectionParams = Collections.emptyList();
        } else {
            this.connectionParams = Arrays.asList(connectionParams);
        }
        
        try {
            Class.forName(driverName);
        } catch (final ClassNotFoundException e) {
            throw new SQLException("Could not load H2 database driver.", e);
        }
    }
    
    /**
     * The URL to the database.
     */
    protected final String getDatabaseUrl() {
        return databaseUrl;
    } 
    
    /**
     * Parameters for the connection.
     */
    protected final List<String> getConnectionParams() {
        return connectionParams;
    }
    
    /**
     * {@inheritDoc}
     * 
     * @throws UnsupportedOperationException when {@link #isCanBackupDatabase()} is {@code false}.
     */
    @Override
    public final void backupDatabase(final CharSequence databaseVersion) {
        if (!isCanBackupDatabase()) {
            throw new UnsupportedOperationException("Database backup is not supported.");
        } else {
            doBackupDatabase(databaseVersion);
        }
    }
    
    /**
     * Conducts the actual database backup.
     */
    protected void doBackupDatabase(final CharSequence databaseVersion) {
        //let sub-classes realize this in an optional fashion.
    }
    
    /**
     * A connection to the database.
     */
    public abstract Connection getConnection();
    
    /**
     * Closes all currently open database connections.
     */
    public abstract void closeAllConnections();
}
