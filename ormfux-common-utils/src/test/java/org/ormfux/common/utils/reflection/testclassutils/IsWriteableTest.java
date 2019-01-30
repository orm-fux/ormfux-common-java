package org.ormfux.common.utils.reflection.testclassutils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.ormfux.common.utils.reflection.ClassUtils;

public class IsWriteableTest {
    
    @Test
    public void testWriteableThroughAccessor() {
        assertTrue(ClassUtils.isWriteable(AccessorProperty.class, "property"));
        assertTrue(ClassUtils.isWriteable(AccessorOnlyProperty.class, "property"));
        assertTrue(ClassUtils.isWriteable(InheritedAccessorProperty.class, "property"));
        assertTrue(ClassUtils.isWriteable(AccessorAddedProperty.class, "property"));
        
        assertTrue(ClassUtils.isWriteable(AccessorProperty.class, "property", 1));
        assertTrue(ClassUtils.isWriteable(AccessorOnlyProperty.class, "property", 1));
        assertTrue(ClassUtils.isWriteable(InheritedAccessorProperty.class, "property", 1));
        assertTrue(ClassUtils.isWriteable(AccessorAddedProperty.class, "property", 1));
        
        assertFalse(ClassUtils.isWriteable(AccessorProperty.class, "property", "wrongtype"));
        assertFalse(ClassUtils.isWriteable(AccessorOnlyProperty.class, "property", "wrongtype"));
        assertFalse(ClassUtils.isWriteable(InheritedAccessorProperty.class, "property", "wrongtype"));
        assertFalse(ClassUtils.isWriteable(AccessorAddedProperty.class, "property", "wrongtype"));
    }
    
    @Test
    public void testWriteablePublic() {
        assertTrue(ClassUtils.isWriteable(PublicProperty.class, "property"));
        assertTrue(ClassUtils.isWriteable(InheritedPublicProperty.class, "property"));
        
        assertTrue(ClassUtils.isWriteable(PublicProperty.class, "property", 1));
        assertTrue(ClassUtils.isWriteable(InheritedPublicProperty.class, "property", 1));
        
        assertFalse(ClassUtils.isWriteable(PublicProperty.class, "property", "wrongtype"));
        assertFalse(ClassUtils.isWriteable(InheritedPublicProperty.class, "property", "wrongtype"));
    }
    
    @Test
    public void testNonWriteable() {
        assertFalse(ClassUtils.isWriteable(PrivateProperty.class, "property"));
        assertFalse(ClassUtils.isWriteable(InheritedPrivateProperty.class, "property"));
        
        assertFalse(ClassUtils.isWriteable(PrivateProperty.class, "property", 1));
        assertFalse(ClassUtils.isWriteable(InheritedPrivateProperty.class, "property", 1));
        
        assertFalse(ClassUtils.isWriteable(PrivateProperty.class, "property", "wrongtype"));
        assertFalse(ClassUtils.isWriteable(InheritedPrivateProperty.class, "property", "wrongtype"));
    }
    
    @Test
    public void testNonExistingProperty() {
        assertFalse(ClassUtils.isWriteable(AccessorProperty.class, "nonexisting"));
        assertFalse(ClassUtils.isWriteable(PublicProperty.class, "nonexisting"));
        assertFalse(ClassUtils.isWriteable(PrivateProperty.class, "nonexisting"));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullObject() {
        ClassUtils.isWriteable(null, "property");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullPropertyName() {
        ClassUtils.isWriteable(AccessorProperty.class, null);
    }
    
    public static class AccessorProperty {
        
        @SuppressWarnings("unused")
        private int property;
        
        public void setProperty(int property) {
            this.property = property;
        }
        
    }
    
    public static class InheritedAccessorProperty extends AccessorProperty {}
    
    public static class PublicProperty {
        
        public int property;
        
    }
    
    public static class InheritedPublicProperty extends PublicProperty {}
    
    public static class PrivateProperty {
        @SuppressWarnings("unused")
        private int property;
    }
    
    public static class InheritedPrivateProperty extends PrivateProperty {}
    
    public static class AccessorAddedProperty extends PrivateProperty {
        public void setProperty(int property) {
            
        }
    }
    
    public static class AccessorOnlyProperty {
        public void setProperty(int property) {
            
        }
    }
    
    
}
