package org.ormfux.common.db.investigations.h2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.h2.Driver;
import org.junit.Test;

public class H2Test {
    
    @Test
    public void testSetupDatabase() throws ClassNotFoundException, SQLException {
        Class.forName(Driver.class.getName()); //TODO "forName" required?
        
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=MYSQL;DATABASE_TO_UPPER=false");// "jdbc:h2://" + System.getProperty("user.dir") + "/target/test_setup_db;MODE=MYSQL");
        
        final StringBuilder createQuery = new StringBuilder();
        createQuery.append("create table ")
                   .append("category (")
                   .append("id varchar(255) not null, ")
                   .append("creationDate datetime, ")
                   .append("modificationDate datetime, ")
                   .append("version bigint not null, ")
                   .append("name varchar(255) not null, ")
                   .append("type varchar(255) not null, ")
                   .append("parent varchar(255), ")
                   .append("primary key (id), ")
                   .append("constraint uk_cat_name_type unique (name, type), ")
                   .append("constraint fk_cat_cat_parent foreign key (parent) references category (id) ")
                   .append(");");
        
//      Statement sqlStatement = connection.createStatement();
//        sqlStatement.executeUpdate(createQuery.toString());
//      sqlStatement.executeQuery("select * from category");
        
        final PreparedStatement preparedStament = connection.prepareStatement(createQuery.toString());
        preparedStament.addBatch();
        preparedStament.executeBatch();
//        preparedStament.close();
        
        connection.prepareStatement("select * from category").executeQuery();
        
        connection.close();
        
        connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=MYSQL;DATABASE_TO_UPPER=false");
        connection.prepareStatement("select * from category").executeQuery();
//        sqlStatement = connection.createStatement();
//        sqlStatement.executeQuery("select * from category");
        connection.close();
    }
    
}
