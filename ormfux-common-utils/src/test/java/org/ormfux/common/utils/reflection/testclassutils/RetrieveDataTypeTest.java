package org.ormfux.common.utils.reflection.testclassutils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.ormfux.common.utils.reflection.ClassUtils;
import org.ormfux.common.utils.reflection.exception.ElementNotFoundException;

public class RetrieveDataTypeTest {
    
    @Test
    public void testFirstDeclaration() {
        assertEquals(String.class, ClassUtils.retrieveDataType(LeafClass.class));
        assertEquals(String.class, ClassUtils.retrieveDataType(LeafLeafClass.class));
    }
    
    @Test
    public void getIndexedDeclaration() {
        assertEquals(String.class, ClassUtils.retrieveDataType(LeafClass.class, 0));
        assertEquals(String.class, ClassUtils.retrieveDataType(LeafLeafClass.class, 0));
        assertEquals(Long.class, ClassUtils.retrieveDataType(LeafClass.class, 1));
        assertEquals(Long.class, ClassUtils.retrieveDataType(LeafLeafClass.class, 1));
        assertEquals(Integer.class, ClassUtils.retrieveDataType(LeafClass.class, 2));
        assertEquals(Integer.class, ClassUtils.retrieveDataType(LeafLeafClass.class, 2));
    }
    
    @Test(expected = ElementNotFoundException.class)
    public void testNotEnoughDataTypes() {
        ClassUtils.retrieveDataType(LeafClass.class, 3);
    }
    
    @Test(expected = ElementNotFoundException.class)
    public void testNoActualTypeArguments() {
        ClassUtils.retrieveDataType(GenericIntermediateClass.class);
    }
    
    @Test(expected = ElementNotFoundException.class)
    public void testNoGenericSuperClass() {
        ClassUtils.retrieveDataType(GenericSuperClass.class);
    }
    
    public static class GenericSuperClass<S,R,T> {
        
    }
    
    public static class GenericIntermediateClass<S,R,T> extends GenericSuperClass<S, R, T> {
        
    }
    
    public static class LeafClass extends GenericIntermediateClass<String, Long, Integer> {
        
    }
    
    public static class LeafLeafClass extends LeafClass {
        
    }
}
