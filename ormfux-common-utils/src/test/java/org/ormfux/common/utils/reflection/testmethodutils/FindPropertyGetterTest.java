package org.ormfux.common.utils.reflection.testmethodutils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.ormfux.common.utils.reflection.MethodUtils;

public class FindPropertyGetterTest {
    
    @Test
    public void testWithoutResultType() throws NoSuchMethodException, SecurityException {
        assertEquals(GetterProvider.class.getMethod("getStringProperty"), MethodUtils.findPropertyGetter(GetterProvider.class, "stringProperty"));
        assertEquals(GetterProvider.class.getMethod("isPrimitiveBoolean"), MethodUtils.findPropertyGetter(GetterProvider.class, "primitiveBoolean"));
        assertEquals(GetterProvider.class.getMethod("getBooleanObject"), MethodUtils.findPropertyGetter(GetterProvider.class, "booleanObject"));
        assertEquals(GetterProvider.class.getMethod("isBoolean"), MethodUtils.findPropertyGetter(GetterProvider.class, "boolean"));
    }
    
    @Test
    public void testWithoutResultTypeInherited() throws NoSuchMethodException, SecurityException {
        assertEquals(GetterProvider.class.getMethod("getStringProperty"), MethodUtils.findPropertyGetter(InheritedGetters.class, "stringProperty"));
        assertEquals(GetterProvider.class.getMethod("isPrimitiveBoolean"), MethodUtils.findPropertyGetter(InheritedGetters.class, "primitiveBoolean"));
        assertEquals(GetterProvider.class.getMethod("getBooleanObject"), MethodUtils.findPropertyGetter(InheritedGetters.class, "booleanObject"));
        assertEquals(GetterProvider.class.getMethod("isBoolean"), MethodUtils.findPropertyGetter(InheritedGetters.class, "boolean"));
    }
    
    @Test
    public void testWithoutResultTypeOverriden() throws NoSuchMethodException, SecurityException {
        assertEquals(OverriddenGetters.class.getMethod("getStringProperty"), MethodUtils.findPropertyGetter(OverriddenGetters.class, "stringProperty"));
        assertEquals(OverriddenGetters.class.getMethod("isPrimitiveBoolean"), MethodUtils.findPropertyGetter(OverriddenGetters.class, "primitiveBoolean"));
        assertEquals(OverriddenGetters.class.getMethod("getBooleanObject"), MethodUtils.findPropertyGetter(OverriddenGetters.class, "booleanObject"));
        assertEquals(GetterProvider.class.getMethod("isBoolean"), MethodUtils.findPropertyGetter(OverriddenGetters.class, "boolean"));
    }
    
    @Test
    public void testWithResultType() throws NoSuchMethodException, SecurityException {
        assertEquals(GetterProvider.class.getMethod("getStringProperty"), MethodUtils.findPropertyGetter(GetterProvider.class, "stringProperty", String.class));
        assertEquals(GetterProvider.class.getMethod("isPrimitiveBoolean"), MethodUtils.findPropertyGetter(GetterProvider.class, "primitiveBoolean", boolean.class));
        assertEquals(GetterProvider.class.getMethod("isPrimitiveBoolean"), MethodUtils.findPropertyGetter(GetterProvider.class, "primitiveBoolean", Boolean.class));
        assertEquals(GetterProvider.class.getMethod("getBooleanObject"), MethodUtils.findPropertyGetter(GetterProvider.class, "booleanObject", Boolean.class));
        assertEquals(GetterProvider.class.getMethod("isBooleanObject"), MethodUtils.findPropertyGetter(GetterProvider.class, "booleanObject", boolean.class));
        assertEquals(GetterProvider.class.getMethod("getBoolean"), MethodUtils.findPropertyGetter(GetterProvider.class, "boolean", Boolean.class));
        assertEquals(GetterProvider.class.getMethod("isBoolean"), MethodUtils.findPropertyGetter(GetterProvider.class, "boolean", boolean.class));
    }
    
