package org.ormfux.common.utils.reflection.testmethodutils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.ormfux.common.utils.reflection.MethodUtils;

public class FindPropertySetterTest {
    
    @Test
    public void testWithoutResultType() throws NoSuchMethodException, SecurityException {
        assertEquals(SetterProvider.class.getMethod("setStringProperty", String.class), 
                     MethodUtils.findPropertySetter(SetterProvider.class, "stringProperty"));
        assertEquals(SetterProvider.class.getMethod("setPrimitiveBoolean", boolean.class), 
                     MethodUtils.findPropertySetter(SetterProvider.class, "primitiveBoolean"));
        assertEquals(SetterProvider.class.getMethod("setBooleanObject", Boolean.class), 
                     MethodUtils.findPropertySetter(SetterProvider.class, "booleanObject"));
    }
    
    @Test
    public void testWithoutResultTypeInherited() throws NoSuchMethodException, SecurityException {
        assertEquals(SetterProvider.class.getMethod("setStringProperty", String.class), 
                     MethodUtils.findPropertySetter(InheritedSetters.class, "stringProperty"));
        assertEquals(SetterProvider.class.getMethod("setPrimitiveBoolean", boolean.class), 
                     MethodUtils.findPropertySetter(InheritedSetters.class, "primitiveBoolean"));
        assertEquals(SetterProvider.class.getMethod("setBooleanObject", Boolean.class), 
                     MethodUtils.findPropertySetter(InheritedSetters.class, "booleanObject"));
    }
    
    @Test
    public void testWithoutResultTypeOverriden() throws NoSuchMethodException, SecurityException {
        assertEquals(OverriddenSetters.class.getMethod("setStringProperty", String.class), 
                     MethodUtils.findPropertySetter(OverriddenSetters.class, "stringProperty"));
        assertEquals(OverriddenSetters.class.getMethod("setPrimitiveBoolean", boolean.class), 
                     MethodUtils.findPropertySetter(OverriddenSetters.class, "primitiveBoolean"));
        assertEquals(OverriddenSetters.class.getMethod("setBooleanObject", Boolean.class), 
                     MethodUtils.findPropertySetter(OverriddenSetters.class, "booleanObject"));
    }
    
    @Test
    public void testWithParamType() throws NoSuchMethodException, SecurityException {
        assertEquals(SetterProvider.class.getMethod("setStringProperty", String.class), 
                     MethodUtils.findPropertySetter(SetterProvider.class, "stringProperty", String.class));
        assertEquals(SetterProvider.class.getMethod("setPrimitiveBoolean", boolean.class), 
                     MethodUtils.findPropertySetter(SetterProvider.class, "primitiveBoolean", boolean.class));
        assertEquals(SetterProvider.class.getMethod("setBooleanObject", Boolean.class), 
                     MethodUtils.findPropertySetter(SetterProvider.class, "booleanObject", Boolean.class));
    }
    
    @Test
    public void testWithParamTypeInherited() throws NoSuchMethodException, SecurityException {
        assertEquals(SetterProvider.class.getMethod("setStringProperty", String.class), 
                     MethodUtils.findPropertySetter(InheritedSetters.class, "stringProperty", String.class));
        assertEquals(SetterProvider.class.getMethod("setPrimitiveBoolean", boolean.class), 
                     MethodUtils.findPropertySetter(InheritedSetters.class, "primitiveBoolean", boolean.class));
        assertEquals(SetterProvider.class.getMethod("setBooleanObject", Boolean.class), 
                     MethodUtils.findPropertySetter(InheritedSetters.class, "booleanObject", boolean.class));
    }
    
