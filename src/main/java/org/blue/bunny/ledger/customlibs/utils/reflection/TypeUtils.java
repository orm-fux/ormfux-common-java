package org.blue.bunny.ledger.customlibs.utils.reflection;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilities for type checking.
 */
public final class TypeUtils {
    
    private TypeUtils() {
    }
    
    /**
     * Map of primitive types to their "object" types (e.g. int to Integer)
     */
    protected static final Map<Class<?>, Class<?>> primitiveTypeToTypeMap = new HashMap<>();
    
    /**
     * Map of "object" types to their "primitive" types (e.g. Integer to int)
     */
    protected static final Map<Class<?>, Class<?>> typeToPrimitiveTypeMap = new HashMap<>();

    static {
        primitiveTypeToTypeMap.put(boolean.class, Boolean.class);
        primitiveTypeToTypeMap.put(byte.class, Byte.class);
        primitiveTypeToTypeMap.put(char.class, Character.class);
        primitiveTypeToTypeMap.put(short.class, Short.class);
        primitiveTypeToTypeMap.put(int.class, Integer.class);
        primitiveTypeToTypeMap.put(long.class, Long.class);
        primitiveTypeToTypeMap.put(float.class, Float.class);
        primitiveTypeToTypeMap.put(double.class, Double.class);
        primitiveTypeToTypeMap.put(void.class, Void.class);
        
        typeToPrimitiveTypeMap.put(Boolean.class, boolean.class);
        typeToPrimitiveTypeMap.put(Byte.class, byte.class);
        typeToPrimitiveTypeMap.put(Character.class, char.class);
        typeToPrimitiveTypeMap.put(Short.class, short.class);
        typeToPrimitiveTypeMap.put(Integer.class, int.class);
        typeToPrimitiveTypeMap.put(Long.class, long.class);
        typeToPrimitiveTypeMap.put(Float.class, float.class);
        typeToPrimitiveTypeMap.put(Double.class, double.class);
        typeToPrimitiveTypeMap.put(Void.class, void.class);

    }
    
    /**
     * Checks if values of type {@code assigned} can be assigned to preoperties of type {@code target}.
     *
     * @param target The target type.
     * @param assigned The type fo the value to assign to target.
     * @return {@code true} when assignment is possible.
     */
    public static boolean isTypeAssignable(Class<?> target, Class<?> assigned) {
        if (target.isAssignableFrom(assigned)) {
            return true;
        }
        
        if (target.isPrimitive() || assigned.isPrimitive()) {
            if ((TypeUtils.primitiveTypeToTypeMap.get(target) != null && TypeUtils.primitiveTypeToTypeMap.get(target).isAssignableFrom(assigned))
                    || (TypeUtils.typeToPrimitiveTypeMap.get(target) != null && TypeUtils.typeToPrimitiveTypeMap.get(target).isAssignableFrom(assigned))
                    || (TypeUtils.primitiveTypeToTypeMap.get(assigned) != null && target.isAssignableFrom(TypeUtils.primitiveTypeToTypeMap.get(assigned)))
                    || (TypeUtils.typeToPrimitiveTypeMap.get(assigned) != null && target.isAssignableFrom(TypeUtils.typeToPrimitiveTypeMap.get(assigned)))) {
                    return true;
            }
        }
        
        return false;
    }}
