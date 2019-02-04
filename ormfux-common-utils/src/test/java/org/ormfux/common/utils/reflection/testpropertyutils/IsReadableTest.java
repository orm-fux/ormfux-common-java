package org.ormfux.common.utils.reflection.testpropertyutils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.ormfux.common.utils.reflection.PropertyUtils;

public class IsReadableTest {
    
    @Test
    public void testReadableThroughAccessor() {
        assertTrue(PropertyUtils.isReadable(new AccessorProperty(), "property"));
        assertTrue(PropertyUtils.isReadable(new AccessorOnlyProperty(), "property"));
        assertTrue(PropertyUtils.isReadable(new InheritedAccessorProperty(), "property"));
        assertTrue(PropertyUtils.isReadable(new AccessorAddedProperty(), "property"));
    }
    
    @Test
    public void testReadablePublic() {
        assertTrue(PropertyUtils.isReadable(new PublicProperty(), "property"));
        assertTrue(PropertyUtils.isReadable(new InheritedPublicProperty(), "property"));
    }
    
    @Test
    public void testNonReadable() {
        assertFalse(PropertyUtils.isReadable(new PrivateProperty(), "property"));
        assertFalse(PropertyUtils.isReadable(new InheritedPrivateProperty(), "property"));
    }
    
    @Test
    public void testNonExistingProperty() {
        assertFalse(PropertyUtils.isReadable(new AccessorProperty(), "nonexisting"));
        assertFalse(PropertyUtils.isReadable(new PublicProperty(), "nonexisting"));
        assertFalse(PropertyUtils.isReadable(new PrivateProperty(), "nonexisting"));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullObject() {
        PropertyUtils.isReadable(null, "property");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullPropertyName() {
        PropertyUtils.isReadable(new AccessorProperty(), null);
    }
    
    public static class AccessorProperty {
        
        private String property;
        
        
        public String getProperty() {
            return property;
        }
        
        public void setProperty(String property) {
            this.property = property;
        }
        
    }
    
    public static class InheritedAccessorProperty extends AccessorProperty {}
    
    public static class PublicProperty {
        
        public String property;
        
    }
    
    public static class InheritedPublicProperty extends PublicProperty {}
    
    public static class PrivateProperty {
        @SuppressWarnings("unused")
        private String property;
    }
    
    public static class InheritedPrivateProperty extends PrivateProperty {}
    
    public static class AccessorAddedProperty extends PrivateProperty {
        public String getProperty() {
            return null;
        }
        
        public void setProperty(String property) {
            
        }
    }
    
    public static class AccessorOnlyProperty {
        public String getProperty() {
            return null;
        }
        
        public void setProperty(String property) {
            
        }
    }
    
    
}
