package org.ormfux.common.utils.reflection.testtypeutils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.ormfux.common.utils.reflection.TypeUtils;

public class IsTypeAssignableTest {
    
    @Test
    public void testInheritanceRelation() {
        assertTrue(TypeUtils.isTypeAssignable(Interface.class, Interface.class));
        assertTrue(TypeUtils.isTypeAssignable(Interface.class, SubClass.class));
        assertTrue(TypeUtils.isTypeAssignable(Interface.class, SubSubClass.class));
        
        assertFalse(TypeUtils.isTypeAssignable(Interface.class, SuperClass.class));
        assertFalse(TypeUtils.isTypeAssignable(SubClass.class, Interface.class));
        assertFalse(TypeUtils.isTypeAssignable(SubClass.class, Interface.class));
        
        assertTrue(TypeUtils.isTypeAssignable(SuperClass.class, SuperClass.class));
        assertTrue(TypeUtils.isTypeAssignable(SuperClass.class, SubClass.class));
        assertTrue(TypeUtils.isTypeAssignable(SuperClass.class, SubSubClass.class));
        
        assertFalse(TypeUtils.isTypeAssignable(SubClass.class, SuperClass.class));
        assertFalse(TypeUtils.isTypeAssignable(SubSubClass.class, SuperClass.class));
    }
    
    @Test
    public void testPrimitiveToPrimitiveTypes() {
        assertTrue(TypeUtils.isTypeAssignable(boolean.class, boolean.class));
        assertTrue(TypeUtils.isTypeAssignable(byte.class, byte.class));
        assertTrue(TypeUtils.isTypeAssignable(char.class, char.class));
        assertTrue(TypeUtils.isTypeAssignable(int.class, int.class));
        assertTrue(TypeUtils.isTypeAssignable(short.class, short.class));
        assertTrue(TypeUtils.isTypeAssignable(float.class, float.class));
        assertTrue(TypeUtils.isTypeAssignable(double.class, double.class));
        assertTrue(TypeUtils.isTypeAssignable(long.class, long.class));
        assertTrue(TypeUtils.isTypeAssignable(void.class, void.class));
    }
    
    @Test
    public void testPrimitiveToTypes() {
        assertTrue(TypeUtils.isTypeAssignable(Boolean.class, boolean.class));
        assertTrue(TypeUtils.isTypeAssignable(Byte.class, byte.class));
        assertTrue(TypeUtils.isTypeAssignable(Character.class, char.class));
        assertTrue(TypeUtils.isTypeAssignable(Integer.class, int.class));
        assertTrue(TypeUtils.isTypeAssignable(short.class, short.class));
        assertTrue(TypeUtils.isTypeAssignable(Float.class, float.class));
        assertTrue(TypeUtils.isTypeAssignable(Double.class, double.class));
        assertTrue(TypeUtils.isTypeAssignable(Long.class, long.class));
        assertTrue(TypeUtils.isTypeAssignable(Void.class, void.class));
    }
    
    @Test
    public void testTypeToPrimitive() {
        assertTrue(TypeUtils.isTypeAssignable(boolean.class, Boolean.class));
        assertTrue(TypeUtils.isTypeAssignable(byte.class, Byte.class));
        assertTrue(TypeUtils.isTypeAssignable(char.class, Character.class));
        assertTrue(TypeUtils.isTypeAssignable(int.class, Integer.class));
        assertTrue(TypeUtils.isTypeAssignable(short.class, Short.class));
        assertTrue(TypeUtils.isTypeAssignable(float.class, Float.class));
        assertTrue(TypeUtils.isTypeAssignable(double.class, Double.class));
        assertTrue(TypeUtils.isTypeAssignable(long.class, Long.class));
        assertTrue(TypeUtils.isTypeAssignable(void.class, Void.class));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullTarget() {
        TypeUtils.isTypeAssignable(null, SuperClass.class);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullAssigned() {
        TypeUtils.isTypeAssignable(SuperClass.class, null);
    }
    
    public static interface Interface {}
    
    public static class SuperClass {}
    
    public static class SubClass extends SuperClass implements Interface {}
    
    public static class SubSubClass extends SubClass {}
    
}
