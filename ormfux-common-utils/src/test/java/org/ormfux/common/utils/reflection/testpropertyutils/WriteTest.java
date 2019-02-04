package org.ormfux.common.utils.reflection.testpropertyutils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.ormfux.common.utils.reflection.PropertyUtils;

public class WriteTest {
    
    @Test
    public void testWriteThroughAccessor() {
        AccessorProperty accessorProperty = new AccessorProperty();
        PropertyUtils.write(accessorProperty, "property", 1);
        assertEquals(1, accessorProperty.getProperty());
        
        AccessorOnlyProperty accessorOnlyProperty = new AccessorOnlyProperty();
        PropertyUtils.write(accessorOnlyProperty, "property", 1);
        assertEquals(1, accessorOnlyProperty.property2);
        
        InheritedAccessorProperty inheritedAccessorProperty = new InheritedAccessorProperty();
        PropertyUtils.write(inheritedAccessorProperty, "property", 1);
        assertEquals(1, inheritedAccessorProperty.getProperty());
        
        AccessorAddedProperty accessorAddedProperty = new AccessorAddedProperty();
        PropertyUtils.write(accessorAddedProperty, "property", 1);
        assertEquals(1, accessorAddedProperty.property2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWriteWrongTypeThroughAccessor() {
        PropertyUtils.write(new AccessorProperty(), "property", "wrongtype");
    }
    
    @Test
    public void testWritePublic() {
        PublicProperty publicProperty = new PublicProperty();
        PropertyUtils.write(publicProperty, "property", 1);
        assertEquals(1, publicProperty.property);
        
        InheritedPublicProperty inheritedPublicProperty = new InheritedPublicProperty();
        PropertyUtils.write(inheritedPublicProperty, "property", 1);
        assertEquals(1, inheritedPublicProperty.property);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWritePublicWrongType() {
        PropertyUtils.write(new PublicProperty(), "property", "wrongtype");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNonWriteable() {
        PropertyUtils.write(new PrivateProperty(), "property", 1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNonExistingProperty() {
        PropertyUtils.write(new AccessorProperty(), "nonexisting", "value");
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullObject() {
        PropertyUtils.write(null, "property", "value");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullPropertyName() {
        PropertyUtils.write(new AccessorProperty(), null, "value");
    }
    
    public static class AccessorProperty {
        
        private int property;
        
        public void setProperty(int property) {
            this.property = property;
        }
        
        
        public int getProperty() {
            return property;
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
        
        public int property2;
        
        public void setProperty(int property) {
            this.property2 = property;
        }
    }
    
    public static class AccessorOnlyProperty {
        
        public int property2;
        
        public void setProperty(int property) {
            this.property2 = property;
        }
    }
    
    
}