    @Test
    public void testWithParamTypeOverriden() throws NoSuchMethodException, SecurityException {
        assertEquals(OverriddenSetters.class.getMethod("setStringProperty", String.class), 
                     MethodUtils.findPropertySetter(OverriddenSetters.class, "stringProperty", String.class));
        assertEquals(OverriddenSetters.class.getMethod("setPrimitiveBoolean", boolean.class), 
                     MethodUtils.findPropertySetter(OverriddenSetters.class, "primitiveBoolean", boolean.class));
        assertEquals(OverriddenSetters.class.getMethod("setBooleanObject", Boolean.class), 
                     MethodUtils.findPropertySetter(OverriddenSetters.class, "booleanObject", boolean.class));
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testWithWrongParamType() throws NoSuchMethodException {
        MethodUtils.findPropertySetter(OverriddenSetters.class, "stringProperty", Integer.class);
    }
    
    @Test
    public void testPrimitiveTypeHandling() throws NoSuchMethodException, SecurityException {
        assertEquals(SetterProvider.class.getMethod("setPrimitiveInt", int.class), 
                     MethodUtils.findPropertySetter(SetterProvider.class, "primitiveInt", int.class));
        assertEquals(SetterProvider.class.getMethod("setPrimitiveInt", int.class), 
                     MethodUtils.findPropertySetter(SetterProvider.class, "primitiveInt", Integer.class));
        assertEquals(SetterProvider.class.getMethod("setObjectInt", Integer.class), 
                     MethodUtils.findPropertySetter(SetterProvider.class, "objectInt", int.class));
        assertEquals(SetterProvider.class.getMethod("setObjectInt", Integer.class), 
                     MethodUtils.findPropertySetter(SetterProvider.class, "objectInt", Integer.class));
        
        assertEquals(SetterProvider.class.getMethod("setPrimitiveInt", int.class), 
                     MethodUtils.findPropertySetter(InheritedSetters.class, "primitiveInt", int.class));
        assertEquals(SetterProvider.class.getMethod("setPrimitiveInt", int.class), 
                     MethodUtils.findPropertySetter(InheritedSetters.class, "primitiveInt", Integer.class));
        assertEquals(SetterProvider.class.getMethod("setObjectInt", Integer.class), 
                     MethodUtils.findPropertySetter(InheritedSetters.class, "objectInt", int.class));
        assertEquals(SetterProvider.class.getMethod("setObjectInt", Integer.class), 
                     MethodUtils.findPropertySetter(InheritedSetters.class, "objectInt", Integer.class));
        
        assertEquals(SetterProvider.class.getMethod("setPrimitiveInt", int.class), 
                     MethodUtils.findPropertySetter(OverriddenSetters.class, "primitiveInt", int.class));
        assertEquals(SetterProvider.class.getMethod("setPrimitiveInt", int.class), 
                     MethodUtils.findPropertySetter(OverriddenSetters.class, "primitiveInt", Integer.class));
        assertEquals(SetterProvider.class.getMethod("setObjectInt", Integer.class), 
                     MethodUtils.findPropertySetter(OverriddenSetters.class, "objectInt", int.class));
        assertEquals(SetterProvider.class.getMethod("setObjectInt", Integer.class), 
                     MethodUtils.findPropertySetter(OverriddenSetters.class, "objectInt", Integer.class));
        
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testNonExistingWithoutParamType() throws NoSuchMethodException {
        MethodUtils.findPropertySetter(OverriddenSetters.class, "NONEXISTING");
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testNonExistingWithParamType() throws NoSuchMethodException {
        MethodUtils.findPropertySetter(OverriddenSetters.class, "NONEXISTING", String.class);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullClassWithoutParamType() throws NoSuchMethodException {
        MethodUtils.findPropertySetter(null, "stringProperty");
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullClassWithParamType() throws NoSuchMethodException {
        MethodUtils.findPropertySetter(null, "stringProperty", String.class);
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testNullPropertyNameWithoutParamType() throws NoSuchMethodException {
        MethodUtils.findPropertySetter(OverriddenSetters.class, null);
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testNullPropertyNameWithParamType() throws NoSuchMethodException {
        MethodUtils.findPropertySetter(OverriddenSetters.class, null, String.class);
    }
    
    @Test
    public void testNullParamType() throws NoSuchMethodException, SecurityException {
        assertEquals(SetterProvider.class.getMethod("setStringProperty", String.class), 
                     MethodUtils.findPropertySetter(SetterProvider.class, "stringProperty", null));
        assertEquals(SetterProvider.class.getMethod("setStringProperty", String.class), 
                     MethodUtils.findPropertySetter(InheritedSetters.class, "stringProperty", null));
        assertEquals(OverriddenSetters.class.getMethod("setStringProperty", String.class), 
                     MethodUtils.findPropertySetter(OverriddenSetters.class, "stringProperty", null));
    }
    
    public static class SetterProvider {
        
        public void setStringProperty(String val) {
        }
        
        public void setPrimitiveBoolean(boolean val) {
        }
        
        public void setBooleanObject(Boolean val) {
        }
        
        public void setPrimitiveInt(int val) {
        }
        
        public void setObjectInt(Integer val) {
        }
        
    }
    
    public static class InheritedSetters extends SetterProvider {}
    
    public static class OverriddenSetters extends SetterProvider {
        
        @Override
        public void setStringProperty(String val) {
        }
        
        @Override
        public void setPrimitiveBoolean(boolean val) {
        }
        
        @Override
        public void setBooleanObject(Boolean val) {
        }
        
    }
}
