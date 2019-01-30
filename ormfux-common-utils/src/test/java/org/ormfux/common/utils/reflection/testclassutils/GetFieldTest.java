package org.ormfux.common.utils.reflection.testclassutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;

import org.junit.Test;
import org.ormfux.common.utils.reflection.ClassUtils;

public class GetFieldTest {
    
    @Test
    public void testFieldFound() throws NoSuchFieldException, SecurityException {
        assertEquals(getField(MockLeafClass.class, "privateProperty"), ClassUtils.getField(MockLeafClass.class, "privateProperty"));
        assertEquals(getField(MockLeafClass.class, "privateStaticProperty"), ClassUtils.getField(MockLeafClass.class, "privateStaticProperty"));
        assertEquals(getField(MockLeafClass.class, "protectedProperty"), ClassUtils.getField(MockLeafClass.class, "protectedProperty"));
        assertEquals(getField(MockLeafClass.class, "protectedStaticProperty"), ClassUtils.getField(MockLeafClass.class, "protectedStaticProperty"));
        assertEquals(getField(MockLeafClass.class, "packageProperty"), ClassUtils.getField(MockLeafClass.class, "packageProperty"));
        assertEquals(getField(MockLeafClass.class, "packageStaticProperty"), ClassUtils.getField(MockLeafClass.class, "packageStaticProperty"));
        assertEquals(getField(MockLeafClass.class, "publicProperty"), ClassUtils.getField(MockLeafClass.class, "publicProperty"));
        assertEquals(getField(MockLeafClass.class, "publicStaticProperty"), ClassUtils.getField(MockLeafClass.class, "publicStaticProperty"));
    }
    
    @Test
    public void testFieldOfSuperClassFound() throws NoSuchFieldException, SecurityException {
        assertEquals(getField(MockIntermediateClass.class, "privateProperty"), ClassUtils.getField(NoFieldLeafClass.class, "privateProperty"));
        assertEquals(getField(MockIntermediateClass.class, "privateStaticProperty"), ClassUtils.getField(NoFieldLeafClass.class, "privateStaticProperty"));
        assertEquals(getField(MockIntermediateClass.class, "protectedProperty"), ClassUtils.getField(NoFieldLeafClass.class, "protectedProperty"));
        assertEquals(getField(MockIntermediateClass.class, "protectedStaticProperty"), ClassUtils.getField(NoFieldLeafClass.class, "protectedStaticProperty"));
        assertEquals(getField(MockIntermediateClass.class, "packageProperty"), ClassUtils.getField(NoFieldLeafClass.class, "packageProperty"));
        assertEquals(getField(MockIntermediateClass.class, "packageStaticProperty"), ClassUtils.getField(NoFieldLeafClass.class, "packageStaticProperty"));
        assertEquals(getField(MockIntermediateClass.class, "publicProperty"), ClassUtils.getField(NoFieldLeafClass.class, "publicProperty"));
        assertEquals(getField(MockIntermediateClass.class, "publicStaticProperty"), ClassUtils.getField(NoFieldLeafClass.class, "publicStaticProperty"));
    }
    
    @Test
    public void testFieldNotFound() throws NoSuchFieldException, SecurityException {
        assertNull(ClassUtils.getField(NoFieldClass.class, "privateProperty"));
        assertNull(ClassUtils.getField(NoFieldLeafClass.class, "nonexisting"));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullClass() {
        ClassUtils.getField(null, "fieldname");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullFieldName() {
        ClassUtils.getField(NoFieldClass.class, null);
    }
    
    private Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException, SecurityException {
        return clazz.getDeclaredField(fieldName);
    }
    
    public static class MockSuperClass {
        
        @SuppressWarnings("unused")
        private Long privateProperty;
        
        @SuppressWarnings("unused")
        private static String privateStaticProperty;
        
        protected Long protectedProperty;
        
        protected static String protectedStaticProperty;
        
        Long packageProperty;
        
        static String packageStaticProperty;
        
        public Long publicProperty;
        
        public static String publicStaticProperty;
    }
    
    public static class MockIntermediateClass extends MockSuperClass {
        
        @SuppressWarnings("unused")
        private Long privateProperty;
        
        @SuppressWarnings("unused")
        private static String privateStaticProperty;
        
        protected Long protectedProperty;
        
        protected static String protectedStaticProperty;
        
        Long packageProperty;
        
        static String packageStaticProperty;
        
        public Long publicProperty;
        
        public static String publicStaticProperty;
    }
    
    public static class MockLeafClass extends MockIntermediateClass {
        
        @SuppressWarnings("unused")
        private Long privateProperty;
        
        @SuppressWarnings("unused")
        private static String privateStaticProperty;
        
        protected Long protectedProperty;
        
        protected static String protectedStaticProperty;
        
        Long packageProperty;
        
        static String packageStaticProperty;
        
        public Long publicProperty;
        
        public static String publicStaticProperty;
    }
    
    public static class NoFieldClass {
        
    }
    
    public static class NoFieldLeafClass extends MockIntermediateClass {
        
    }
}
