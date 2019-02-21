package org.ormfux.common.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.ormfux.common.di.annotations.Bean;
import org.ormfux.common.di.annotations.BeanConstructor;
import org.ormfux.common.di.annotations.ConfigValue;
import org.ormfux.common.di.exception.BeanDefinitionException;
import org.ormfux.common.di.exception.BeanInstantiationException;
import org.ormfux.common.di.exception.ConfigValueLoadException;

/**
 * Simple dependency injection implementation: We don't want to constantly create new Objects
 * or pass Objects around. This can be used to create new Objects of classes annotated with 
 * {@link Bean}. 
 * <p/>
 * Bean instances are cached in this context. So, this context provides <i>singleton</i> 
 * instances when asked for the same type multiple times. Constructor values of the instances 
 * are automatically "injected" with instances of the respective constructor parameters's 
 * type (As long as the type is also annotated with {@link Bean} or is a manually registered 
 * bean type.) The constructor annotated with {@link BeanConstructor} is the one used for 
 * instance creation for annotated beans; the no-argument constructor for "manual" beans. 
 * <p/>
 * It is also possible to create non-singleton beans. Non-singletons are not stored in the context. 
 * Each time such a bean has to be injected a new instance is created. So beware of circular 
 * references of non-singletons!
 * <p/>
 * <i>Why is this implemented?</i>
 * <ul>
 *  <li>See above.</li>
 *  <li>A library like Spring would be overkill for smaller applications.</li>
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
    
    private InjectionContext() {
        throw new UnsupportedOperationException("The InjectionContext is static and not supposed to be instantiated.");
    }
    
    /**
     * Creates a new instance of the given type or gets the matching instance from this context's 
     * cache. The type has to be annotated with {@link Bean} or must be a manually registered one. 
     * 
     * @param beanType The bean type.
     * @return The instance.
     * 
     * @throws BeanDefinitionException
     * @throws BeanInstantiationException
     * @throws ConfigValueLoadException
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(final Class<T> beanType) throws BeanDefinitionException, 
                                                                BeanInstantiationException,
                                                                ConfigValueLoadException {
        final Object bean = BEANS.get(beanType);
        
        if (bean != null) {
            return (T) bean;
            
        } else {
            validateBeanDefinition(beanType, new ArrayList<>());
            
            final BeanCreationCache<T> newBeanContainer = getBean(beanType, new BeanCreationCache<>(false));
            BEANS.putAll(newBeanContainer.getCachedBeans());
            
            return newBeanContainer.getBean();
        }
        
    }
    
    /**
     * Validates that the class actually represents a bean. Includes validation of the values to
     * inject into new instances.
     * 
     * @param beanType The bean type to validate.
     * @param discoveredNewBeans The, during the validation process, already found types of beans,
     *                           for which a new instance needs to be created.
     * 
     * @throws BeanDefinitionException When the class or one of its (indirect) properties to inject
     *                             does not represent a valid bean definition. 
     * @throws ConfigValueLoadException
     */
    private static void validateBeanDefinition(final Class<?> beanType, 
                                               final List<Class<?>> discoveredNewBeans) throws BeanDefinitionException,
                                                                                               ConfigValueLoadException {
        if (!BEANS.containsKey(beanType)) {
            if (discoveredNewBeans.contains(beanType)) {
                throw new BeanDefinitionException("Circular bean definition discovered, involving: " + beanType);
            }
            
            if (!isBeanType(beanType)) {
                throw new BeanDefinitionException("Not a bean type: " + beanType);
            } 
            
            if (isAnnotatedBeanType(beanType)) {
                if (Arrays.stream(beanType.getConstructors())
                          .filter(constructor -> constructor.isAnnotationPresent(BeanConstructor.class))
                          .count() > 1) {
                    throw new BeanDefinitionException("More than one bean instantation constructor.");
                }
                
                discoveredNewBeans.add(beanType);
                
                for (final Parameter parameter : getBeanConstructor(beanType).getParameters()) {
                    if (parameter.isAnnotationPresent(ConfigValue.class)) {
                        if (parameter.getType().isPrimitive() && getConfigValue(parameter) == null) {
                            throw new BeanDefinitionException("No config value for primitive paramater.");
                        }
                        
                    } else {
                        validateBeanDefinition(parameter.getType(), new ArrayList<>(discoveredNewBeans));
                    }
                }
                
            } else if (isManualBeanType(beanType)) {
                if (Arrays.stream(beanType.getConstructors()).anyMatch(c -> c.isAnnotationPresent(BeanConstructor.class))) {
                    throw new BeanDefinitionException("No constructor is allowed to be marked.");
                }
            }
            
        }
    }
    
    /**
     * Returns a an existing bean from the caches or creates a new instance. A new instance is only created 
     * when the requested bean is not found in one of the caches. 
     * 
     * @param beanType The type of the bean to create.
     * @param newInstancesCache The cache with all newly created bean instances.
     * @return The bean instance along with the other newly created beans.
     * 
     * @throws BeanInstantiationException
     * @throws ConfigValueLoadException
     */
    private static <T> BeanCreationCache<T> getBean(final Class<T> beanType, 
                                                    final BeanCreationCache<?> newInstancesCache) throws BeanInstantiationException,
                                                                                                         ConfigValueLoadException {
        if (BEANS.containsKey(beanType) || newInstancesCache.getCachedBean(beanType) != null) {
            return getExistingBean(beanType, newInstancesCache);
            
        } else if (isAnnotatedBeanType(beanType)) {
            return createBean(beanType, beanType.getAnnotation(Bean.class).singleton(), newInstancesCache);
            
        } else if (isManualBeanType(beanType)) {
            final BeanDescriptor beanDescriptor = getBeanDescriptor(beanType);
            return createBean(beanType, beanDescriptor.isSingleton(), newInstancesCache);
            
        } else {
            //We should never get here!
            throw new BeanInstantiationException("Not a bean type: " + beanType);
        }
        
    }
    
    /**
     * Gets a bean for which the instance already exists in a cache.
     * 
     * @param beanType The bean type.
     * @param newInstancesCache The cache of instances not yet in the main cache.
     * @return The bean instance.
     */
    @SuppressWarnings("unchecked")
    private static <T> BeanCreationCache<T> getExistingBean(final Class<T> beanType, final BeanCreationCache<?> newInstancesCache) {
        final BeanCreationCache<T> existingInstanceContainer = new BeanCreationCache<>(false);
        
        if (BEANS.containsKey(beanType)) {
            existingInstanceContainer.setBean((T) BEANS.get(beanType));
        } else {
            existingInstanceContainer.setBean((T) newInstancesCache.getCachedBean(beanType));
        }
        
        return existingInstanceContainer;
    }
    
    /**
     * Creates a new instance for a bean.
     * 
     * @param beanType The type to instantiate.
     * @param singleton If the bean is a singleton.
     * @param parentInstancesCache The cache with all bean instances not yet in the main cache.
     * @return The created instance along other newly created instances required for the bean.
     * 
     * @throws BeanInstantiationException
     * @throws ConfigValueLoadException
     */
    private static <T> BeanCreationCache<T> createBean(final Class<T> beanType, 
                                                       final boolean singleton, 
                                                       final BeanCreationCache<?> parentInstancesCache) throws BeanInstantiationException,
                                                                                                               ConfigValueLoadException {
        final BeanCreationCache<T> newInstancesCache = new BeanCreationCache<>(singleton, parentInstancesCache);
        
        final Constructor<?> beanConstructor = getBeanConstructor(beanType);
        final List<Object> paramValues = new ArrayList<>();
        
        for (final Parameter parameter : beanConstructor.getParameters()) {
            if (parameter.isAnnotationPresent(ConfigValue.class)) {
                paramValues.add(getConfigValue(parameter));
                
            } else {
                final BeanCreationCache<?> paramInstanceCache = getBean(parameter.getType(), newInstancesCache);
                newInstancesCache.pullContentsIn(paramInstanceCache);
                paramValues.add(paramInstanceCache.getBean());
            }
        }
        
        try {
            @SuppressWarnings("unchecked")
            final T bean = (T) beanConstructor.newInstance(paramValues.toArray());
            newInstancesCache.setBean(bean);
            
            return newInstancesCache;
            
        } catch (InstantiationException | IllegalAccessException 
                    | IllegalArgumentException | InvocationTargetException e) {
            throw new BeanInstantiationException("Cannot create new bean instance.", e);
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
     * 
     * @throws BeanDefinitionException
     */
    public static void addBeanDefinition(final Class<?> beanType, final boolean singleton) throws BeanDefinitionException {
        addBeanDefinition(new BeanDescriptor(beanType, singleton));
    }
    
    /**
     * Adds a definition of a bean (without Bean-annotation) to the InjectionContext.
     * Use this, when you want to use something as a Bean, but cannot modify its source.
     * <p/>
     * Each type can only be registered once!
     *  
     * @param beanDescriptor The bean definition.
     * 
     * @throws BeanDefinitionException
     */
    public static void addBeanDefinition(final BeanDescriptor beanDescriptor) throws BeanDefinitionException {
        if (!BEAN_DESCRIPTORS.contains(beanDescriptor)) {
            if (isAnnotatedBeanType(beanDescriptor.getBeanType())) {
                throw new BeanDefinitionException("The type is already a bean. No need to add it manually: " + beanDescriptor.getBeanType());
            } else {
                BEAN_DESCRIPTORS.add(beanDescriptor);
            }
        }
    }
    
    /**
     * Checks, if the class represents a bean that can be instantiated with this context.
     * 
     * @param beanType The potential bean type.
     * @return {@code true} when a bean type.
     */
    private static boolean isBeanType(final Class<?> beanType) {
        return isAnnotatedBeanType(beanType) || isManualBeanType(beanType);
    }
    
    /**
     * Checks, if the provided type is type registered as a "manual bean".
     *  
     * @param beanType The bean type.
     * @return {@code true} when a manual bean.
     */
    private static boolean isManualBeanType(final Class<?> beanType) {
        return getBeanDescriptor(beanType) != null && getBeanConstructor(beanType) != null;
    }
    
    /**
     * Gets the descriptor for the bean type.
     * 
     * @param beanType The bean type.
     * @return the descriptor; {@code null} when there is none.
     */
    private static BeanDescriptor getBeanDescriptor(final Class<?> beanType) {
        for (final BeanDescriptor beanDescriptor : BEAN_DESCRIPTORS) {
            if (Objects.equals(beanType, beanDescriptor.getBeanType())) {
                return beanDescriptor;
            }
        }
        
        return null;
    }
    
    /**
     * Checks if the given type is a type which this context can handle. This is the case when 
     * it is annotated with {@link Bean}, not abstract, and has a properly annotated constructor.
     * 
     * @param beanType The type.
     * @return {@code true} when this context can handle the type.
     */
    private static boolean isAnnotatedBeanType(Class<?> beanType) {
        return beanType.isAnnotationPresent(Bean.class) 
                    && !Modifier.isAbstract(beanType.getModifiers())
                    && getBeanConstructor(beanType) != null;
    }
    
    /**
     * Gets the constructor with which to instantiate a bean of the given type.
     * 
     * @param beanType The bean type.
     * @return The constructor. For annotated beans this is the constructor with the 
     *         {@link BeanConstructor} annotation or the no-argument constructor (when
     *         no other constructor is defined). For manual beans this is the no-argument
     *         constructor.
     */
    private static Constructor<?> getBeanConstructor(final Class<?> beanType) {
        if (beanType.isAnnotationPresent(Bean.class)) {
            final Constructor<?>[] constructors = beanType.getConstructors();
            
            if (constructors.length == 1 && constructors[0].getParameterCount() == 0) {
                return constructors[0];
            } else {
                return Arrays.stream(constructors)
                             .filter(c -> c.isAnnotationPresent(BeanConstructor.class))
                             .findFirst()
                             .orElse(null);
            }
            
        } else {
            try {
                return beanType.getConstructor();
            } catch (final NoSuchMethodException e) {
                return null;
            }
        }
    }
    
    /**
     * Adds a new set of configuration values to the context. The configuration values
     * are loaded from a file that is not on the application's class path.
     * 
     * @param name The identifying name of the value set.
     * @param path The full path to the config value file.
     * 
     * @throws ConfigValueLoadException
     */
    public static void addExternalConfigValueSet(final String name, final String path) throws ConfigValueLoadException {
        ConfigValueContext.addExternalConfigValueSet(name, path);
    }
    
    /**
     * Adds a new set of configuration values to the context. The configuration values
     * are loaded from a file that is on the application's class path.
     * 
     * @param name The identifying name of the value set.
     * @param path The path to the config value file.
     * 
     * @throws ConfigValueLoadException
     */
    public static void addConfigValueSet(final String name, final String path) throws ConfigValueLoadException {
        ConfigValueContext.addConfigValueSet(name, path);
    }
    
    /**
     * Gets the value of the parameter from the {@link ConfigValueContext}.
     * 
     * @param parameter The parameter.
     * @return The config value.
     */
    private static Object getConfigValue(final Parameter parameter) throws ConfigValueLoadException {
        return ConfigValueContext.getConfigValue(parameter.getAnnotation(ConfigValue.class), parameter.getType());
    }
    
}
