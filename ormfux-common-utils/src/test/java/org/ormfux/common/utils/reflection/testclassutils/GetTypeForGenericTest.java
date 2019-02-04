package org.ormfux.common.utils.reflection.testclassutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;

import org.junit.Test;
import org.ormfux.common.utils.reflection.ClassUtils;

public class GetTypeForGenericTest {
    
    @Test
    public void testTypeFound() throws NoSuchFieldException, SecurityException {
        Type genericPropertyType = GenericBaseClass.class.getDeclaredField("property").getGenericType();
        assertTrue(genericPropertyType instanceof TypeVariable);
        
        assertEquals(BigDecimal.class, ClassUtils.getTypeForGeneric(ConcreteClass.class, (TypeVariable<?>) genericPropertyType));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNoGenericSuperClass() throws NoSuchFieldException, SecurityException {
        Type genericPropertyType = GenericBaseClass.class.getDeclaredField("property").getGenericType();
        ClassUtils.getTypeForGeneric(GenericBaseClass.class, (TypeVariable<?>) genericPropertyType);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullClass() throws NoSuchFieldException, SecurityException {
        Type genericPropertyType = GenericBaseClass.class.getDeclaredField("property").getGenericType();
        ClassUtils.getTypeForGeneric(null, (TypeVariable<?>) genericPropertyType);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullTypeVariable() {
        ClassUtils.getTypeForGeneric(GenericBaseClass.class, null);
    }
    
    public static abstract class GenericBaseClass<S, T> {
        
        @SuppressWarnings("unused")
        private T property;
    }
    
    public static class ConcreteClass extends GenericBaseClass<String, BigDecimal> {

    }
    
    
}
