package org.blue.bunny.ledger.customlibs.utils.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * utilities for working with methods and "method/property paths".
 */
public final class MethodUtils {
    
    private MethodUtils() {
    }
    
    /**
     * Gets the setter method for the property of the class.
     *
     * @param entityClass The class owning the property.
     * @param property The name of the property.
     * @return The setter method.
     */
    public static Method findPropertySetter(final Class<?> entityClass, final String property) throws NoSuchMethodException {
        return findPropertySetter(entityClass, property, null);
    }
    
    /**
     * Gets the setter method for the property of the class.
     *
     * @param entityClass The class owning the property.
     * @param property The name of the property.
     * @param propertyType The parameter type of the setter.
     * @return The setter method.
     */
    public static Method findPropertySetter(final Class<?> entityClass, final String property, final Class<?> propertyType) throws NoSuchMethodException {
        final String setterName = "set" + StringUtils.capitalize(property);
        
        if (propertyType != null) {
            Method setterMethod = null; 
            
            try {
                setterMethod = entityClass.getMethod(setterName, propertyType);
            } catch (final NoSuchMethodException e) {}
            
            if (setterMethod == null) {
                if (propertyType.isPrimitive()) {
                    //use "object type" if method for primitive type is not found
                    setterMethod = entityClass.getMethod(setterName, TypeUtils.primitiveTypeToTypeMap.get(propertyType));
                } else if (TypeUtils.primitiveTypeToTypeMap.containsValue(propertyType)) {
                    //use primitive type if method for "object type" is not found
                    Class<?> primitiveType = null;
                    
                    for (Class<?> type : TypeUtils.primitiveTypeToTypeMap.keySet()) {
                        if (TypeUtils.primitiveTypeToTypeMap.get(type).equals(propertyType)) {
                            primitiveType = type;
                            break;
                        }
                    }
                    
                    setterMethod = entityClass.getMethod(setterName, primitiveType);
                } else {
                    //find method with assignable parameter if no primitive type relation
                    final Method[] methods = entityClass.getMethods();
                    
                    for (Method method : methods) {
                        if (method.getName().equals(setterName)
                                && method.getParameterTypes().length == 1
                                && method.getParameterTypes()[0].isAssignableFrom(propertyType)
                                && Modifier.isPublic(method.getModifiers())) {
                            return method;
                        }
                    }
                }
            }
            
            if (setterMethod != null && Modifier.isPublic(setterMethod.getModifiers())) {
                return setterMethod;
            }
        } else {
            final Method[] methods = entityClass.getMethods();
            
            for (Method method : methods) {
                if (method.getName().equals(setterName)
                        && method.getParameterTypes().length == 1
                        && Modifier.isPublic(method.getModifiers())) {
                    return method;
                }
            }
            
        }
        
        throw new NoSuchMethodException("Property setter for '"+ property + "' of '"+ entityClass.getName() +"' not found");
    }
    
    /**
     * Gets the getter method for the property of the class. Also considers that boolean properties can have getters
     * with prefix "is" or "get".
     *
     * @param entityClass The class owning the property.
     * @param property The name of the property.
     * @param propertyType The return type of the getter.
     * @return The setter method.
     */
    public static Method findPropertyGetter(final Class<?> entityClass, final String property, final Class<?> propertyType) throws NoSuchMethodException {
        if (propertyType == null) {
            return findPropertyGetter(entityClass, property);
        }
        
        final String getterName = "get" + StringUtils.capitalize(property);
        final String booleanGetterName = "is" + StringUtils.capitalize(property);
        
        Method getterMethod = null;
        
        if (propertyType.equals(Boolean.class) || propertyType.equals(boolean.class)) {
            try {
               getterMethod = entityClass.getMethod(booleanGetterName);
            } catch (final NoSuchMethodException e) {
                getterMethod = entityClass.getMethod(getterName);
            }
        } else {
            getterMethod = entityClass.getMethod(getterName);
        }
        
        if (getterMethod != null && Modifier.isPublic(getterMethod.getModifiers())) {
            if (propertyType.isAssignableFrom(getterMethod.getReturnType())) {
                return getterMethod;
            } else {
                Class<?> requiredReturnType = propertyType;
                
                if (propertyType.isPrimitive()) {
                    requiredReturnType = TypeUtils.primitiveTypeToTypeMap.get(propertyType);
                } else {
                    for (Class<?> type : TypeUtils.primitiveTypeToTypeMap.keySet()) {
                        if (TypeUtils.primitiveTypeToTypeMap.get(type).equals(propertyType)) {
                            requiredReturnType = type;
                            break;
                        }
                    }
                }
                
                if (requiredReturnType.isAssignableFrom(getterMethod.getReturnType())) {
                    return getterMethod;
                }
            }
        }
                 
        
        throw new NoSuchMethodException("Property getter for '"+ property + "' of '"+ entityClass.getName() +"' not found");
        
    }
    
