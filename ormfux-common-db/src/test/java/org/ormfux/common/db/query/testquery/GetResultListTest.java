package org.ormfux.common.db.query.testquery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Iterator;

import org.junit.Test;
import org.ormfux.common.db.exception.NonMatchedParamException;
import org.ormfux.common.db.exception.SQLException;
import org.ormfux.common.db.query.Query;
import org.ormfux.common.db.query.QueryResult;
import org.ormfux.common.db.query.QueryResult.QueryResultRow;

public class GetResultListTest extends AbstractQueryTest {
    
    public GetResultListTest() {
        super("nativeresultlistdb");
    }

    @Test
    public void testWithoutLimitation() {
        Query query = queryManager.createQuery("select id from mock order by id");
        QueryResult resultList = query.getResultList();
        
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        
        Iterator<QueryResultRow> resultIterator = resultList.iterator();
        QueryResultRow resultRow = resultIterator.next();
        assertEquals("idvalue", resultRow.getValue(0));
        assertEquals("idvalue", resultRow.getValue("id"));
        assertFalse(resultIterator.hasNext());
        
        queryManager.createQuery("insert into mock (id) values ('id2')").executeUpdate();
        
        resultList = query.getResultList();
        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        
        resultIterator = resultList.iterator();
        resultRow = resultIterator.next();
        assertEquals("id2", resultRow.getValue(0));
        assertEquals("id2", resultRow.getValue("id"));
        resultRow = resultIterator.next();
        assertEquals("idvalue", resultRow.getValue(0));
        assertEquals("idvalue", resultRow.getValue("id"));
        assertFalse(resultIterator.hasNext());
        
    }
    
    @Test
    public void testWithLimitation() {
        queryManager.createQuery("insert into mock (id) values ('id2')").executeUpdate();
        
        Query query = queryManager.createQuery("select id from mock where id = :id");
        query.addParameter("id", "idvalue");
        
        QueryResult resultList = query.getResultList();
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        Iterator<QueryResultRow> resultIterator = resultList.iterator();
        QueryResultRow resultRow = resultIterator.next();
        assertEquals("idvalue", resultRow.getValue(0));
        assertEquals("idvalue", resultRow.getValue("id"));
        assertFalse(resultIterator.hasNext());
        
    }
    
    @Test
    public void testWithLimitationNoResult() {
        queryManager.createQuery("insert into mock (id) values ('id2')").executeUpdate();
        
        Query query = queryManager.createQuery("select id from mock where id = :id");
        query.addParameter("id", "nonexisting");
        
        QueryResult resultList = query.getResultList();
        assertNotNull(resultList);
        assertEquals(0, resultList.size());
    }
    
    @Test(expected = SQLException.class)
    public void testParamValueMissing() {
        Query query = queryManager.createQuery("select id from mock where id = :id");
        
        query.getResultList();
    }
    
    @Test(expected = NonMatchedParamException.class)
    public void testTooManyParams() {
        Query query = queryManager.createQuery("select id from mock where id = :id");
        query.addParameter("id", "id");
        query.addParameter("id2", "id");
        
        query.getResultList();
    }
    
}
