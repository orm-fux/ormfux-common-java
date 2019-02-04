package org.ormfux.common.ioc;

import java.util.HashMap;
import java.util.Map;

/**
 * A cache for newly created bean instances.
 *
 * @param <B> The type of the requested "root" bean.
 */
class BeanCreationCache<B> {
    
    /**
     * The originally requested "root" bean.
     */
    private B bean;
    
    /**
     * All instances of singleton beans, which are created, because the
     * "root" bean (in-)directly depends on them.
     */
    private final Map<Class<?>, Object> newBeans = new HashMap<>();
    
    /**
     * Default constructor
     */
    public BeanCreationCache() {
    }
    
    /**
     * @param parentCache Cache with which to merge.
     */
    public BeanCreationCache(final BeanCreationCache<?> parentCache) {
        this.newBeans.putAll(parentCache.newBeans);
    }
    
    /**
     * The originally requested "root" bean.
     */
    public B getBean() {
        return bean;
    }
    
    /**
     * @see #getBean()
     */
    public void setBean(final B bean) {
        this.bean = bean;
    }
    
    /**
     * All cache contents.
     */
    public Map<Class<?>, Object> getNewBeans() {
        return newBeans;
    }
    
    /**
     * Adds a new bean to the cache.
     * 
     * @param beanType The type of the bean.
     * @param bean The bean.
     */
    public void putBeanInCache(final Class<?> beanType, final Object bean) {
        this.newBeans.put(beanType, bean);
    }
    
    /**
     * Gets the bean of the specified type from the cache.
     * 
     * @param beanType The bean type.
     * @return The bean; {@code null} when it does not exist.
     */
    public Object getCachedBean(final Class<?> beanType) {
        return newBeans.get(beanType);
    }
    
}