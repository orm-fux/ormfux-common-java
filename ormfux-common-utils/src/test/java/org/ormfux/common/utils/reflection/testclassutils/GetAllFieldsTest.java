package org.ormfux.common.utils.reflection.testclassutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Test;
import org.ormfux.common.utils.reflection.ClassUtils;

public class GetAllFieldsTest {
    
    @Test
    public void testWithoutStopClass() throws NoSuchFieldException, SecurityException {
        List<Field> fields = ClassUtils.getAllFields(MockLeafClass.class);
        assertNotNull(fields);
        assertEquals(24, fields.size());
        
        assertTrue(fields.contains(getField(MockSuperClass.class, "privateSuperProperty")));
        assertTrue(fields.contains(getField(MockSuperClass.class, "privateSuperStaticProperty")));
        assertTrue(fields.contains(getField(MockSuperClass.class, "protectedSuperProperty")));
        assertTrue(fields.contains(getField(MockSuperClass.class, "protectedSuperStaticProperty")));
        assertTrue(fields.contains(getField(MockSuperClass.class, "packageSuperProperty")));
        assertTrue(fields.contains(getField(MockSuperClass.class, "packageSuperStaticProperty")));
        assertTrue(fields.contains(getField(MockSuperClass.class, "publicSuperProperty")));
        assertTrue(fields.contains(getField(MockSuperClass.class, "publicSuperStaticProperty")));
        
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "privateIntermediateProperty")));
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "privateIntermediateStaticProperty")));
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "protectedIntermediateProperty")));
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "protectedIntermediateStaticProperty")));
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "packageIntermediateProperty")));
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "packageIntermediateStaticProperty")));
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "publicIntermediateProperty")));
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "publicIntermediateStaticProperty")));
        
        assertTrue(fields.contains(getField(MockLeafClass.class, "privateLeafProperty")));
        assertTrue(fields.contains(getField(MockLeafClass.class, "privateLeafStaticProperty")));
        assertTrue(fields.contains(getField(MockLeafClass.class, "protectedLeafProperty")));
        assertTrue(fields.contains(getField(MockLeafClass.class, "protectedLeafStaticProperty")));
        assertTrue(fields.contains(getField(MockLeafClass.class, "packageLeafProperty")));
        assertTrue(fields.contains(getField(MockLeafClass.class, "packageLeafStaticProperty")));
        assertTrue(fields.contains(getField(MockLeafClass.class, "publicLeafProperty")));
        assertTrue(fields.contains(getField(MockLeafClass.class, "publicLeafStaticProperty")));
        
    }
    
    @Test
    public void testWithStopClass() throws NoSuchFieldException, SecurityException {
        List<Field> fields = ClassUtils.getAllFields(MockLeafClass.class, MockSuperClass.class);
        assertNotNull(fields);
        assertEquals(16, fields.size());
        
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "privateIntermediateProperty")));
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "privateIntermediateStaticProperty")));
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "protectedIntermediateProperty")));
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "protectedIntermediateStaticProperty")));
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "packageIntermediateProperty")));
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "packageIntermediateStaticProperty")));
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "publicIntermediateProperty")));
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "publicIntermediateStaticProperty")));
        
        assertTrue(fields.contains(getField(MockLeafClass.class, "privateLeafProperty")));
        assertTrue(fields.contains(getField(MockLeafClass.class, "privateLeafStaticProperty")));
        assertTrue(fields.contains(getField(MockLeafClass.class, "protectedLeafProperty")));
        assertTrue(fields.contains(getField(MockLeafClass.class, "protectedLeafStaticProperty")));
        assertTrue(fields.contains(getField(MockLeafClass.class, "packageLeafProperty")));
        assertTrue(fields.contains(getField(MockLeafClass.class, "packageLeafStaticProperty")));
        assertTrue(fields.contains(getField(MockLeafClass.class, "publicLeafProperty")));
        assertTrue(fields.contains(getField(MockLeafClass.class, "publicLeafStaticProperty")));
        
    }
    
    @Test
    public void testWithoutOwnFields() throws NoSuchFieldException, SecurityException {
        List<Field> fields = ClassUtils.getAllFields(NoFieldLeafClass.class);
        assertNotNull(fields);
        assertEquals(16, fields.size());
        
        assertTrue(fields.contains(getField(MockSuperClass.class, "privateSuperProperty")));
        assertTrue(fields.contains(getField(MockSuperClass.class, "privateSuperStaticProperty")));
        assertTrue(fields.contains(getField(MockSuperClass.class, "protectedSuperProperty")));
        assertTrue(fields.contains(getField(MockSuperClass.class, "protectedSuperStaticProperty")));
        assertTrue(fields.contains(getField(MockSuperClass.class, "packageSuperProperty")));
        assertTrue(fields.contains(getField(MockSuperClass.class, "packageSuperStaticProperty")));
        assertTrue(fields.contains(getField(MockSuperClass.class, "publicSuperProperty")));
        assertTrue(fields.contains(getField(MockSuperClass.class, "publicSuperStaticProperty")));
        
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "privateIntermediateProperty")));
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "privateIntermediateStaticProperty")));
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "protectedIntermediateProperty")));
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "protectedIntermediateStaticProperty")));
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "packageIntermediateProperty")));
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "packageIntermediateStaticProperty")));
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "publicIntermediateProperty")));
        assertTrue(fields.contains(getField(MockIntermediateClass.class, "publicIntermediateStaticProperty")));
        
    }
    
    @Test
    public void testNoFields() throws NoSuchFieldException, SecurityException {
        List<Field> fields = ClassUtils.getAllFields(NoFieldClass.class);
        assertNotNull(fields);
        assertEquals(0, fields.size());
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullClass() {
        ClassUtils.getAllFields(null);
    }
    
    private Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException, SecurityException {
        return clazz.getDeclaredField(fieldName);
    }
    
    public static class MockSuperClass {
        
        @SuppressWarnings("unused")
        private Long privateSuperProperty;
        
        @SuppressWarnings("unused")
        private static String privateSuperStaticProperty;
        
        protected Long protectedSuperProperty;
        
        protected static String protectedSuperStaticProperty;
        
        Long packageSuperProperty;
        
        static String packageSuperStaticProperty;
        
        public Long publicSuperProperty;
        
        public static String publicSuperStaticProperty;
    }
    
    public static class MockIntermediateClass extends MockSuperClass {
        
        @SuppressWarnings("unused")
        private Long privateIntermediateProperty;
        
        @SuppressWarnings("unused")
        private static String privateIntermediateStaticProperty;
        
        protected Long protectedIntermediateProperty;
        
        protected static String protectedIntermediateStaticProperty;
        
        Long packageIntermediateProperty;
        
        static String packageIntermediateStaticProperty;
        
        public Long publicIntermediateProperty;
        
        public static String publicIntermediateStaticProperty;
    }
    
    public static class MockLeafClass extends MockIntermediateClass {
        
        @SuppressWarnings("unused")
        private Long privateLeafProperty;
        
        @SuppressWarnings("unused")
        private static String privateLeafStaticProperty;
        
        protected Long protectedLeafProperty;
        
        protected static String protectedLeafStaticProperty;
        
        Long packageLeafProperty;
        
        static String packageLeafStaticProperty;
        
        public Long publicLeafProperty;
        
        public static String publicLeafStaticProperty;
    }
    
    public static class NoFieldClass {
        
    }
    
    public static class NoFieldLeafClass extends MockIntermediateClass {
        
    }
}
