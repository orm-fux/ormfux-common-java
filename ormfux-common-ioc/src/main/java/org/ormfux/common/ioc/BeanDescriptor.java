package org.ormfux.common.ioc;

import org.ormfux.common.utils.object.EqualsBuilder;

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
        final EqualsBuilder<BeanDescriptor> builder = new EqualsBuilder<>(this, other);
        builder.append(BeanDescriptor::getBeanType);
        
        return builder.isEquals();
    }
}
