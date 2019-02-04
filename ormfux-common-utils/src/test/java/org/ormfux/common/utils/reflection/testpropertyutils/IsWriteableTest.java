package org.ormfux.common.utils.reflection.testpropertyutils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.ormfux.common.utils.reflection.PropertyUtils;

public class IsWriteableTest {
    
    @Test
    public void testWriteableThroughAccessor() {
        assertTrue(PropertyUtils.isWriteable(new AccessorProperty(), "property"));
        assertTrue(PropertyUtils.isWriteable(new AccessorOnlyProperty(), "property"));
        assertTrue(PropertyUtils.isWriteable(new InheritedAccessorProperty(), "property"));
        assertTrue(PropertyUtils.isWriteable(new AccessorAddedProperty(), "property"));
        
        assertTrue(PropertyUtils.isWriteable(new AccessorProperty(), "property", 1));
        assertTrue(PropertyUtils.isWriteable(new AccessorOnlyProperty(), "property", 1));
        assertTrue(PropertyUtils.isWriteable(new InheritedAccessorProperty(), "property", 1));
        assertTrue(PropertyUtils.isWriteable(new AccessorAddedProperty(), "property", 1));
        
        assertFalse(PropertyUtils.isWriteable(new AccessorProperty(), "property", "wrongtype"));
        assertFalse(PropertyUtils.isWriteable(new AccessorOnlyProperty(), "property", "wrongtype"));
        assertFalse(PropertyUtils.isWriteable(new InheritedAccessorProperty(), "property", "wrongtype"));
        assertFalse(PropertyUtils.isWriteable(new AccessorAddedProperty(), "property", "wrongtype"));
    }
    
    @Test
    public void testWriteablePublic() {
        assertTrue(PropertyUtils.isWriteable(new PublicProperty(), "property"));
        assertTrue(PropertyUtils.isWriteable(new InheritedPublicProperty(), "property"));
        
        assertTrue(PropertyUtils.isWriteable(new PublicProperty(), "property", 1));
        assertTrue(PropertyUtils.isWriteable(new InheritedPublicProperty(), "property", 1));
        
        assertFalse(PropertyUtils.isWriteable(new PublicProperty(), "property", "wrongtype"));
        assertFalse(PropertyUtils.isWriteable(new InheritedPublicProperty(), "property", "wrongtype"));
    }
    
    @Test
    public void testNonWriteable() {
        assertFalse(PropertyUtils.isWriteable(new PrivateProperty(), "property"));
        assertFalse(PropertyUtils.isWriteable(new InheritedPrivateProperty(), "property"));
        
        assertFalse(PropertyUtils.isWriteable(new PrivateProperty(), "property", 1));
        assertFalse(PropertyUtils.isWriteable(new InheritedPrivateProperty(), "property", 1));
        
        assertFalse(PropertyUtils.isWriteable(new PrivateProperty(), "property", "wrongtype"));
        assertFalse(PropertyUtils.isWriteable(new InheritedPrivateProperty(), "property", "wrongtype"));
    }
    
    @Test
    public void testNonExistingProperty() {
        assertFalse(PropertyUtils.isWriteable(new AccessorProperty(), "nonexisting"));
        assertFalse(PropertyUtils.isWriteable(new PublicProperty(), "nonexisting"));
        assertFalse(PropertyUtils.isWriteable(new PrivateProperty(), "nonexisting"));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullObject() {
        PropertyUtils.isWriteable(null, "property");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullPropertyName() {
        PropertyUtils.isWriteable(new AccessorProperty(), null);
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
