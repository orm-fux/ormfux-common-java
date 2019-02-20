package org.ormfux.common.ioc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.Test;
import org.ormfux.common.ioc.annotations.Bean;
import org.ormfux.common.ioc.annotations.BeanConstructor;

public class AnnotatedBeansTest extends AbstractInjectionContextTest {
    
    @Test
    public void testGetBean() {
        Bean1 bean = InjectionContext.getBean(Bean1.class);
        assertThat(bean).isNotNull();
        
        assertThat(bean.nonInjected).isNull();
        
        assertThat(bean.bean2).isNotNull();
        assertThat(bean.bean3).isNotNull();
        assertThat(bean.nonSingleton).isNotNull();
        
        assertThat(bean.bean2.nonSingleton).isNotNull();
        assertThat(bean.bean3.bean2).isNotNull();
        
        assertThat(bean.bean2).isSameAs(bean.bean3.bean2); //the same object
        assertThat(bean.nonSingleton).isNotSameAs(bean.bean2.nonSingleton); //not the same object
        
        Map<Class<?>, Object> beansCache = getBeansCache();
        assertThat(beansCache.size()).isEqualTo(3);
        assertThat(beansCache.get(Bean1.class)).isSameAs(bean);
        assertThat(beansCache.get(Bean2.class)).isSameAs(bean.bean2);
        assertThat(beansCache.get(Bean3.class)).isSameAs(bean.bean3);
    }
    
    @Bean
    public static class Bean1 {
        
        private Bean2 bean2;
        
        private Bean3 bean3;
        
        private NonSingletonBean nonSingleton;
        
        private Bean2 nonInjected;
        
        @BeanConstructor
        public Bean1(Bean2 bean2, Bean3 bean3, NonSingletonBean nonSingleton) {
            this.bean2 = bean2;
            this.bean3 = bean3;
            this.nonSingleton = nonSingleton;
        }
        
        public Bean1() {
            throw new UnsupportedOperationException();
        }
        
    }
    
    @Bean
    public static class Bean2 {
        
        private NonSingletonBean nonSingleton;
        
        @BeanConstructor
        public Bean2(NonSingletonBean nonSingleton) {
            this.nonSingleton = nonSingleton;
        }
        
        public Bean2() {
            throw new UnsupportedOperationException();
        }
        
    }
    
    @Bean
    public static class Bean3 {
        
        private Bean2 bean2;
        
        @BeanConstructor
        public Bean3(Bean2 bean2) {
            this.bean2 = bean2;
        }
        
        public Bean3() {
            throw new UnsupportedOperationException();
        }
        
    }
    
    @Bean(singleton = false)
    public static class NonSingletonBean {
        //default constructor will be called
    }
    
}
