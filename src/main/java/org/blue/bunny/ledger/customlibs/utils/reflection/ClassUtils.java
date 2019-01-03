package org.blue.bunny.ledger.customlibs.utils.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utilities for class analysis.
 */
public final class ClassUtils {
    
    private ClassUtils() {
    }
    
    /**
     * Traverses the inheritance hierache bottom-up an returns the first super-class,
     * which has a generic.
     * @param clazz The class for which to get the generic super class.
     * @return ParameterizedType of super class; {@code null} when there is none.
     */
    public static ParameterizedType findFirstParameterizedSuperclass(Class<?> clazz) {
        while (Object.class != clazz) {
            final Type genericSuperclass = clazz.getGenericSuperclass();
            
            if (genericSuperclass instanceof ParameterizedType) {
                return (ParameterizedType) genericSuperclass;
            } else {
                clazz = clazz.getSuperclass();
            }
        }
        
        return null;
    }
    
    /**
     * Retrieve the class type parameter.
     * 
     * @param clazz class with type parameter
     * @return class parameter.
     */
    public static Class<?> retrieveDataType(final Class<?> clazz) {
        final ParameterizedType parameterizedSuperclass = findFirstParameterizedSuperclass(clazz);
        
        if (parameterizedSuperclass == null) {
            throw new IllegalArgumentException(
                    "Incompatible class. Class must be (directly or indirectly) inherited from one generic class.");
        }
        
        return (Class<?>) parameterizedSuperclass.getActualTypeArguments()[0];
    }
    
    /**
     * Retrieve the class parameter at position pos.
     * 
     * @param clazz class with type parameter
     * @param pos type parameter position
     * @return class parameter.
     */
    public static Class<?> retrieveDataType(final Class<?> clazz, final int pos) {
        return (Class<?>) ((ParameterizedType) (clazz.getGenericSuperclass())).getActualTypeArguments()[pos];
    }
    
    /**
     * Checks if the field is publicly readable (i.e. has a "public" modifier or a getter method).
     *
     * @param clazz The class owning the field.
     * @param fieldName The name of the field.
     * @return {@code true} when readable.
     */
    public static boolean isReadable(final Class<?> clazz, final String fieldName) {
        final Field field = getField(clazz, fieldName);
        
        if (field != null && Modifier.isPublic(field.getModifiers())) {
            return true;
        }
        
        try {
            return MethodUtils.findPropertyGetter(clazz, fieldName) != null;
        } catch (final NoSuchMethodException e) {
            return false;
        }
    }
    
    /**
     * Checks if the field is publicly writeable (i.e. has a "public" modifier or a setter method).
     *
     * @param clazz The class owning the field.
     * @param fieldName The name of the field.
     * @return {@code true} when writeable.
     */
    public static boolean isWriteable(final Class<?> clazz, final String fieldName) {
        final Field field = ClassUtils.getField(clazz, fieldName);
        
        if (field != null && Modifier.isPublic(field.getModifiers())) {
            return true;
        }
        
        try {
            return MethodUtils.findPropertySetter(clazz, fieldName) != null;
        } catch (final NoSuchMethodException e) {
            return false;
        }
    }
    
    /**
     * Checks if the field is publicly writeable (i.e. has a "public" modifier or a setter method).
     *
     * @param clazz The class owning the field.
     * @param fieldName The name of the field.
     * @param value The value which would be written to the field (for type comliance checks).
     * @return {@code true} when writeable.
     */
    public static boolean isWriteable(final Class<?> clazz, final String fieldName, final Object value) {
        final Field field = ClassUtils.getField(clazz, fieldName);
        
        if (field != null && Modifier.isPublic(field.getModifiers())) {
            if (value == null) {
                return !field.getType().isPrimitive();
            } else {
                return TypeUtils.isTypeAssignable(field.getType(), value.getClass());
            }
        }
        
        try {
            final Method setter = MethodUtils.findPropertySetter(clazz, fieldName);
            
            if (setter != null) {
                if (value == null) {
                    return !setter.getParameterTypes()[0].isPrimitive();
                } else {
                    return TypeUtils.isTypeAssignable(setter.getParameterTypes()[0], value.getClass());
                }
            }
        } catch (final NoSuchMethodException e) {}
        
        return false;
    }
    
    /**
     * Gets the field with the name from the class or its super class. Values lower 
     * in the inheritance hierarchy are found first.
     * 
     * @param clazz The class owning the field.
     * @param fieldName The name of the field.
     * @return The field; {@code null} when not found.
     */
    public static Field getField(final Class<?> clazz, final String fieldName) {
        final List<Field> allFields = getAllFields(clazz);
        
        for (final Field field : allFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        
        return null;
    }
    
    /**
     * Returns a list of all fields used by this object from it's class and all super classes.
     * 
     * @param objectClass the class
     * @return a list of fields
     */
    public static List<Field> getAllFields(final Class<?> objectClass) {
        return getAllFields(objectClass, Object.class);
    }
    
    /**
     * Returns a list of all fields used by this object from it's class and all super classes until
     * the stop class.
     * 
     * @param objectClass the class
     * @param stopClass the stop class
     * @return a list of fields
     */
    public static <T> List<Field> getAllFields(final Class<T> objectClass, final Class<? super T> stopClass) {
        final List<Field> fieldList = new ArrayList<Field>();
        collectAllFields(objectClass, stopClass, fieldList);
        
        return fieldList;
    }
    
    /**
     * Collects all fields used by this object from it's class and all super classes until the stop
     * class.
     * 
     * @param objectClass the class
     * @param stopClass the stop class
     * @param fieldList a list of fields
     */
    private static void collectAllFields(final Class<?> objectClass, final Class<?> stopClass, final List<Field> fieldList) {
        if (!objectClass.equals(stopClass)) {
            final Field[] newFields = objectClass.getDeclaredFields();
            fieldList.addAll(Arrays.asList(newFields));
            
            final Class<?> superClass = objectClass.getSuperclass();
            
            if (superClass != null) {
                collectAllFields(superClass, stopClass, fieldList);
            }
        }
    }
}
