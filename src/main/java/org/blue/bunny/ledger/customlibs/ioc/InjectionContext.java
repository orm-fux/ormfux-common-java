package org.blue.bunny.ledger.customlibs.ioc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.blue.bunny.ledger.customlibs.ioc.annotations.Bean;
import org.blue.bunny.ledger.customlibs.ioc.annotations.Init;
import org.blue.bunny.ledger.customlibs.ioc.annotations.Inject;
import org.blue.bunny.ledger.customlibs.utils.reflection.ClassUtils;

/**
 * Simple inversion of control implementation: We don't want to constantly create new Objects
 * or pass Objects around. This can be used to create new Objects of classes annotated with 
 * {@link Bean}. The instances are cached in this context. So, this context provides 
 * <i>singleton</i> instances when asked for the same type multiple times. Property values
 * of the instances are automatically "injected" with instances of the respective property's 
 * type (as long as the type is also annotated with {@link Bean}). Only properties annotated 
 * with {@link Inject} are automatically set. The property values are also kept in this 
 * context's cache.
 * <p/>
 * It is also possible to create non-singleton beans. They are not stored ina context. Each time 
 * such a bean has to be injected a new instance is created. So beware of circular references 
 * of non-singletons!
 * <p/>
 * <i>Why is this implemented?</i>
 * <ul>
 *  <li>See above.</li>
 *  <li>A library like Spring would be overkill.</li>
 * </ul>
 *
 */
public final class InjectionContext {
    
    /**
     * The instance cache of this context.
     */
    private static Map<Class<?>, Object> beans = new HashMap<>();
    
    /**
     * Creates a new instance of the given type or gets the matching instance from this context's cache. 
     * The type has to be annotated with {@link Bean}. Properties of the type, annotated with {@link Inject},
     * get a value either from this context's cache or as a new instance that is then also placed in 
     * this context's cache. 
     * 
     * @param beanType The bean type.
     * @return The instance.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(final Class<T> beanType) {
        if (isBeanType(beanType)) {
            final Object bean = beans.get(beanType);
            
            if (bean != null) {
                return (T) bean;
                
            } else {
                return addBean(beanType);
                
            }
            
        } else {
            throw new IllegalArgumentException("Not a bean type: " + beanType);
        }
        
    }
    
    /**
     * Adds a new instance of the given type to this context's cache. Field values are injected, 
     * too. This may result in further creation and placement of instances in this cache.
     * 
     * @param beanType The type to instantiate.
     * @return The created instance.
     */
    private static <T> T addBean(final Class<T> beanType) {
        try {
            final T bean = ClassUtils.createObject(beanType);
            //For circular references put this in the content immediately.
            if (beanType.getAnnotation(Bean.class).singleton()) {
                beans.put(beanType, bean);
            }
            
            //Inject values into bean.
            Class<?> currentType = beanType;
            
            while (currentType != null) {
                for (final Field beanField : currentType.getDeclaredFields()) {
                    Class<?> fieldType = null;
                    
                    if (beanField.isAnnotationPresent(Inject.class)) {
                        
                        //we need to do some extra work for generic fields.
                        final Type genericType = beanField.getGenericType();
                        
                        if (genericType instanceof TypeVariable) {
                            final TypeVariable<?> typeVariable = (TypeVariable<?>) genericType;
                            //TODO move this to ClassUtils.
                            //get first class type argument which is a super type of the generic declaration
                            //how to identify by name?
                            final Type[] bounds = typeVariable.getBounds();
                            final Class<?> boundType;
                            
                            if (bounds[0] instanceof ParameterizedType) {
                                boundType = (Class<?>) ((ParameterizedType) bounds[0]).getRawType();
                            } else {
                                boundType = (Class<?>) bounds[0];
                            }
                            
                            final ParameterizedType parameterizedSuperClass = ClassUtils.findFirstParameterizedSuperclass(beanType);
                            final Type[] typeArguments = parameterizedSuperClass.getActualTypeArguments();
                            
                            for (final Type typeArgument : typeArguments) {
                                if (boundType.isAssignableFrom((Class<?>) typeArgument)) {
                                    fieldType = (Class<?>) typeArgument;
                                    break;
                                }
                                
                            }
                            
                        } else {
                            fieldType = beanField.getType();
                        }
                        
                         final Object fieldValue = getBean(fieldType);
                         
                         if (fieldValue != null) {
                             if (!beanField.canAccess(bean)) {
                                 beanField.setAccessible(true);
                             }
                             
                             beanField.set(bean, fieldValue);
                             
                         } else {
                             throw new RuntimeException("There is no bean to inject into " + beanType.getName() + "." + beanField.getName());
                         }
                    }
                }
                
                currentType = currentType.getSuperclass();
            }
            
            //call initialization methods (super-classes first)
            initializeBean(beanType, bean);
            
            return bean;
            
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot create new bean of type " + beanType, e);
        }
    }
    
    /**
     * Calls the methods of the bean, which have the {@code @Init} annotation. Invokes top-down in the inheritance 
     * hierarchy - i.e. super-classes first. The methods must be public, non-static, and have no parameters.
     */
    private static <T> void initializeBean(final Class<T> beanType, final T bean) throws IllegalAccessException {
        List<Class<?>> superClasses = new ArrayList<>();
        Class<?> superClass = beanType;
        
        do {
            superClasses.add(0, superClass);
            superClass = superClass.getSuperclass();
        } while (superClass != null && superClass != Object.class);
        
        try {
            for (final Class<?> curSuperClass : superClasses) {
                for (final Method method : curSuperClass.getDeclaredMethods()) {
                    if (Modifier.isPublic(method.getModifiers()) 
                            && !Modifier.isStatic(method.getModifiers())
                            && method.isAnnotationPresent(Init.class)) {
                        method.invoke(bean);
                    }
                }
            }
        } catch (final IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException("Error calling initialization method.", e);
        }
    }
    
    /**
     * Checks if the given type is a type which this context can handle. This is the case when 
     * it is annotated with {@link Bean} and not abstract.
     * 
     * @param beanType The type.
     * @return {@code true} when this context can handle the type.
     */
    private static boolean isBeanType(Class<?> beanType) {
        return beanType.isAnnotationPresent(Bean.class) && !Modifier.isAbstract(beanType.getModifiers());
    }
    
}
