package org.ormfux.common.db.query.testabstractquery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.ormfux.common.db.exception.DuplicateParamException;
import org.ormfux.common.db.query.AbstractQuery;
import org.ormfux.common.utils.object.Objects;

public class AddParametersTest extends AbstractAbstractQueryTest {
    
    @Test
    public void testAddParameters() {
        AbstractQuery query = new MockQuery(new MockConnectionProvider());
        Map<String, Object> params = getParams(query);
        assertEquals(0, params.size());
        
        Map<String, Object> addedParams = new HashMap<>();
        addedParams.put("param1", query);
        
        query.addParameters(addedParams);
        assertEquals(1, params.size());
        assertTrue(Objects.isSame(query, params.get("param1")));
        
        addedParams = new HashMap<>();
        addedParams.put("paramName", "second param");
        addedParams.put("nullParam", null);
        
        query.addParameters(addedParams);
        assertEquals(3, params.size());
        assertTrue(Objects.isSame(query, params.get("param1")));
        assertEquals("second param", params.get("paramName"));
        assertTrue(params.containsKey("nullParam"));
        assertNull(params.get("nullParam"));
    }
    
    @Test
    public void testAddParametersEmpty() {
        AbstractQuery query = new MockQuery(new MockConnectionProvider());
        Map<String, Object> params = getParams(query);
        assertEquals(0, params.size());
        
        query.addParameters(new HashMap<>());
        assertEquals(0, params.size());
    }
    
    @Test(expected = NullPointerException.class)
    public void testAddParametersNull() {
        AbstractQuery query = new MockQuery(new MockConnectionProvider());
        Map<String, Object> params = getParams(query);
        assertEquals(0, params.size());
        
        query.addParameters(null);
    }
    
    @Test(expected = DuplicateParamException.class)
    public void testDuplicateParamName() {
        AbstractQuery query = new MockQuery(new MockConnectionProvider());
        Map<String, Object> params = getParams(query);
        assertEquals(0, params.size());
        
        query.addParameter("param1", query);
        assertEquals(1, params.size());
        assertTrue(Objects.isSame(query, params.get("param1")));
        
        Map<String, Object> addedParams = new HashMap<>();
        addedParams.put("param1", "duplicate param name");
        
        query.addParameters(addedParams);
    }
    
}