    /**
     * Gets the getter method for the property of the class. Also considers that boolean properties can have getters
     * with prefix "is" or "get".
     *
     * @param entityClass The class owning the property.
     * @param property The name of the property.
     * @return The setter method.
     */
    public static Method findPropertyGetter(Class<?> entityClass, String property) throws NoSuchMethodException {
        final String getterName = "get" + StringUtils.capitalize(property);
        final String booleanGetterName = "is" + StringUtils.capitalize(property);
        
        Method getterMethod = null;
        
        try {
            getterMethod = entityClass.getMethod(getterName);
        } catch (final NoSuchMethodException e) {}
        
        if (getterMethod == null) {
            try {
                getterMethod = entityClass.getMethod(booleanGetterName);
            } catch (final NoSuchMethodException e) {}
        }
        
        if (getterMethod != null && getterMethod.getReturnType().equals(Boolean.TYPE)) {
            try {
                final Method booleanGetterMethod = entityClass.getMethod(booleanGetterName);
                
                if (booleanGetterMethod != null) {
                    getterMethod = booleanGetterMethod;
                }
            } catch (final NoSuchMethodException e) {}
        }
        
        if (getterMethod != null && Modifier.isPublic(getterMethod.getModifiers())) {
            return getterMethod;
        }
        
        throw new NoSuchMethodException("Property getter for '"+ property + "' of '"+ entityClass.getName() +"' not found");
        
    }
    
    /**
     * Transforms aproperty path to a list of methods. E.h. "{@code person.address.setStreet(String)}" will become a list consisting
     * of the methods "{@code getPerson()}", "{@code getAddress()}", and "{@code setStreet(String)}".
     * 
     * @param root The class at which the property path starts.
     * @param pathToMethod The property path, separated by colons, with the final entry being a proper method name.
     * @param returnType The types of the values returned by the final method.
     * @param parameterTypes The types of the parameters of the final method.
     * @return the list of methods, which need to be invoked.
     */
    public static List<Method> getPathToMethod(final Class<?> root, 
                                               final String pathToMethod, 
                                               final Class<?> returnType, 
                                               final Class<?>... parameterTypes) throws NoSuchMethodException {
        if (pathToMethod == null || pathToMethod.isEmpty() || pathToMethod.startsWith(".") || pathToMethod.endsWith(".")) {
            throw new IllegalArgumentException("Invalid path to method '"+pathToMethod+"'");
        }
        
        final int lastMethodPos = pathToMethod.lastIndexOf('.');
        
        if (lastMethodPos > 0) {
            final List<Method> methodPath = getPropertyGetterPath(root, pathToMethod.substring(0, lastMethodPos), null);
            final Class<?> lastGetterReturnType = methodPath.get(methodPath.size() - 1).getReturnType();
            methodPath.add(getPublicMethod(lastGetterReturnType, pathToMethod.substring(lastMethodPos + 1), returnType, parameterTypes));
            
            return methodPath;
        } else {
            final List<Method> methodPath = new ArrayList<Method>(1);
            methodPath.add(getPublicMethod(root, pathToMethod, returnType, parameterTypes));
            
            return methodPath;
        }
    }
    
    /**
     * Transforms aproperty path to a list of methods. E.h. "{@code person.address.setStreet(String)}" will become a list consisting
     * of the methods "{@code getPerson()}", "{@code getAddress()}", and "{@code setStreet(String)}". Allows to provide the indices
     * of the final method, for which the parameter types are unknown. This method tries to find the best matching method based on the
     * known parameter types.
     * 
     * @param root The class at which the property path starts.
     * @param pathToMethod The property path, separated by colons, with the final entry being a proper method name.
     * @param returnType The types of the values returned by the final method.
     * @param parameterTypes The types of the parameters of the final method.
     * @return the list of methods, which need to be invoked.
     *
     * @see #getPublicMethodWithNullArguments(..)
     */
    public static List<Method> getPathToMethodWithNullArguments(final Class<?> root, 
                                                                final String pathToMethod, 
                                                                final Class<?> returnType, 
                                                                final Class<?>[] parameterTypes, 
                                                                final int[] nullArgumentIndices) throws NoSuchMethodException {
        if (pathToMethod == null || pathToMethod.isEmpty() || pathToMethod.startsWith(".") || pathToMethod.endsWith(".")) {
            throw new IllegalArgumentException("Invalid path to method '"+pathToMethod+"'");
        }
        
        final int lastMethodPos = pathToMethod.lastIndexOf('.');
        
        if (lastMethodPos > 0) {
            final List<Method> methodPath = getPropertyGetterPath(root, pathToMethod.substring(0, lastMethodPos), null);
            final Class<?> lastGetterReturnType = methodPath.get(methodPath.size() - 1).getReturnType();
            
            methodPath.add(getPublicMethodWithNullArguments(lastGetterReturnType, 
                                                            pathToMethod.substring(lastMethodPos + 1), 
                                                            returnType, 
                                                            parameterTypes, 
                                                            nullArgumentIndices));
            
            return methodPath;
        } else {
            final List<Method> methodPath = new ArrayList<Method>(1);
            methodPath.add(getPublicMethodWithNullArguments(root, pathToMethod, returnType, parameterTypes, nullArgumentIndices));
            
            return methodPath;
        }
    }
    
