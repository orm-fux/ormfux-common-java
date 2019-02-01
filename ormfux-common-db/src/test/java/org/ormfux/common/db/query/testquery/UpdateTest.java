package org.ormfux.common.db.query.testquery;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;
import org.ormfux.common.db.query.Query;

public class UpdateTest extends AbstractQueryTest {
    
    public UpdateTest() {
        super("nativeupdatedb");
    }
    
    @Test
    public void testUpdateExisting() throws SQLException {
        Query query = queryManager.createQuery("update mock set id = 'newid' where id = 'idvalue'");
        query.executeUpdate();
        
        Connection connection = getDbConnection();
        Statement statement = connection.createStatement();
        ResultSet updateResult = statement.executeQuery("select id from mock where id = 'newid'");
        assertTrue(updateResult.next());
        assertFalse(updateResult.next());
        
        updateResult.close();
        statement.close();
        connection.close();
    }
    
    @Test
    public void testCreateNew() throws SQLException {
        Query query = queryManager.createQuery("insert into mock (id) values ('newid')");
        query.executeUpdate();
        
        Connection connection = getDbConnection();
        Statement statement = connection.createStatement();
        ResultSet updateResult = statement.executeQuery("select id from mock where id = 'newid'");
        assertTrue(updateResult.next());
        assertFalse(updateResult.next());
        
        updateResult.close();
        statement.close();
        connection.close();
    }
    
    @Test
    public void testDelete() throws SQLException {
        Connection connection = getDbConnection();
        Statement statement = connection.createStatement();
        ResultSet updateResult = statement.executeQuery("select id from mock where id = 'idvalue'");
        assertTrue(updateResult.next());
        assertFalse(updateResult.next());
        updateResult.close();
        
        Query query = queryManager.createQuery("delete from mock where id = 'idvalue'");
        query.executeUpdate();
        
        updateResult = statement.executeQuery("select id from mock where id = 'idvalue'");
        assertFalse(updateResult.next());
        
        updateResult.close();
        statement.close();
        connection.close();
    }
    
}
