package org.ormfux.common.ioc;

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

import org.ormfux.common.ioc.annotations.Bean;
import org.ormfux.common.ioc.annotations.Init;
import org.ormfux.common.ioc.annotations.Inject;
import org.ormfux.common.ioc.exception.BeanInstantiationException;
import org.ormfux.common.ioc.exception.BeanLookupException;
import org.ormfux.common.utils.ListUtils;
import org.ormfux.common.utils.reflection.ClassUtils;
import org.ormfux.common.utils.reflection.exception.InstantiationException;

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
 * It is also possible to create non-singleton beans. Non-singletons are not stored in the context. 
 * Each time such a bean has to be injected a new instance is created. So beware of circular 
 * references of non-singletons!
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
     * The type has to be annotated with {@link Bean} or must be a manually registered one. Properties of 
     * the type, annotated with {@link Inject}, get a value either from this context's cache or as a new 
     * instance that is then also placed in this context's cache. 
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
            validateBeanDefinition(beanType);
            
            final BeanCreationCache<T> newBeanContainer = getBean(beanType, new BeanCreationCache<>());
            BEANS.putAll(newBeanContainer.getNewBeans());
            
            return newBeanContainer.getBean();
        }
        
    }
    
    /**
     * Returns a an existing bean from the caches or creates a new instance. A new instance is only created 
     * when the requested bean is not found in one of the caches. 
     * 
     * @param beanType The type of the bean to create.
     * @param newInstancesCache The cache with all newly created bean instances.
     * @return The bean instance along with the other newly created beans (includes the ones from the cache 
     *         provided to the method).
     */
    @SuppressWarnings("unchecked")
    private static <T> BeanCreationCache<T> getBean(final Class<T> beanType, final BeanCreationCache<?> newInstancesCache) {
        if (BEANS.containsKey(beanType) || newInstancesCache.getCachedBean(beanType) != null) {
            final BeanCreationCache<T> existingInstanceContainer = new BeanCreationCache<>(newInstancesCache);
            
            if (BEANS.containsKey(beanType)) {
                existingInstanceContainer.setBean((T) BEANS.get(beanType));
            } else {
                existingInstanceContainer.setBean((T) newInstancesCache.getCachedBean(beanType));
            }
            
            return existingInstanceContainer;
            
        } else if (isAnnotatedBeanType(beanType)) {
            return createAnnotatedBean(beanType, newInstancesCache);
        } else if (isManualBeanType(beanType)) {
            return createManualBean(beanType, newInstancesCache);
            
        } else {
            //We should never get here!
            throw new BeanLookupException("Not a bean type: " + beanType);
        }
        
    }
    
    /**
     * Validates that the class actually represents a bean. Includes validation of the values to
     * inject into new instances.
     * 
     * @param beanType The bean type to validate.
     * 
     * @throws BeanLookupException When the class or one of its (indirect) properties to inject
     *                             does not represent a valid bean definition. 
     */
    private static void validateBeanDefinition(final Class<?> beanType) throws BeanLookupException {
        validateBeanDefinition(beanType, new ArrayList<>());
    }
    
    /**
     * Validates that the class actually represents a bean. Includes validation of the values to
     * inject into new instances.
     * 
     * @param beanType The bean type to validate.
     * @param discoveredNewBeans The, during the validation process, already found types of beans,
     *                           for which a new instance needs to be created.
     * 
     * @throws BeanLookupException When the class or one of its (indirect) properties to inject
     *                             does not represent a valid bean definition. 
     */
    private static void validateBeanDefinition(final Class<?> beanType, final List<Class<?>> discoveredNewBeans) throws BeanLookupException {
        if (!BEANS.containsKey(beanType) && !discoveredNewBeans.contains(beanType)) {
            if (isAnnotatedBeanType(beanType) || isManualBeanType(beanType)) {
                discoveredNewBeans.add(beanType);
                
                Class<?> currentType = beanType;
                
                while (currentType != null) {
                    for (final Field beanField : currentType.getDeclaredFields()) {
                        final Class<?> fieldBeanType = getTypeToInject(beanType, beanField);
                        
                        if (fieldBeanType != null) {
                            validateBeanDefinition(fieldBeanType, discoveredNewBeans);
                        }
                    }
                    
                    currentType = currentType.getSuperclass();
                }
                
            } else {
                throw new BeanLookupException("Not a bean type: " + beanType);
            }
            
        }
    }
    
    /**
     * Adds a new instance of the given type to this context's cache. Field values are injected, 
     * too. This may result in further creation and placement of instances in this cache.
     * 
     * @param beanType The type to instantiate.
     * @param newInstancesCache The cache with all newly created bean instances.
     * @return The created instance.
     */
    @SuppressWarnings("unchecked")
    private static <T> BeanCreationCache<T> createManualBean(final Class<T> beanType, final BeanCreationCache<?> newInstancesCache) {
        final BeanDescriptor beanDescriptor = getBeanDescriptor(beanType);
        return createBean((Class<T>) beanDescriptor.getBeanType(), beanDescriptor.isSingleton(), newInstancesCache);
    }
    
    /**
     * Adds a new instance of the given type to this context's cache. Field values are injected, 
     * too. This may result in further creation and placement of instances in this cache.
     * <p/>
     * The type must be annotated with {@link Bean}.
     * 
     * @param beanType The type to instantiate.
     * @param newInstancesCache The cache with all newly created bean instances.
     * @return The created instance.
     */
    private static <T> BeanCreationCache<T> createAnnotatedBean(final Class<T> beanType, final BeanCreationCache<?> newInstancesCache) {
        return createBean(beanType, beanType.getAnnotation(Bean.class).singleton(), newInstancesCache);
    }
    
    /**
     * Adds a new instance of the given type to this context's cache. Field values are injected, 
     * too. This may result in further creation and placement of instances in this cache.
     * <p/>
     * The type must be annotated with {@link Bean}.
     * 
     * @param beanType The type to instantiate.
     * @param singleton If the bean is a singleton.
     * @param newInstancesCache The cache with all newly created bean instances.
     * @return The created instance.
     */
    private static <T> BeanCreationCache<T> createBean(final Class<T> beanType, 
                                                       final boolean singleton, 
                                                       final BeanCreationCache<?> newInstancesCache) throws BeanInstantiationException, 
                                                                                                            BeanLookupException {
        try {
            final BeanCreationCache<T> newBeanContainer = new BeanCreationCache<>(newInstancesCache);
            
            final T bean = ClassUtils.createObject(beanType);
            newBeanContainer.setBean(bean);
            
            //For circular references put this in the context immediately.
            if (singleton) {
                newBeanContainer.putBeanInCache(beanType, bean);
            }
            
            //Inject values into bean.
            injectValues(beanType, bean, newBeanContainer);
            
            //call initialization methods (super-classes first)
            initializeBean(beanType, bean);
            
            return newBeanContainer;
            
        } catch (InstantiationException e) {
            throw new BeanInstantiationException("Cannot create new bean instance.", e);
        }
    }
    
    /**
     * Injects the values into the bean properties.
     * 
     * @param beanType The type of the bean.
     * @param bean The bean in which to inject.
     * @param newInstancesCache The cache with all newly created bean instances. Instances created in this method 
     *                          are added to this cache.
     * 
     * @throws IllegalAccessException
     */
    private static <T> void injectValues(final Class<T> beanType, 
                                         final T bean, 
                                         final BeanCreationCache<?> newInstancesCache) throws BeanInstantiationException {
        Class<?> currentType = beanType;
        
        while (currentType != null) {
            for (final Field beanField : currentType.getDeclaredFields()) {
                final Class<?> fieldBeanType = getTypeToInject(beanType, beanField);
                
                if (fieldBeanType != null) {
                    final BeanCreationCache<?> fieldValueContainer = getBean(fieldBeanType, newInstancesCache);
                    final Object fieldValue = fieldValueContainer.getBean();
                    newInstancesCache.getNewBeans().putAll(fieldValueContainer.getNewBeans());
                    
                    if (fieldValue != null) {
                        if (!beanField.isAccessible()) { //TODO Java11 if (!beanField.canAccess(bean)) {
                            beanField.setAccessible(true);
                        }
                        
                        try {
                            beanField.set(bean, fieldValue);
                        } catch (final IllegalAccessException e) {
                            throw new BeanInstantiationException("Cannot set value to " + beanType.getName() + "." + beanField.getName());
                        }
                        
                    } else {
                        throw new BeanLookupException("There is no bean to inject into " + beanType.getName() + "." + beanField.getName());
                    }
                }
            }
            
            currentType = currentType.getSuperclass();
        }
    }

    /**
     * Gets the type of the bean to inject into the property represented by the field.
     * 
     * @param beanType The bean type that owns the field.
     * @param field The field.
     * @return The type for which to create a new field value; {@code null} when not an injectable field.
     */
    private static Class<?> getTypeToInject(final Class<?> beanType, final Field field) {
        if (field.isAnnotationPresent(Inject.class)) {
            //we need to do some extra work for generic fields.
            final Type genericType = field.getGenericType();
            
            if (genericType instanceof TypeVariable) {
                return ClassUtils.getTypeForGeneric(beanType, (TypeVariable<?>) genericType);
            } else {
                return field.getType();
            }
            
        } else {
            return null;
        }
        
    }
    
    /**
     * Calls the methods of the bean, which have the {@code @Init} annotation. Invokes top-down in the inheritance 
     * hierarchy - i.e. super-classes first. The methods must be public, non-static, and have no parameters.
     */
    private static <T> void initializeBean(final Class<T> beanType, final T bean) throws BeanInstantiationException {
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
        } catch (final IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            throw new BeanInstantiationException("Error calling initialization method.", e);
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