    /**
     * Gets the public method from the class that matches the interface definition.
     * 
     * @param root The clas sowning the method.
     * @param methodName The name of the method.
     * @param returnType The type returned by the method; can be {@code null}.
     * @param parameterTypes The types of the method parameters in the order as defined by the method.
     * @return The method.
     */
    public static Method getPublicMethod(final Class<?> root, 
                                         final String methodName, 
                                         final Class<?> returnType, 
                                         final Class<?>... parameterTypes) throws NoSuchMethodException {
        final Method method = root.getMethod(methodName, parameterTypes);
        
        if (Modifier.isPublic(method.getModifiers()) ) {
            if (returnType == null || checkTypeAssignability(returnType, method.getReturnType())) {
                return method;
            }
        }
        
        throw new NoSuchMethodException("No such method");
    }
    
    /**
     * Gets the public method from the class that matches the interface definition. Allows to define the indices
     * of the parameters, for which the type is not known. This method tries to find the best matching method based
     * on the known types.
     * 
     * @param root The clas sowning the method.
     * @param methodName The name of the method.
     * @param returnType The type returned by the method; can be {@code null}.
     * @param parameterTypes The types of the method parameters in the order as defined by the method.
     * @param nullArgumentIndices The indices of the parameters for which the type is unknown.
     * @return The method.
     */
    public static Method getPublicMethodWithNullArguments(final Class<?> root, 
                                                          final String methodName, 
                                                          final Class<?> returnType, 
                                                          final Class<?>[] parameterTypes, 
                                                          final int[] nullArgumentIndices) throws NoSuchMethodException {
        final int noOfParameters = (parameterTypes == null ? 0 : parameterTypes.length) + (nullArgumentIndices == null ? 0 : nullArgumentIndices.length);
        final List<?> nullArgIndexList = nullArgumentIndices == null ? Collections.EMPTY_LIST : Arrays.asList(nullArgumentIndices);
        
        Method result = null;
        
        for (final Method method : root.getMethods()) {
            final Class<?>[] methodParameterTypes = method.getParameterTypes();
            
            if (method.getName().equals(methodName)
                    && Modifier.isPublic(method.getModifiers())
                    && methodParameterTypes.length == noOfParameters) {
                
                boolean argumentsMatch = true;
                
                for (int i = 0, parameterTypeIndex = 0; i < methodParameterTypes.length; i++) {
                    if (!nullArgIndexList.contains(i)) {
                        if(parameterTypes != null && !checkTypeAssignability(methodParameterTypes[i], parameterTypes[parameterTypeIndex++])){
                            argumentsMatch = false;
                            break;
                        }
                    }
                }
                
                if (argumentsMatch) {
                    result = method;
                    break;
                }
                
            }
        }
        
        if (result != null && (returnType == null || checkTypeAssignability(returnType, result.getReturnType()))) {
            return result;
        }
        
        throw new NoSuchMethodException("No such method");
    }
    
    //TODO Move to TypeUtils
    @Deprecated
    private static boolean checkTypeAssignability(final Class<?> target, final Class<?> assigned) {
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
    }
    
