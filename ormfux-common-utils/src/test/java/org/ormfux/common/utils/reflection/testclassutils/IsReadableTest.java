package org.ormfux.common.utils.reflection.testclassutils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.ormfux.common.utils.reflection.ClassUtils;

public class IsReadableTest {
    
    @Test
    public void testReadableThroughAccessor() {
        assertTrue(ClassUtils.isReadable(AccessorProperty.class, "property"));
        assertTrue(ClassUtils.isReadable(AccessorOnlyProperty.class, "property"));
        assertTrue(ClassUtils.isReadable(InheritedAccessorProperty.class, "property"));
        assertTrue(ClassUtils.isReadable(AccessorAddedProperty.class, "property"));
    }
    
    @Test
    public void testReadablePublic() {
        assertTrue(ClassUtils.isReadable(PublicProperty.class, "property"));
        assertTrue(ClassUtils.isReadable(InheritedPublicProperty.class, "property"));
    }
    
    @Test
    public void testNonReadable() {
        assertFalse(ClassUtils.isReadable(PrivateProperty.class, "property"));
        assertFalse(ClassUtils.isReadable(InheritedPrivateProperty.class, "property"));
    }
    
    @Test
    public void testNonExistingProperty() {
        assertFalse(ClassUtils.isReadable(AccessorProperty.class, "nonexisting"));
        assertFalse(ClassUtils.isReadable(PublicProperty.class, "nonexisting"));
        assertFalse(ClassUtils.isReadable(PrivateProperty.class, "nonexisting"));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullObject() {
        ClassUtils.isReadable(null, "property");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullPropertyName() {
        ClassUtils.isReadable(AccessorProperty.class, null);
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
