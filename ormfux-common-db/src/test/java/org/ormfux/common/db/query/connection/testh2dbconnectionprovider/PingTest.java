package org.ormfux.common.db.query.connection.testh2dbconnectionprovider;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;
import org.ormfux.common.db.query.connection.H2DbConnectionProvider;

public class PingTest extends AbstractH2ConnectionProviderTest {
    
    @Before
    public void beforeTest() {
        super.beforeTest();
        setDbFilesDir("build/h2testfiles/ping");
    }
    
    @Test
    public void testPingFileConnection() throws SQLException {
        final String h2Url = "./" + getDbFilesDir().getPath() + "/pingtest";
        File h2File = new File(h2Url + ".mv.db");
        
        H2DbConnectionProvider connectionProvider = new H2DbConnectionProvider("jdbc:h2:file:" + h2Url);
        
        assertFalse(h2File.isFile());
        assertFalse(connectionProvider.ping());
        
        Connection connection = connectionProvider.getConnection();
        
        //we need to actually do something to create the database file
        Statement statement = connection.createStatement();
        statement.executeUpdate("create table test (id varchar(255))");
        connection.commit();
        
        assertTrue(h2File.isFile());
        assertTrue(connectionProvider.ping());
        
        shutdownDb(statement);
    }
    
    @Test
    public void testPingInMemoryConnection() throws SQLException {
        final String h2Url = "inmemping";
        
        H2DbConnectionProvider connectionProvider = new H2DbConnectionProvider("jdbc:h2:mem:" + h2Url);
        Connection connection = connectionProvider.getConnection();
        
        //we need to actually do something to create the database file
        Statement statement = connection.createStatement();
        statement.executeUpdate("create table test (id varchar(255))");
        connection.commit();
        
        assertFalse(connectionProvider.ping());
        
        shutdownDb(statement);
    }
}