    /**
     * Transforms a property path to a list of methods for reading a nested proeprty. E.h. "{@code person.address.street}" will 
     * become a list consisting of the methods "{@code getPerson()}", "{@code getAddress()}", and "{@code getStreet()}".
     * 
     * @param root The class at which the property path starts.
     * @param propertyPath The property path, separated by colons.
     * @param propertyType The type of the final property (optional).
     * @return the list of methods, which need to be invoked.
     */
    public static List<Method> getPropertyGetterPath(final Class<?> root, final String propertyPath, final Class<?> propertyType) throws NoSuchMethodException {
        final String[] properties = propertyPath.split("\\.");
        
        final List<Method> getterMethods = new ArrayList<Method>();
        
        Class<?> currentClass = root;
        
        for (int pathIndex = 0; pathIndex < properties.length; pathIndex++) {
            final Method getterMethod;
            
            if (pathIndex < properties.length - 1 || propertyType == null) {
                getterMethod = findPropertyGetter(currentClass, properties[pathIndex]);
            } else {
                getterMethod = findPropertyGetter(currentClass, properties[pathIndex], propertyType);
            }
            
            getterMethods.add(getterMethod);
            currentClass = getterMethod.getReturnType();
        }
        
        return getterMethods;
    }

    /**
     * Transforms a property path to a list of methods for writing a nested property. E.h. "{@code person.address.street}" will 
     * become a list consisting of the methods "{@code getPerson()}", "{@code getAddress()}", and "{@code setStreet(String)}".
     * 
     * @param root The class at which the property path starts.
     * @param propertyPath The property path, separated by colons.
     * @param propertyType The type of the final property (required).
     * @return the list of methods, which need to be invoked.
     */
    public static List<Method> getPropertySetterPath(final Class<?> root, final String propertyPath, final Class<?> propertyType) throws NoSuchMethodException {
        final String[] properties = propertyPath.split("\\.");
        final List<Method> getterMethods = new ArrayList<Method>();
        
        Class<?> currentClass = root;
        
        for (int pathIndex = 0; pathIndex < properties.length; pathIndex++) {
            final Method getterMethod;
            
            if (pathIndex < properties.length - 1) {
                getterMethod = findPropertyGetter(currentClass, properties[pathIndex]);
            } else {
                getterMethod = findPropertySetter(currentClass, properties[pathIndex], propertyType);
            }
            
            getterMethods.add(getterMethod);
            currentClass = getterMethod.getReturnType();
        }
        
        return getterMethods;
    }
    
    /**
     * Reads the value of a nested property from an Object.
     *
     * @param root The Object from which to read.
     * @param propertyPath The path to the nested property. Property names are colon-separated.
     * @param createIntermediate If the intermediate objects on the property path should be automatically created
     *                           when they are not defined. The types need to have a public no-argument constructor.
     */
    public static Object invokePropertyGetterPath(final Object root, 
                                                  final String propertyPath, 
                                                  final boolean createIntermediate) throws NoSuchMethodException, 
                                                                                           IllegalAccessException, 
                                                                                           IllegalArgumentException, 
                                                                                           InvocationTargetException, 
                                                                                           InstantiationException {
        return invokePropertyGetterPath(root, propertyPath, null, createIntermediate);
    }
    
    /**
     * Reads the value to a nested property of an Object.
     *
     * @param root The Object acting as root of the property path.
     * @param propertyPath The path to the nested property. Property names are colon-separated.
     * @param propertyType The type of the final property.
     * @param createIntermediate If the intermediate objects on the property path should be automatically created
     *                           when they are not defined. The types need to have a public no-argument constructor.
     */
    public static Object invokePropertyGetterPath(final Object root, 
                                                  final String propertyPath, 
                                                  final Class<?> propertyType, 
                                                  final boolean createIntermediate) throws NoSuchMethodException, 
                                                                                           IllegalAccessException, 
                                                                                           IllegalArgumentException, 
                                                                                           InvocationTargetException, 
                                                                                           InstantiationException {
        final List<Method> propertyGetterPath = getPropertyGetterPath(root.getClass(), propertyPath, propertyType);
        
        return invokePropertyGetterPath(root, propertyGetterPath, createIntermediate);
    }
    
    /**
     * Reads the value of a nested property from an Object.
     *
     * @param root The Object from which to read.
     * @param propertyGetterPath The getter methods to invoke (each on the result of the previous getter).
     * @param createIntermediate If the intermediate objects on the property path should be automatically created
     *                           when they are not defined. The types need to have a public no-argument constructor.
     */
    public static Object invokePropertyGetterPath(final Object root, 
                                                  final List<Method> propertyGetterPath, 
                                                  final boolean createIntermediate) throws IllegalAccessException, 
                                                                                           IllegalArgumentException, 
                                                                                           InvocationTargetException, 
                                                                                           InstantiationException, 
                                                                                           NoSuchMethodException {
        Object currentObject = root;
        
        for (int i = 0; i < propertyGetterPath.size(); i++) {
            if (currentObject == null) {
                return null;
            }
            
            final Object invocationResult = propertyGetterPath.get(i).invoke(currentObject);
            
            if (invocationResult == null && i < propertyGetterPath.size() - 1 && createIntermediate) {
                //object is missing on the path to last getter. create intermediate objects
                currentObject = createObjectsForPath(currentObject, propertyGetterPath.subList(i, propertyGetterPath.size() - 1));
                //last getter for next loop-cycle
                i = propertyGetterPath.size() - 2;
            } else {
                currentObject = invocationResult;
            }
        }
        
        return currentObject;
    }
    
