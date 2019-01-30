package org.ormfux.common.utils.reflection.testclassutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.ParameterizedType;

import org.junit.Test;
import org.ormfux.common.utils.reflection.ClassUtils;

public class FindFirstParameterizedSuperClassTest {
    
    @Test
    public void testParameterizedSuperClassFound() {
        ParameterizedType superClass = ClassUtils.findFirstParameterizedSuperclass(LeafClass.class);
        assertNotNull(superClass);
        assertEquals(ParameterizedIntermediateClass.class, superClass.getRawType());
        
        superClass = ClassUtils.findFirstParameterizedSuperclass(RealizingClass.class);
        assertNotNull(superClass);
        assertEquals(ParameterizedIntermediateClass.class, superClass.getRawType());
        
        superClass = ClassUtils.findFirstParameterizedSuperclass(ParameterizedIntermediateClass.class);
        assertNotNull(superClass);
        assertEquals(ParameterizedRootClass.class, superClass.getRawType());
    }
    
    @Test
    public void testNoParameterizedSuperClass() {
        assertNull(ClassUtils.findFirstParameterizedSuperclass(ParameterizedRootClass.class));
        assertNull(ClassUtils.findFirstParameterizedSuperclass(PlainLeafClass.class));
        assertNull(ClassUtils.findFirstParameterizedSuperclass(PlainIntermediateClass.class));
        assertNull(ClassUtils.findFirstParameterizedSuperclass(PlainRootClass.class));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullClass() {
        ClassUtils.findFirstParameterizedSuperclass(null);
    }
    
    public static class ParameterizedRootClass<T> {
        
    }
    
    public static class ParameterizedIntermediateClass<T> extends ParameterizedRootClass<T> {
        
    }
    
    public static class RealizingClass extends ParameterizedIntermediateClass<String> {
        
    }
    
    public static class LeafClass extends RealizingClass {
        
    }
    
    public static class PlainRootClass {
        
    }
    
    public static class PlainIntermediateClass {
        
    }
    
    public static class PlainLeafClass {
        
    }
}
