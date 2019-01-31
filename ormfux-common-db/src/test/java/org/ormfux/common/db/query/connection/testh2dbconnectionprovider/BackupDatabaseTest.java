package org.ormfux.common.db.query.connection.testh2dbconnectionprovider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.ormfux.common.db.query.connection.H2DbConnectionProvider;

public class BackupDatabaseTest extends AbstractH2ConnectionProviderTest {
    
    @Before
    public void beforeTest() {
        super.beforeTest();
        setDbFilesDir("build/h2testfiles/backupdb");
    }
    
    @Test
    public void testBackup() throws SQLException, ParseException, IOException {
        final String h2Url = "./" + getDbFilesDir().getPath() + "/dbtobackup";
        File h2File = new File(h2Url + ".mv.db");
        
        H2DbConnectionProvider connectionProvider = new H2DbConnectionProvider("jdbc:h2:file:" + h2Url);
        Connection connection = connectionProvider.getConnection();
        
        //we need to actually do something to create the database file
        Statement statement = connection.createStatement();
        statement.executeUpdate("create table test (id varchar(255))");
        connection.commit();
        
        assertTrue(h2File.isFile());
        
        statement.executeUpdate("insert into test (id) values ('testid')");
        connection.commit();
        
        connectionProvider.backupDatabase("versionNumber");
        
        assertTrue(connection.isClosed());
        
        File h2BackupFile = null;
        
        for (File file : getDbFilesDir().listFiles()) {
            if (file.getName().startsWith("dbtobackup.mv.db.versionNumber.")) {
                h2BackupFile = file;
                break;
            }
        }
        
        assertNotNull(h2BackupFile);
        //time stamp at end of file name
        Date timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").parse(h2BackupFile.getName().substring(h2BackupFile.getName().lastIndexOf('.') + 1));
        assertTrue(new Date().getTime() - timestamp.getTime() < 2000);
        
        assertEquals(h2File.length(), h2BackupFile.length());
        
        try (InputStream original = new FileInputStream(h2File); InputStream backup = new FileInputStream(h2BackupFile)) {
            int lastOriginalByte;
            
            while ((lastOriginalByte = original.read()) != -1) {
                assertEquals(lastOriginalByte, backup.read());
            }
            
            assertEquals(-1, backup.read());
            
        } 
    }
    
    @Test
    public void testBackupNotSupported() throws SQLException {
        final String h2Url = "nobackup";
        
        H2DbConnectionProvider connectionProvider = new H2DbConnectionProvider("jdbc:h2:mem:" + h2Url);
        
        try {
            connectionProvider.backupDatabase("version");
            fail("Expecting UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {}
        
        Connection connection = connectionProvider.getConnection();
        
        Statement statement = connection.createStatement();
        shutdownDb(statement);
    }
}
