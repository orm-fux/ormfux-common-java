package org.ormfux.common.ioc;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;
import org.ormfux.common.ioc.annotations.Bean;
import org.ormfux.common.ioc.annotations.BeanConstructor;
import org.ormfux.common.ioc.exception.BeanDefinitionException;

public class BeanValidationTest extends AbstractInjectionContextTest {
    
    @Test
    public void testCircularBeanDefinition() {
        assertThatThrownBy(() -> InjectionContext.getBean(SelfReferencingBean.class)).isExactlyInstanceOf(BeanDefinitionException.class);
        assertThatThrownBy(() -> InjectionContext.getBean(CircularBean.class)).isExactlyInstanceOf(BeanDefinitionException.class);
        assertThatThrownBy(() -> InjectionContext.getBean(CircularIntermediateBean.class)).isExactlyInstanceOf(BeanDefinitionException.class);
    }
    
    @Test
    public void testNotABean() {
        assertThatThrownBy(() -> InjectionContext.getBean(BeanValidationTest.class)).isExactlyInstanceOf(BeanDefinitionException.class);
    }
    
    @Test
    public void testAbstractBean() {
        assertThatThrownBy(() -> InjectionContext.getBean(AbstractBean.class)).isExactlyInstanceOf(BeanDefinitionException.class);
    }
    
    @Test
    public void testMissingBeanConstructor() {
        assertThatThrownBy(() -> InjectionContext.getBean(MissingBeanConstructor.class)).isExactlyInstanceOf(BeanDefinitionException.class);
    }
    
    @Test
    public void testNonUniqueBeanConstructor() {
        assertThatThrownBy(() -> InjectionContext.getBean(NonUniqueBeanConstructor.class)).isExactlyInstanceOf(BeanDefinitionException.class);
    }
    
    @Test
    public void testNonBeanConstructorParam() {
        assertThatThrownBy(() -> InjectionContext.getBean(NonBeanConstructorParam.class)).isExactlyInstanceOf(BeanDefinitionException.class);
    }
    
    @Bean
    public static class SelfReferencingBean {
        
        @BeanConstructor
        public SelfReferencingBean(SelfReferencingBean b) {}
        
    }
    
    @Bean
    public static class CircularBean {
        
        @BeanConstructor
        public CircularBean(CircularIntermediateBean b) {}
        
    }
    
    @Bean
    public static class CircularIntermediateBean {
        
        @BeanConstructor
        public CircularIntermediateBean(CircularBean b) {}
    }
    
    @Bean
    public static abstract class AbstractBean {}
    
    @Bean
    public static class MissingBeanConstructor {
        
        public MissingBeanConstructor(Object o) {
        }
        
    }
    
    @Bean
    public static class NonUniqueBeanConstructor {
        
        @BeanConstructor
        public NonUniqueBeanConstructor(Object o) {
        }
        
        @BeanConstructor
        public NonUniqueBeanConstructor() {
        }
        
    }
    
    @Bean
    public static class NonBeanConstructorParam {
        
        @BeanConstructor
        public NonBeanConstructorParam(Object o) {
        }
        
    }
    
}
