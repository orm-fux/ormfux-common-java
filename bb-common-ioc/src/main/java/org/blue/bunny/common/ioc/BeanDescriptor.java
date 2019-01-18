package org.blue.bunny.common.ioc;

import java.util.Objects;

/**
 * Descriptor for beans, which are defined without annotations.
 */
public class BeanDescriptor {
    
    /**
     * The type of the bean.
     */
    private final Class<?> beanType;
    
    /**
     * If the bean is a singleton.
     */
    private final boolean singleton;
    
    /**
     * @param beanType The type of the bean.
     * @param singleton If the bean is a singleton.
     */
    public BeanDescriptor(final Class<?> beanType, final boolean singleton) {
        this.beanType = beanType;
        this.singleton = singleton;
    }
    
    
    /**
     * The type of the bean.
     */
    public Class<?> getBeanType() {
        return beanType;
    }
    
    /**
     * If the bean is a singleton.
     */
    public boolean isSingleton() {
        return singleton;
    }
    
    /**
     * Equals by bean type.
     */
    @Override
    public boolean equals(final Object other) {
        if (other == null || !(other instanceof BeanDescriptor)) {
            return false;
        }
        
        return Objects.equals(((BeanDescriptor) other).getBeanType(), this.beanType);
        
    }
}
