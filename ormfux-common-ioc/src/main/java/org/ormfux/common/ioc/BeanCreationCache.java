package org.ormfux.common.ioc;

import java.util.HashMap;
import java.util.Map;

/**
 * A cache for newly created bean instances.
 *
 * @param <B> The type of the requested "root" bean.
 */
class BeanCreationCache<B> {
    
    private BeanCreationCache<?> parentCache;
    
    /**
     * The originally requested "root" bean.
     */
    private B bean;
    
    private final boolean singleton;
    
    private final Map<Class<?>, Object> cache = new HashMap<>();
    
    public BeanCreationCache(final boolean singleton) {
        this(singleton, null);
    }
    
    public BeanCreationCache(final boolean singleton, final BeanCreationCache<?> parentCache) {
        this.singleton = singleton;
        this.parentCache = parentCache;
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
        
        if (singleton) {
            cache.put(bean.getClass(), bean);
        }
    }
    
    public void pullContentsIn(final BeanCreationCache<?> otherCache) {
        cache.putAll(otherCache.getCachedBeans());
    }
    
    /**
     * All cache contents.
     */
    public Map<Class<?>, Object> getCachedBeans() {
        return cache;
    }
    
    /**
     * Gets the bean of the specified type from the cache.
     * 
     * @param beanType The bean type.
     * @return The bean; {@code null} when it does not exist.
     */
    public Object getCachedBean(final Class<?> beanType) {
        final Object cachedBean = cache.get(beanType);
        
        if (cachedBean != null) {
            return cachedBean;
        } else if (parentCache != null) {
            return parentCache.getCachedBean(beanType);
        } else {
            return null;
        }
    }
    
}