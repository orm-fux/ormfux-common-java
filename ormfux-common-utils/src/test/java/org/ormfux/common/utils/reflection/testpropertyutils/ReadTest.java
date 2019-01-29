package org.ormfux.common.utils.reflection.testpropertyutils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.ormfux.common.utils.reflection.PropertyUtils;

public class ReadTest {
    
    @Test
    public void testReadThroughAccessor() {
        assertEquals("valuethroughaccessor", PropertyUtils.read(new AccessorProperty(), "property"));
        assertEquals("valueaccessor", PropertyUtils.read(new AccessorOnlyProperty(), "property"));
        assertEquals("valuethroughaccessor", PropertyUtils.read(new InheritedAccessorProperty(), "property"));
        assertEquals("valueadded", PropertyUtils.read(new AccessorAddedProperty(), "property"));
    }
    
    @Test
    public void testReadPublic() {
        assertEquals("value", PropertyUtils.read(new PublicProperty(), "property"));
        assertEquals("value", PropertyUtils.read(new InheritedPublicProperty(), "property"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNonReadable() {
        PropertyUtils.read(new PrivateProperty(), "property");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNonReadableInherited() {
        PropertyUtils.read(new InheritedPrivateProperty(), "property");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNonExistingAccessor() {
        PropertyUtils.read(new AccessorProperty(), "nonexisting");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNonExistingPublic() {
        PropertyUtils.read(new PublicProperty(), "nonexisting");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNonExistingPrivate() {
        PropertyUtils.read(new PrivateProperty(), "nonexisting");
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullObject() {
        PropertyUtils.read(null, "property");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullPropertyName() {
        PropertyUtils.read(new AccessorProperty(), null);
    }
    
    public static class AccessorProperty {
        
        private String property = "value";
        
        
        public String getProperty() {
            return property + "throughaccessor";
        }
        
    }
    
    public static class InheritedAccessorProperty extends AccessorProperty {}
    
    public static class PublicProperty {
        
        public String property = "value";
        
    }
    
    public static class InheritedPublicProperty extends PublicProperty {}
    
    public static class PrivateProperty {
        @SuppressWarnings("unused")
        private String property = "value";
    }
    
    public static class InheritedPrivateProperty extends PrivateProperty {}
    
    public static class AccessorAddedProperty extends PrivateProperty {
        public String getProperty() {
            return "valueadded";
        }
        
    }
    
    public static class AccessorOnlyProperty {
        public String getProperty() {
            return "valueaccessor";
        }
    }
    
    
}
