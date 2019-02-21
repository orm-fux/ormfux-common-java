package org.ormfux.common.di;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;
import org.ormfux.common.di.InjectionContext;
import org.ormfux.common.di.annotations.Bean;
import org.ormfux.common.di.annotations.BeanConstructor;
import org.ormfux.common.di.exception.BeanDefinitionException;

public class GenericPropertyInjectionTest extends AbstractDependencyInjectionTest {
    
    @Test
    public void testGenericInject() {
        GenericDeclaringBean bean = InjectionContext.getBean(GenericDeclaringBean.class);
        assertThat(bean).isNotNull();
        assertThat(bean.getGenericProperty()).isNotNull();
        assertThat(bean.getGenericProperty().getClass()).isEqualTo(InjectedBeanType.class);
        
        assertThatThrownBy(() -> InjectionContext.getBean(GenericBean.class)).isExactlyInstanceOf(BeanDefinitionException.class);
    }
    
    public static abstract class GenericPropertyHolder<T> {
        
        private T genericProperty;
        
        public GenericPropertyHolder(T genericProperty) {
            this.genericProperty = genericProperty;
        }
        
        public T getGenericProperty() {
            return genericProperty;
        }
    }
    
    @Bean
    public static class GenericDeclaringBean extends GenericPropertyHolder<InjectedBeanType> {
        
        @BeanConstructor
        public GenericDeclaringBean(InjectedBeanType genericProperty) {
            super(genericProperty);
        }
        
    }
    
    @Bean
    public static class GenericBean<T> extends GenericPropertyHolder<T> {
        
        @BeanConstructor
        public GenericBean(T genericProperty) {
            super(genericProperty);
        }
        
    }
    
    @Bean
    public static class InjectedBeanType {
        
        @BeanConstructor
        public InjectedBeanType() {
        }
        
    }
}
