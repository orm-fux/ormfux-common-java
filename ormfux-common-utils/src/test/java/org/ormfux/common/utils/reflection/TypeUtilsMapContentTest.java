package org.ormfux.common.utils.reflection;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.ormfux.common.utils.reflection.TypeUtils;

public class TypeUtilsMapContentTest {
    
    @Test
    public void testPrimitiveToType() {
        assertEquals(9, TypeUtils.PRIMITIVE_TYPE_TO_TYPE_MAP.size());
        assertEquals(Boolean.class, TypeUtils.PRIMITIVE_TYPE_TO_TYPE_MAP.get(boolean.class));
        assertEquals(Byte.class, TypeUtils.PRIMITIVE_TYPE_TO_TYPE_MAP.get(byte.class));
        assertEquals(Character.class, TypeUtils.PRIMITIVE_TYPE_TO_TYPE_MAP.get(char.class));
        assertEquals(Short.class, TypeUtils.PRIMITIVE_TYPE_TO_TYPE_MAP.get(short.class));
        assertEquals(Integer.class, TypeUtils.PRIMITIVE_TYPE_TO_TYPE_MAP.get(int.class));
        assertEquals(Long.class, TypeUtils.PRIMITIVE_TYPE_TO_TYPE_MAP.get(long.class));
        assertEquals(Float.class, TypeUtils.PRIMITIVE_TYPE_TO_TYPE_MAP.get(float.class));
        assertEquals(Double.class, TypeUtils.PRIMITIVE_TYPE_TO_TYPE_MAP.get(double.class));
        assertEquals(Void.class, TypeUtils.PRIMITIVE_TYPE_TO_TYPE_MAP.get(void.class));
    }
    
    @Test
    public void testTypeToPrimitive() {
        assertEquals(9, TypeUtils.TYPE_TO_PRIMITIVE_TYPE_MAP.size());
        assertEquals(boolean.class, TypeUtils.TYPE_TO_PRIMITIVE_TYPE_MAP.get(Boolean.class));
        assertEquals(byte.class, TypeUtils.TYPE_TO_PRIMITIVE_TYPE_MAP.get(Byte.class));
        assertEquals(char.class, TypeUtils.TYPE_TO_PRIMITIVE_TYPE_MAP.get(Character.class));
        assertEquals(short.class, TypeUtils.TYPE_TO_PRIMITIVE_TYPE_MAP.get(Short.class));
        assertEquals(int.class, TypeUtils.TYPE_TO_PRIMITIVE_TYPE_MAP.get(Integer.class));
        assertEquals(long.class, TypeUtils.TYPE_TO_PRIMITIVE_TYPE_MAP.get(Long.class));
        assertEquals(float.class, TypeUtils.TYPE_TO_PRIMITIVE_TYPE_MAP.get(Float.class));
        assertEquals(double.class, TypeUtils.TYPE_TO_PRIMITIVE_TYPE_MAP.get(Double.class));
        assertEquals(void.class, TypeUtils.TYPE_TO_PRIMITIVE_TYPE_MAP.get(Void.class));
    }
    
}
