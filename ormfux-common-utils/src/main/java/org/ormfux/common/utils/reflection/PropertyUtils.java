package org.ormfux.common.utils.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Utilities for working with properties of objects.
 */
public final class PropertyUtils {
    
    private PropertyUtils() {
    }
    
    /**
     * Checks, if the property with the given name can be read publicly from the Object.
     *
     * @param object The owning Object.
     * @param propertyName The name of the property.
     * @return {@code true} when the property can be read.
     */
    public static boolean isReadable(final Object object, final String propertyName) {
        return ClassUtils.isReadable(object.getClass(), propertyName);
    }
    
    /**
     * Checks, if the property with the given name can be written publicly from the Object.
     *
     * @param object The owning Object.
     * @param propertyName The name of the property.
     * @return {@code true} when the property can be written.
     */
    public static boolean isWriteable(final Object object, final String propertyName) {
        return ClassUtils.isWriteable(object.getClass(), propertyName);
    }
    
    /**
     * Checks, if the property with the given name can be read publicly from the Object.
     *
     * @param object The owning Object.
     * @param propertyName The name of the property.
     * @param value The value, which would be written to the property (for type compatibility checks).
     * @return {@code true} when the property can be read.
     */
    public static boolean isWriteable(final Object object, final String propertyName, final Object value) {
        return ClassUtils.isWriteable(object.getClass(), propertyName, value);
    }
    
    /**
     * Read the value of the property from the Object. The property must be publicly readable.
     *
     * @param object The Object.
     * @param propertyName The name of the property.
     * @return The property value.
     */
    public static Object read(final Object object, final String propertyName) {
        if (isReadable(object, propertyName)) {
            
            //Prefer getting value through accessor.
            try {
                return MethodUtils.invokePropertyGetterPath(object, propertyName, null, false);
            } catch (final NoSuchMethodException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | InstantiationException e) {
                //ignore. we'll try without getter.
            }
            
            final Field field = ClassUtils.getField(object.getClass(), propertyName);
            
            if (field != null) {
                try {
                    return field.get(object);
                } catch (final IllegalArgumentException | IllegalAccessException e) {
                    throw new IllegalArgumentException("Property '" + propertyName + "' is not readable for: " + object, e);
                }
            } else {
                throw new IllegalArgumentException("Property '" + propertyName + "' is not readable for: " + object);
            }
            
        } else {
            throw new IllegalArgumentException("Property '" + propertyName + "' is not readable for: " + object);
        }
    }
    
    /**
     * Read the value to the property of the Object. The property must be publicly writeable.
     *
     * @param object The Object.
     * @param propertyName The name of the property.
     * @param value The property value.
     */
    public static void write(final Object object, final String propertyName, final Object value) {
        if (isWriteable(object, propertyName, value)) {
            
            //Prefer setting value through accessor.
            try {
                MethodUtils.invokePropertySetterPath(object, propertyName, value, false);
            } catch (final NoSuchMethodException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | InstantiationException e1) {
                
                Field field = ClassUtils.getField(object.getClass(), propertyName);
                
                if (field != null) {
                    try {
                        field.set(object, value);
                    } catch (final IllegalArgumentException | IllegalAccessException e) {
                        throw new IllegalArgumentException("Property '" + propertyName + "' is not writeable for: " + object, e);
                    }
                } else {
                    throw new IllegalArgumentException("Property '" + propertyName + "' is not writeable for: " + object);
                }
            }
        } else {
            throw new IllegalArgumentException("Property '" + propertyName + "' is not writeable for " + object.getClass().getName() + " with value: " + value);
        }
    }
}
