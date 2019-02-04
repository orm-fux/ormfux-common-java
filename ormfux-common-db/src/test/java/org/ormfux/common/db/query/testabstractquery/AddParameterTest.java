package org.ormfux.common.db.query.testabstractquery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.ormfux.common.db.exception.DuplicateParamException;
import org.ormfux.common.db.query.AbstractQuery;
import org.ormfux.common.utils.object.Objects;

public class AddParameterTest extends AbstractAbstractQueryTest {
    
    @Test
    public void testAddParameter() {
        AbstractQuery query = new MockQuery(new MockConnectionProvider());
        Map<String, Object> params = getParams(query);
        assertEquals(0, params.size());
        
        query.addParameter("param1", query);
        assertEquals(1, params.size());
        assertTrue(Objects.isSame(query, params.get("param1")));
        
        query.addParameter("paramName", "second param");
        assertEquals(2, params.size());
        assertTrue(Objects.isSame(query, params.get("param1")));
        assertEquals("second param", params.get("paramName"));
        
        query.addParameter("nullParam", null);
        assertEquals(3, params.size());
        assertTrue(Objects.isSame(query, params.get("param1")));
        assertEquals("second param", params.get("paramName"));
        assertTrue(params.containsKey("nullParam"));
        assertNull(params.get("nullParam"));
    }
    
    @Test(expected = DuplicateParamException.class)
    public void testDuplicateParamName() {
        AbstractQuery query = new MockQuery(new MockConnectionProvider());
        Map<String, Object> params = getParams(query);
        assertEquals(0, params.size());
        
        query.addParameter("param1", query);
        assertEquals(1, params.size());
        assertTrue(Objects.isSame(query, params.get("param1")));
        
        query.addParameter("param1", "duplicate param name");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyParamName() {
        AbstractQuery query = new MockQuery(new MockConnectionProvider());
        query.addParameter(null, "value");
    }
}