    /**
     * Writes the value to a nested property of an Object.
     *
     * @param root The Object acting as root of the property path.
     * @param propertyPath The path to the nested property. Property names are colon-separated.
     * @param valueToSet The value to write to the property.
     * @param createIntermediate If the intermediate objects on the property path should be automatically created
     *                           when they are not defined. The types need to have a public no-argument constructor.
     */
    public static Object invokePropertySetterPath(final Object root, 
                                                  final String propertyPath, 
                                                  final Object valueToSet, 
                                                  final boolean createIntermediate) throws NoSuchMethodException, 
                                                                                           IllegalAccessException, 
                                                                                           IllegalArgumentException, 
                                                                                           InvocationTargetException, 
                                                                                           InstantiationException {
        final List<Method> propertySetterPath = getPropertySetterPath(root.getClass(), propertyPath, valueToSet == null ? null : valueToSet.getClass());
        
        return invokePropertySetterPath(root, propertySetterPath, valueToSet, createIntermediate);
    }
    
    /**
     * Writes the value to a nested property of an Object.
     *
     * @param root The Object acting as root of the property path.
     * @param propertyPath The path to the nested property. Property names are colon-separated.
     * @param propertySetterPath The methods to invoke (each on the result of the previous). The last one is the setter.
     * @param valueToSet The value to write to the property.
     * @param createIntermediate If the intermediate objects on the property path should be automatically created
     *                           when they are not defined. The types need to have a public no-argument constructor.
     */
    public static Object invokePropertySetterPath(final Object root, 
                                                  final List<Method> propertySetterPath, 
                                                  final Object valueToSet, 
                                                  final boolean createIntermediate) throws IllegalAccessException, 
                                                                                           IllegalArgumentException, 
                                                                                           InvocationTargetException, 
                                                                                           InstantiationException, 
                                                                                           NoSuchMethodException {
        Object currentObject = root;
        
        for (int i = 0; i < propertySetterPath.size() - 1; i++) {
//            if (currentObject == null) {
//                return null;
//            }
            
            final Object invocationResult = propertySetterPath.get(i).invoke(currentObject);
            
            if (invocationResult == null && createIntermediate) {
                if (createIntermediate) {
                    //object is missing on the path to last getter
                    //create intermediate objects
                    currentObject = createObjectsForPath(currentObject, propertySetterPath.subList(i, propertySetterPath.size() - 1));
                    break;
                } else {
                    throw new NullPointerException(propertySetterPath.get(i).getDeclaringClass().getName() 
                                                   +"." + propertySetterPath.get(i).getName() + "() returned null.");
                }
            } else {
                currentObject = invocationResult;
            }
        }
        
        //set the value
        final Method setterMethod = propertySetterPath.get(propertySetterPath.size() - 1);
        final Object result = setterMethod.invoke(currentObject, new Object[]{valueToSet});
        
        return result;
    }
    
    /**
     * Creates the Objects denoted by the list of getter methods, so that we can finally traverse
     * from the root to the last getter.
     * 
     * @param root The start of the path.
     * @param getterPath The getter methods.
     * @return The final created value.
     */
    private static Object createObjectsForPath(final Object root, 
                                               final List<Method> getterPath) throws NoSuchMethodException, 
                                                                                     InstantiationException, 
                                                                                     IllegalAccessException, 
                                                                                     IllegalArgumentException, 
                                                                                     InvocationTargetException {
        Object currentObject = root;
        
        for (final Method getterMethod : getterPath) {
            final String propertyName;
            String getterName = getterMethod.getName();
            
            if (getterName.startsWith("get")) {
                propertyName = getterName.substring(3);
            } else if (getterName.startsWith("is")) {
                propertyName = getterName.substring(2);
            } else {
                throw new IllegalArgumentException("The method '" + getterName + "' does not match the getter method name pattern.");
            }
            
            final Class<?> propertyType = getterMethod.getReturnType();
            final Method propertySetter = findPropertySetter(currentObject.getClass(), propertyName, propertyType);
            final Object newInstance = propertyType.newInstance();
            propertySetter.invoke(currentObject, newInstance);
            currentObject = newInstance;
        }
        
        return currentObject;
    }
}