    @Test
    public void testWithResultTypeInherited() throws NoSuchMethodException, SecurityException {
        assertEquals(GetterProvider.class.getMethod("getStringProperty"), MethodUtils.findPropertyGetter(InheritedGetters.class, "stringProperty", String.class));
        assertEquals(GetterProvider.class.getMethod("isPrimitiveBoolean"), MethodUtils.findPropertyGetter(InheritedGetters.class, "primitiveBoolean", boolean.class));
        assertEquals(GetterProvider.class.getMethod("isPrimitiveBoolean"), MethodUtils.findPropertyGetter(InheritedGetters.class, "primitiveBoolean", Boolean.class));
        assertEquals(GetterProvider.class.getMethod("isBooleanObject"), MethodUtils.findPropertyGetter(InheritedGetters.class, "booleanObject", boolean.class));
        assertEquals(GetterProvider.class.getMethod("getBooleanObject"), MethodUtils.findPropertyGetter(InheritedGetters.class, "booleanObject", Boolean.class));
        assertEquals(GetterProvider.class.getMethod("isBoolean"), MethodUtils.findPropertyGetter(InheritedGetters.class, "boolean", boolean.class));
        assertEquals(GetterProvider.class.getMethod("getBoolean"), MethodUtils.findPropertyGetter(InheritedGetters.class, "boolean", Boolean.class));
    }
    
