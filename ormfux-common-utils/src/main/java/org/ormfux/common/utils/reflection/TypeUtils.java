package org.ormfux.common.utils.reflection;

import java.util.HashMap;
import java.util.Map;

import org.ormfux.common.utils.NullableUtils;

/**
 * Utilities for type checking.
 */
public final class TypeUtils {
    
    private TypeUtils() {
        throw new IllegalAccessError(TypeUtils.class.getSimpleName() + " class is not intended to be instantiated");
    }
    
    /**
     * Map of primitive types to their "object" types (e.g. int to Integer)
     */
    protected static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_TO_TYPE_MAP = new HashMap<>();
    
    /**
     * Map of "object" types to their "primitive" types (e.g. Integer to int)
     */
    protected static final Map<Class<?>, Class<?>> TYPE_TO_PRIMITIVE_TYPE_MAP = new HashMap<>();

    static {
        PRIMITIVE_TYPE_TO_TYPE_MAP.put(boolean.class, Boolean.class);
        PRIMITIVE_TYPE_TO_TYPE_MAP.put(byte.class, Byte.class);
        PRIMITIVE_TYPE_TO_TYPE_MAP.put(char.class, Character.class);
        PRIMITIVE_TYPE_TO_TYPE_MAP.put(short.class, Short.class);
        PRIMITIVE_TYPE_TO_TYPE_MAP.put(int.class, Integer.class);
        PRIMITIVE_TYPE_TO_TYPE_MAP.put(long.class, Long.class);
        PRIMITIVE_TYPE_TO_TYPE_MAP.put(float.class, Float.class);
        PRIMITIVE_TYPE_TO_TYPE_MAP.put(double.class, Double.class);
        PRIMITIVE_TYPE_TO_TYPE_MAP.put(void.class, Void.class);
        
        TYPE_TO_PRIMITIVE_TYPE_MAP.put(Boolean.class, boolean.class);
        TYPE_TO_PRIMITIVE_TYPE_MAP.put(Byte.class, byte.class);
        TYPE_TO_PRIMITIVE_TYPE_MAP.put(Character.class, char.class);
        TYPE_TO_PRIMITIVE_TYPE_MAP.put(Short.class, short.class);
        TYPE_TO_PRIMITIVE_TYPE_MAP.put(Integer.class, int.class);
        TYPE_TO_PRIMITIVE_TYPE_MAP.put(Long.class, long.class);
        TYPE_TO_PRIMITIVE_TYPE_MAP.put(Float.class, float.class);
        TYPE_TO_PRIMITIVE_TYPE_MAP.put(Double.class, double.class);
        TYPE_TO_PRIMITIVE_TYPE_MAP.put(Void.class, void.class);

    }
    
    /**
     * Checks if values of type {@code assigned} can be assigned to properties of type {@code target}.
     *
     * @param target The target type.
     * @param assigned The type of the value to assign to target.
     * @return {@code true} when assignment is possible.
     */
    public static boolean isTypeAssignable(Class<?> target, Class<?> assigned) {
        if (target.isAssignableFrom(assigned)) {
            return true;
        }
        
        if (target.isPrimitive() || assigned.isPrimitive()) {
            if (NullableUtils.check(TypeUtils.PRIMITIVE_TYPE_TO_TYPE_MAP.get(target), () -> TypeUtils.PRIMITIVE_TYPE_TO_TYPE_MAP.get(target).isAssignableFrom(assigned))
                    || NullableUtils.check(TypeUtils.TYPE_TO_PRIMITIVE_TYPE_MAP.get(target), () -> TypeUtils.TYPE_TO_PRIMITIVE_TYPE_MAP.get(target).isAssignableFrom(assigned))
                    || NullableUtils.check(TypeUtils.PRIMITIVE_TYPE_TO_TYPE_MAP.get(assigned), () -> target.isAssignableFrom(TypeUtils.PRIMITIVE_TYPE_TO_TYPE_MAP.get(assigned)))
                    || NullableUtils.check(TypeUtils.TYPE_TO_PRIMITIVE_TYPE_MAP.get(assigned), () -> target.isAssignableFrom(TypeUtils.TYPE_TO_PRIMITIVE_TYPE_MAP.get(assigned)))) {
                return true;
            }
        }
        
        return false;
    }
    
}
