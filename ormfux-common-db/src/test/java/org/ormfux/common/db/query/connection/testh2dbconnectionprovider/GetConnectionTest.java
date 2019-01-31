package org.ormfux.common.db.query.connection.testh2dbconnectionprovider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;
import org.ormfux.common.db.query.connection.H2DbConnectionProvider;

public class GetConnectionTest extends AbstractH2ConnectionProviderTest {
    
    @Before
    public void beforeTest() {
        super.beforeTest();
        setDbFilesDir("build/h2testfiles/connection");
    }
    
    @Test
    public void testFileConnection() throws SQLException {
        final String h2Url = "./" + getDbFilesDir().getPath() + "/connectiontest";
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
        
        ResultSet queryResult = statement.executeQuery("select * from test");
        queryResult.first();
        assertEquals("testid", queryResult.getString(1));
        
        queryResult.close();
        shutdownDb(statement);
    }
    
    @Test
    public void testInMemoryConnection() throws SQLException {
        final String h2Url = "inmem";
        
        H2DbConnectionProvider connectionProvider = new H2DbConnectionProvider("jdbc:h2:mem:" + h2Url);
        Connection connection = connectionProvider.getConnection();
        
        //we need to actually do something to create the database file
        Statement statement = connection.createStatement();
        statement.executeUpdate("create table test (id varchar(255))");
        connection.commit();
        
        statement.executeUpdate("insert into test (id) values ('testid')");
        connection.commit();
        
        ResultSet queryResult = statement.executeQuery("select * from test");
        queryResult.first();
        assertEquals("testid", queryResult.getString(1));
        
        queryResult.close();
        shutdownDb(statement);
    }
    
}
