package org.ormfux.common.db.query.connection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import org.ormfux.common.db.exception.SQLException;
import org.ormfux.common.utils.DateUtils;
import org.h2.engine.Constants;
import org.h2.store.fs.FileUtils;

/**
 * Provider for H2 database connections.
 */
public class H2DbConnectionProvider extends AbstractDbConnectionProvider {
    
    /**
     * Prefix for the database URL indicating that it is a file database.
     */
    private static final String FILE_DB_INDICATOR = "jdbc:h2:file:";
    
    /**
     * The URL used to connect to the H2 database.
     */
    private final String connectionUrl;
    
    /**
     * @param databaseFile The path to the H2 database file.
     */
    public H2DbConnectionProvider(final String databaseFile, final String... connectionParams) {
        super("org.h2.Driver", databaseFile, connectionParams);
        
        this.connectionUrl = assembleConnectionUrl(databaseFile, connectionParams);
    }
    
    /**
     * Add the parameters to the database "URL".
     * 
     * @param databaseFile The database "URL".
     * @param connectionParams The params for the URL.
     * @return The URL with the params.
     */
    private String assembleConnectionUrl(final String databaseFile, final String... connectionParams) {
        if (connectionParams == null) {
            return databaseFile;
        } else {
            final StringBuilder connectionBuilder = new StringBuilder(databaseFile);
            
            for (final String connectionParam : connectionParams) {
                connectionBuilder.append(';').append(connectionParam);
            }
            
            return connectionBuilder.toString();
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(connectionUrl);
        } catch (final java.sql.SQLException e) {
            throw new SQLException("Could not open a new SQL connection", e);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void closeAllConnections() {
        
        try {
            final Connection closeConnection = getConnection();
            final Statement closeStatement = closeConnection.createStatement();
            closeStatement.execute("SHUTDOWN");
            closeStatement.close();
            closeConnection.close();
        } catch (java.sql.SQLException e) {
            throw new SQLException("Error closing all connections.", e);
        }
    }
    
    /**
     * {@inheritDoc}
     * 
     * @return {@code true} when the connection is to a file database.
     */
    @Override
    public boolean isCanBackupDatabase() {
        return getDatabaseUrl().startsWith(FILE_DB_INDICATOR);
    }
    
    /** {@inheritDoc} */
    @Override
    protected void doBackupDatabase(final CharSequence databaseVersion) {
        closeAllConnections();
        
        final String dbFile = getDbFile();
        final String backupTimestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(DateUtils.now());
        
        try {
            final String pageDbFile = dbFile + Constants.SUFFIX_PAGE_FILE;
            final String pageDbFileBackup = pageDbFile + "." + databaseVersion + "." + backupTimestamp;
            
            if (FileUtils.exists(pageDbFile) && !FileUtils.exists(pageDbFileBackup)) {
                Files.copy(Paths.get(pageDbFile), Paths.get(pageDbFileBackup));
            }
            
            final String mvDbFile = dbFile + Constants.SUFFIX_MV_FILE;
            final String mvDbFileBackup = mvDbFile + "." + databaseVersion + "." + backupTimestamp;
            
            if (FileUtils.exists(mvDbFile) && !FileUtils.exists(mvDbFileBackup)) {
                Files.copy(Paths.get(mvDbFile), Paths.get(mvDbFileBackup));
            }
        } catch (final IOException e) {
            throw new SQLException("Error creating backup of database.", e);
        }
    }
    
    /**
     * Checks, if the database file exists.
     */
    @Override
    public boolean ping() {
        final String dbFile = getDbFile();
        
        return FileUtils.exists(dbFile + Constants.SUFFIX_PAGE_FILE) 
                || FileUtils.exists(dbFile + Constants.SUFFIX_MV_FILE);
    }
    
    /**
     * The file representing the database.
     */
    private String getDbFile() {
        return getDatabaseUrl().substring(FILE_DB_INDICATOR.length());
    }
}
