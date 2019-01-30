package org.ormfux.common.utils.reflection.testclassutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.ormfux.common.utils.reflection.ClassUtils;
import org.ormfux.common.utils.reflection.exception.InstantiationException;

public class CreateObjectTest {
    
    @Test
    public void testNoArgumentConstructor() {
        Mock mock = ClassUtils.createObject(Mock.class);
        assertNotNull(mock);
        assertEquals("noarg", mock.getProperty());
        
        mock = ClassUtils.createObject(Mock.class, Collections.emptyList(), Collections.emptyList());
        assertNotNull(mock);
        assertEquals("noarg", mock.getProperty());
        
        mock = ClassUtils.createObject(Mock.class, null, Collections.emptyList());
        assertNotNull(mock);
        assertEquals("noarg", mock.getProperty());
        
        mock = ClassUtils.createObject(Mock.class, null, null);
        assertNotNull(mock);
        assertEquals("noarg", mock.getProperty());
    }
    
    @Test
    public void testConstructorWithArguments() {
        Mock mock = ClassUtils.createObject(Mock.class, Arrays.asList(String.class, int.class), Arrays.asList("stringValue", 3));
        assertNotNull(mock);
        assertEquals("argStringInt-stringValue3", mock.getProperty());
        
        mock = ClassUtils.createObject(Mock.class, Arrays.asList(int.class, String.class), Arrays.asList(3, "stringValue"));
        assertNotNull(mock);
        assertEquals("argIntString-3stringValue", mock.getProperty());
        
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMissingArgumentValues() {
        ClassUtils.createObject(Mock.class, Arrays.asList(String.class, int.class), Arrays.asList("stringValue"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullArgumentValuesList() {
        ClassUtils.createObject(Mock.class, Arrays.asList(String.class, int.class), null);
    }
    
    @Test(expected = InstantiationException.class)
    public void testUndefinedConstructor() {
        ClassUtils.createObject(Mock.class, Arrays.asList(String.class), Arrays.asList("value"));
    }
    
    @Test(expected = InstantiationException.class)
    public void testAbstractClass() {
        ClassUtils.createObject(AbstractMock.class);
    }
    
    @Test(expected = InstantiationException.class)
    public void testInterface() {
        ClassUtils.createObject(Interface.class);
    }
    
    @Test(expected = InstantiationException.class)
    public void testPrivateConstructor() {
        ClassUtils.createObject(PrivateConstructor.class);
    }
    
    public static class Mock {
        
        private String property; 
        
        
        public Mock() {
            this.property = "noarg";
        }
        
        public Mock(String val1, int val2) {
            this.property = "argStringInt-" + val1 + val2;
        }
        
        public Mock(int val1, String val2) {
            this.property = "argIntString-" + val1 + val2;
        }
        
        
        public String getProperty() {
            return property;
        }
    }
    
    public static abstract class AbstractMock {
        
    }
    
    public static interface Interface {
        
    }
    
    public static class PrivateConstructor {
        
        private PrivateConstructor() {
        }
    }
    
}