    @Test
    public void testWithResultTypeOverriden() throws NoSuchMethodException, SecurityException {
        assertEquals(OverriddenGetters.class.getMethod("getStringProperty"), MethodUtils.findPropertyGetter(OverriddenGetters.class, "stringProperty", String.class));
        assertEquals(OverriddenGetters.class.getMethod("isPrimitiveBoolean"), MethodUtils.findPropertyGetter(OverriddenGetters.class, "primitiveBoolean", boolean.class));
        assertEquals(OverriddenGetters.class.getMethod("isPrimitiveBoolean"), MethodUtils.findPropertyGetter(OverriddenGetters.class, "primitiveBoolean", Boolean.class));
        assertEquals(OverriddenGetters.class.getMethod("isBooleanObject"), MethodUtils.findPropertyGetter(OverriddenGetters.class, "booleanObject", boolean.class));
        assertEquals(OverriddenGetters.class.getMethod("getBooleanObject"), MethodUtils.findPropertyGetter(OverriddenGetters.class, "booleanObject", Boolean.class));
        assertEquals(GetterProvider.class.getMethod("isBoolean"), MethodUtils.findPropertyGetter(OverriddenGetters.class, "boolean", boolean.class));
        assertEquals(OverriddenGetters.class.getMethod("getBoolean"), MethodUtils.findPropertyGetter(OverriddenGetters.class, "boolean", Boolean.class));
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testWithWrongResultType() throws NoSuchMethodException {
        MethodUtils.findPropertyGetter(OverriddenGetters.class, "stringProperty", Integer.class);
    }
    
    @Test
    public void testPrimitiveTypeHandling() throws NoSuchMethodException, SecurityException {
        assertEquals(GetterProvider.class.getMethod("getPrimitiveInt"), MethodUtils.findPropertyGetter(GetterProvider.class, "primitiveInt", int.class));
        assertEquals(GetterProvider.class.getMethod("getPrimitiveInt"), MethodUtils.findPropertyGetter(GetterProvider.class, "primitiveInt", Integer.class));
        assertEquals(GetterProvider.class.getMethod("getObjectInt"), MethodUtils.findPropertyGetter(GetterProvider.class, "objectInt", int.class));
        assertEquals(GetterProvider.class.getMethod("getObjectInt"), MethodUtils.findPropertyGetter(GetterProvider.class, "objectInt", Integer.class));
        
        assertEquals(GetterProvider.class.getMethod("getPrimitiveInt"), MethodUtils.findPropertyGetter(InheritedGetters.class, "primitiveInt", int.class));
        assertEquals(GetterProvider.class.getMethod("getPrimitiveInt"), MethodUtils.findPropertyGetter(InheritedGetters.class, "primitiveInt", Integer.class));
        assertEquals(GetterProvider.class.getMethod("getObjectInt"), MethodUtils.findPropertyGetter(InheritedGetters.class, "objectInt", int.class));
        assertEquals(GetterProvider.class.getMethod("getObjectInt"), MethodUtils.findPropertyGetter(InheritedGetters.class, "objectInt", Integer.class));
        
        assertEquals(GetterProvider.class.getMethod("getPrimitiveInt"), MethodUtils.findPropertyGetter(OverriddenGetters.class, "primitiveInt", int.class));
        assertEquals(GetterProvider.class.getMethod("getPrimitiveInt"), MethodUtils.findPropertyGetter(OverriddenGetters.class, "primitiveInt", Integer.class));
        assertEquals(GetterProvider.class.getMethod("getObjectInt"), MethodUtils.findPropertyGetter(OverriddenGetters.class, "objectInt", int.class));
        assertEquals(GetterProvider.class.getMethod("getObjectInt"), MethodUtils.findPropertyGetter(OverriddenGetters.class, "objectInt", Integer.class));
        
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testNonExistingWithoutResultType() throws NoSuchMethodException {
        MethodUtils.findPropertyGetter(OverriddenGetters.class, "NONEXISTING");
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testNonExistingWithResultType() throws NoSuchMethodException {
        MethodUtils.findPropertyGetter(OverriddenGetters.class, "NONEXISTING", String.class);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullClassWithoutResultType() throws NoSuchMethodException {
        MethodUtils.findPropertyGetter(null, "stringProperty");
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullClassWithResultType() throws NoSuchMethodException {
        MethodUtils.findPropertyGetter(null, "stringProperty", String.class);
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testNullPropertyNameWithoutResultType() throws NoSuchMethodException {
        MethodUtils.findPropertyGetter(OverriddenGetters.class, null);
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testNullPropertyNameWithResultType() throws NoSuchMethodException {
        MethodUtils.findPropertyGetter(OverriddenGetters.class, null, String.class);
    }
    
    @Test
    public void testNullResultType() throws NoSuchMethodException, SecurityException {
        assertEquals(GetterProvider.class.getMethod("getStringProperty"), MethodUtils.findPropertyGetter(GetterProvider.class, "stringProperty", null));
        assertEquals(GetterProvider.class.getMethod("getStringProperty"), MethodUtils.findPropertyGetter(InheritedGetters.class, "stringProperty", null));
        assertEquals(OverriddenGetters.class.getMethod("getStringProperty"), MethodUtils.findPropertyGetter(OverriddenGetters.class, "stringProperty", null));
    }
    
    public static class GetterProvider {
        
        public String getStringProperty() {
            return null;
        }
        
        public boolean isPrimitiveBoolean() {
            return false;
        }
        
        public Boolean isBooleanObject() {
            return null;
        }
        
        public Boolean getBooleanObject() {
            return null;
        }
        
        public boolean getBoolean() {
            return false;
        }
        
        public boolean isBoolean() {
            return false;
        }
        
        public int getPrimitiveInt() {
            return 0;
        }
        
        public Integer getObjectInt() {
            return null;
        }
        
    }
    
    public static class InheritedGetters extends GetterProvider {}
    
    public static class OverriddenGetters extends GetterProvider {
        
        @Override
        public String getStringProperty() {
            return null;
        }
        
        @Override
        public boolean isPrimitiveBoolean() {
            return false;
        }
        
        @Override
        public Boolean isBooleanObject() {
            return null;
        }
        
        @Override
        public Boolean getBooleanObject() {
            return null;
        }
        
        @Override
        public boolean getBoolean() {
            return false;
        }
        
    }
}
