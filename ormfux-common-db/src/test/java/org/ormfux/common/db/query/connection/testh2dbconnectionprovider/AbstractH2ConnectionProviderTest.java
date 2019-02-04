package org.ormfux.common.db.query.connection.testh2dbconnectionprovider;

import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;

public abstract class AbstractH2ConnectionProviderTest {
    
    private File dbFilesDir = new File("build/h2testfiles/connection");
    
    @Before
    public void beforeTest() {
        cleanDbFiles();
        
        if (!dbFilesDir.isDirectory()) {
            dbFilesDir.mkdirs();
        }
    }
    
    @After
    public void afterTest() {
        cleanDbFiles();
    }
    
    protected void shutdownDb(Statement statement) throws SQLException {
        statement.execute("SHUTDOWN");
        statement.close();
    }
    
    private void cleanDbFiles() {
        if (dbFilesDir.isDirectory()) {
            for (File file : dbFilesDir.listFiles()) {
                file.delete();
            }
            
            dbFilesDir.delete();
        }
    }
    
    protected void setDbFilesDir(String dirPath) {
        dbFilesDir = new File(dirPath);
    }
    
    protected File getDbFilesDir() {
        return dbFilesDir;
    }
    
}
