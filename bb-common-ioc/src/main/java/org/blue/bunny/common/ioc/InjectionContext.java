package org.blue.bunny.common.ioc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.blue.bunny.common.ioc.annotations.Bean;
import org.blue.bunny.common.ioc.annotations.Init;
import org.blue.bunny.common.ioc.annotations.Inject;
import org.blue.bunny.common.utils.ListUtils;
import org.blue.bunny.common.utils.reflection.ClassUtils;

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
     * Additional definitions of beans. These beans don't have a {@link Bean} annotation.
     */
    private static final List<BeanDescriptor> BEAN_DESCRIPTORS = new ArrayList<>();
    
    /**
     * The instance cache of this context.
     */
    private static final Map<Class<?>, Object> BEANS = new HashMap<>();
    
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
        final Object bean = BEANS.get(beanType);
        
        if (bean != null) {
            return (T) bean;
            
        } else {
            if (isAnnotatedBeanType(beanType)) {
                return addAnnotatedBean(beanType);
            } else if (isManualBeanType(beanType)) {
                return addManualBean(beanType);
                
            } else {
                throw new IllegalArgumentException("Not a bean type: " + beanType);
            }
        }
        
    }
    
    /**
     * Adds a new instance of the given type to this context's cache. Field values are injected, 
     * too. This may result in further creation and placement of instances in this cache.
     * 
     * @param beanType The type to instantiate.
     * @return The created instance.
     */
    @SuppressWarnings("unchecked")
    private static <T> T addManualBean(final Class<T> beanType) {
        final BeanDescriptor beanDescriptor = getBeanDescriptor(beanType);
        
        return addBean((Class<T>) beanDescriptor.getBeanType(), beanDescriptor.isSingleton());
    }
    
    /**
     * Adds a new instance of the given type to this context's cache. Field values are injected, 
     * too. This may result in further creation and placement of instances in this cache.
     * <p/>
     * The type must be annotated with {@link Bean}.
     * 
     * @param beanType The type to instantiate.
     * @return The created instance.
     */
    private static <T> T addAnnotatedBean(final Class<T> beanType) {
        return addBean(beanType, beanType.getAnnotation(Bean.class).singleton());
    }
    
    /**
     * Adds a new instance of the given type to this context's cache. Field values are injected, 
     * too. This may result in further creation and placement of instances in this cache.
     * <p/>
     * The type must be annotated with {@link Bean}.
     * 
     * @param beanType The type to instantiate.
     * @param singleton If the bean is a singleton.
     * @return The created instance.
     */
    private static <T> T addBean(final Class<T> beanType, final boolean singleton) {
        try {
            final T bean = ClassUtils.createObject(beanType);
            
            //For circular references put this in the content immediately.
            if (singleton) {
                BEANS.put(beanType, bean);
            }
            
            //Inject values into bean.
            injectValues(beanType, bean);
            
            //call initialization methods (super-classes first)
            initializeBean(beanType, bean);
            
            return bean;
            
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot create new bean of type " + beanType, e);
        }
    }
    
    /**
     * Injects the values into the bean properties.
     * 
     * @param beanType The type of the bean.
     * @param bean The bean in which to inject.
     * 
     * @throws IllegalAccessException
     */
    private static <T> void injectValues(final Class<T> beanType, final T bean) throws IllegalAccessException {
        Class<?> currentType = beanType;
        
        while (currentType != null) {
            for (final Field beanField : currentType.getDeclaredFields()) {
                
                if (beanField.isAnnotationPresent(Inject.class)) {
                    //we need to do some extra work for generic fields.
                    final Type genericType = beanField.getGenericType();
                    final Class<?> fieldType;
                    
                    if (genericType instanceof TypeVariable) {
                        final TypeVariable<?> typeVariable = (TypeVariable<?>) genericType;
                        fieldType = ClassUtils.getTypeForGeneric(beanType, typeVariable);
                        
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
     * Adds a definition of a bean (without Bean-annotation) to the InjectionContext.
     * Use this, when you want to use something as a Bean, but cannot modify its source.
     * <p/>
     * Each type can only be registered once!
     *  
     * @param beanType The type of the bean.
     * @param singleton If the bean shall be a singleton.
     */
    public static void addBeanDefinition(final Class<?> beanType, final boolean singleton) {
        addBeanDefinition(new BeanDescriptor(beanType, singleton));
    }
    
    /**
     * Adds a definition of a bean (without Bean-annotation) to the InjectionContext.
     * Use this, when you want to use something as a Bean, but cannot modify its source.
     * <p/>
     * Each type can only be registered once!
     *  
     * @param beanDescriptor The bean definition.
     */
    public static void addBeanDefinition(final BeanDescriptor beanDescriptor) {
        if (!BEAN_DESCRIPTORS.contains(beanDescriptor)) {
            if (isAnnotatedBeanType(beanDescriptor.getBeanType())) {
                throw new IllegalArgumentException("The type is already a bean. No need to add it manually: " + beanDescriptor.getBeanType());
            } else {
                BEAN_DESCRIPTORS.add(beanDescriptor);
            }
        }
    }
    
    /**
     * Checks, if the provided type is type registered as a "manual bean".
     *  
     * @param beanType The bean type.
     * @return {@code true} when a manual bean.
     */
    private static boolean isManualBeanType(final Class<?> beanType) {
        return getBeanDescriptor(beanType) != null;
    }
    
    /**
     * Gets the descriptor for the bean type.
     * 
     * @param beanType The bean type.
     * @return the descriptor; {@code null} when there is none.
     */
    private static BeanDescriptor getBeanDescriptor(final Class<?> beanType) {
        return ListUtils.selectFirst(BEAN_DESCRIPTORS, beanDescriptor -> Objects.equals(beanType, beanDescriptor.getBeanType()));
    }
    
    /**
     * Checks if the given type is a type which this context can handle. This is the case when 
     * it is annotated with {@link Bean} and not abstract.
     * 
     * @param beanType The type.
     * @return {@code true} when this context can handle the type.
     */
    private static boolean isAnnotatedBeanType(Class<?> beanType) {
        return beanType.isAnnotationPresent(Bean.class) && !Modifier.isAbstract(beanType.getModifiers());
    }
    
}
