package org.ormfux.common.di;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.ormfux.common.di.InjectionContext;
import org.ormfux.common.di.annotations.Bean;
import org.ormfux.common.di.annotations.BeanConstructor;
import org.ormfux.common.di.exception.BeanInstantiationException;

public class AntiInjectContextPollutionTest extends AbstractDependencyInjectionTest {
    
    @Test
    public void testCleanContextOnError() {
        Assertions.assertThatThrownBy(() -> InjectionContext.getBean(Bean1.class))
                  .isInstanceOf(BeanInstantiationException.class);
        
        assertThat(getBeansCache().size()).isEqualTo(0);
    }
    
    @Test
    public void testUnalteredContextOnError() {
        Bean2 bean = InjectionContext.getBean(Bean2.class);
        
        Assertions.assertThatThrownBy(() -> InjectionContext.getBean(Bean1.class))
                  .isInstanceOf(BeanInstantiationException.class);
        
        assertThat(getBeansCache().size()).isEqualTo(1);
        assertThat(getBeansCache().get(Bean2.class)).isSameAs(bean);
    }
    
    @Bean
    public static class Bean1 {
        
        @SuppressWarnings("unused")
        private Bean3 bean3;
        
        @BeanConstructor
        public Bean1(Bean3 bean3) {
            this.bean3 = bean3;
        }
    }
    
    @Bean
    public static class Bean2 {
        
    }
    
    @Bean
    public static class Bean3 {
        
        public Bean3() {
            throw new UnsupportedOperationException();
        }
        
    }
    
}
