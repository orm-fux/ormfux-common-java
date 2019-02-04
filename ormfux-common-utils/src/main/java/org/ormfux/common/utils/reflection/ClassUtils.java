package org.ormfux.common.utils.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ormfux.common.utils.reflection.exception.ElementNotFoundException;

/**
 * Utilities for class analysis.
 */
public final class ClassUtils {
    
    private ClassUtils() {
    }
    
    /**
     * Creates a new Object from the given Class. Uses the public, no-argument constructor.
     * 
     * @param objectType The class.
     * @return The instance.
     */
    public static <T> T createObject(final Class<T> objectType) {
        return createObject(objectType, null, null);
    }
    
    /**
     * Creates a new Object from the given Class. Uses the public, no-argument constructor.
     * 
     * @param objectType The class.
     * @param constructorArgTypes Types of the constructor parameters (in the order they are defined in the constructor)
     * @param constructorArgs The values for the constructor parameters (in the order they are used).
     * @return The instance.
     */
    public static <T> T createObject(final Class<T> objectType, 
                                     final List<Class<?>> constructorArgTypes,
                                     final List<Object> constructorArgs) {
        
        if (constructorArgTypes != null) {
            if (constructorArgs == null) {
                throw new IllegalArgumentException("Argument values are required when the argument types are defined.");
            } else if (constructorArgTypes.size() != constructorArgs.size()) {
                throw new IllegalArgumentException("Not enough values for the constructor arguments.");
            }
        }
        
        try {
            if (constructorArgTypes != null) {
                return objectType.getDeclaredConstructor(constructorArgTypes.toArray(new Class[0]))
                                 .newInstance(constructorArgs.toArray(new Object[0]));
            } else {
                return objectType.getDeclaredConstructor().newInstance();
            }
            
        } catch (final InstantiationException | IllegalAccessException 
                        | IllegalArgumentException | InvocationTargetException
                        | NoSuchMethodException | SecurityException e) {
            throw new org.ormfux.common.utils.reflection.exception.InstantiationException("Error creating new instance of class " + objectType, e);
        }
    }
    
    /**
     * Gets the first class type argument which matches the generic declaration of the super-class.
     * <i>Generic interfaces are not supported!</i>
     * 
     * @param genericProviderType The class defining the generic.
     * @param typeVariable The type variable for which to resolve the type.
     * @return The resolved type; {@code null} when not found.
     */
    public static <T> Class<?> getTypeForGeneric(final Class<T> genericProviderType, final TypeVariable<?> typeVariable) {
        final ParameterizedType parameterizedSuperType = ClassUtils.findFirstParameterizedSuperclass(genericProviderType);
        
        if (parameterizedSuperType.getRawType() instanceof Class) {
            //try identifying by name
            final Class<?> superClass = (Class<?>) parameterizedSuperType.getRawType();
            final TypeVariable<?>[] superTypeParameters = superClass.getTypeParameters();
            
            if (superTypeParameters.length > 0) {
                for (int typeVarIndex = 0; typeVarIndex < superTypeParameters.length; typeVarIndex++) {
                    if (superTypeParameters[typeVarIndex].getName().equals(typeVariable.getName())) {
                         //found a match by name!
                         return (Class<?>) parameterizedSuperType.getActualTypeArguments()[typeVarIndex];
                    }
                }
            }
        } else {
            //fallback. try identifying by type.
            final Type[] bounds = typeVariable.getBounds();
            final Class<?> boundType;
            
            if (bounds[0] instanceof ParameterizedType) {
                boundType = (Class<?>) ((ParameterizedType) bounds[0]).getRawType();
            } else {
                boundType = (Class<?>) bounds[0];
            }
            
            final Type[] typeArguments = parameterizedSuperType.getActualTypeArguments();
            
            for (final Type typeArgument : typeArguments) {
                if (boundType.isAssignableFrom((Class<?>) typeArgument)) {
                    return (Class<?>) typeArgument;
                }
                
            }
        }
        
        return null;
    }
    
    /**
     * Traverses the inheritance hierarchy bottom-up an returns the first super-class, which has a generic.
     * 
     * @param objectType The class for which to get the generic super class.
     * @return ParameterizedType of super class; {@code null} when there is none.
     */
    public static ParameterizedType findFirstParameterizedSuperclass(final Class<?> objectType) {
        Class<?> currentClass = objectType;
        
        while (currentClass != Object.class) {
            final Type genericSuperclass = currentClass.getGenericSuperclass();
            
            if (genericSuperclass instanceof ParameterizedType) {
                return (ParameterizedType) genericSuperclass;
            } else {
                currentClass = currentClass.getSuperclass();
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
        return retrieveDataType(clazz, 0);
    }
    
    /**
     * Retrieve the class parameter at position pos.
     * 
     * @param clazz class with type parameter
     * @param pos type parameter position
     * @return class parameter.
     */
    public static Class<?> retrieveDataType(final Class<?> clazz, final int pos) {
        final ParameterizedType parameterizedSuperclass = findFirstParameterizedSuperclass(clazz);
        
        if (parameterizedSuperclass == null) {
            throw new ElementNotFoundException("Incompatible class. Class must be (directly or indirectly) inherited from one generic class.");
        } else {
            final Type[] typeArguments = parameterizedSuperclass.getActualTypeArguments();
            
            if (typeArguments.length <= pos) {
                throw new ElementNotFoundException("To less generic declarations.");
            } else {
                final Type typeArgument = typeArguments[pos];
                
                if (typeArgument instanceof Class) {
                    return (Class<?>) typeArgument;
                } else {
                    throw new ElementNotFoundException("DataType is not a Class.");
                }
                
            }
            
        }
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
        if (StringUtils.isBlank(fieldName)) {
            throw new IllegalArgumentException("The fieldName is required.");
        }
        
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
        final List<Field> fieldList = new ArrayList<>();
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
