package org.ormfux.common.di;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.ormfux.common.di.BeanDescriptor;
import org.ormfux.common.di.InjectionContext;
import org.ormfux.common.di.annotations.Bean;
import org.ormfux.common.di.annotations.BeanConstructor;
import org.ormfux.common.di.exception.BeanDefinitionException;

public class ManualBeansTest extends AbstractDependencyInjectionTest {
    
    @Test
    public void testAddBeanDefinition() {
        List<BeanDescriptor> beanDescriptors = getBeanDescriptors();
        assertThat(beanDescriptors.size()).isEqualTo(0);
        
        InjectionContext.addBeanDefinition(ManualBean.class, true);
        assertThat(beanDescriptors.size()).isEqualTo(1);
        assertThat(beanDescriptors.get(0).getBeanType()).isEqualTo(ManualBean.class);
        assertThat(beanDescriptors.get(0).isSingleton()).isTrue();
        
        beanDescriptors.clear();
        
        InjectionContext.addBeanDefinition(ManualBean.class, false);
        assertThat(beanDescriptors.size()).isEqualTo(1);
        assertThat(beanDescriptors.get(0).getBeanType()).isEqualTo(ManualBean.class);
        assertThat(beanDescriptors.get(0).isSingleton()).isFalse();
        
        beanDescriptors.clear();
        
        BeanDescriptor beanDescriptor = new BeanDescriptor(ManualBean.class, true);
        InjectionContext.addBeanDefinition(beanDescriptor);
        assertThat(beanDescriptors.size()).isEqualTo(1);
        assertThat(beanDescriptors.get(0)).isSameAs(beanDescriptor);
    }
    
    @Test
    public void testNoDuplicateBeanDescriptors() {
        List<BeanDescriptor> beanDescriptors = getBeanDescriptors();
        assertThat(beanDescriptors.size()).isEqualTo(0);
        
        InjectionContext.addBeanDefinition(ManualBean.class, true);
        assertThat(beanDescriptors.size()).isEqualTo(1);
        assertThat(beanDescriptors.get(0).getBeanType()).isEqualTo(ManualBean.class);
        assertThat(beanDescriptors.get(0).isSingleton()).isTrue();
        
        InjectionContext.addBeanDefinition(ManualBean.class, false);
        assertThat(beanDescriptors.size()).isEqualTo(1);
        assertThat(beanDescriptors.get(0).getBeanType()).isEqualTo(ManualBean.class);
        assertThat(beanDescriptors.get(0).isSingleton()).isTrue();
        
        BeanDescriptor beanDescriptor = new BeanDescriptor(ManualBean.class, true);
        InjectionContext.addBeanDefinition(beanDescriptor);
        assertThat(beanDescriptors.size()).isEqualTo(1);
        assertThat(beanDescriptors.get(0)).isNotSameAs(beanDescriptor);
        assertThat(beanDescriptors.get(0).getBeanType()).isEqualTo(ManualBean.class);
        assertThat(beanDescriptors.get(0).isSingleton()).isTrue();
    }
    
    @Test
    public void testNotAManualBean() {
        assertThatThrownBy(() -> InjectionContext.addBeanDefinition(Bean1.class, false))
                .isExactlyInstanceOf(BeanDefinitionException.class);
    }
    
    @Test
    public void testNotAManualBeanDescriptor() {
        assertThatThrownBy(() -> InjectionContext.addBeanDefinition(new BeanDescriptor(Bean1.class, false)))
                .isExactlyInstanceOf(BeanDefinitionException.class);
    }
    
    @Test
    public void testGetBeanWithManual() {
        InjectionContext.addBeanDefinition(ManualBean.class, true);
        
        Bean1 bean = InjectionContext.getBean(Bean1.class);
        assertThat(bean).isNotNull();
        
        assertThat(bean.nonInjected).isNull();;
        
        assertThat(bean.bean2).isNotNull();
        assertThat(bean.nonSingleton).isNotNull();
        
        Map<Class<?>, Object> beansCache = getBeansCache();
        assertThat(beansCache.size()).isEqualTo(2);
        assertThat(beansCache.get(Bean1.class)).isSameAs(bean);
        assertThat(beansCache.get(ManualBean.class)).isSameAs(bean.bean2);
    }
    
    @Test
    public void testManualBeanNotDefined() {
        assertThatThrownBy(() -> InjectionContext.getBean(Bean1.class))
                .isExactlyInstanceOf(BeanDefinitionException.class);
    }
    
    @Bean
    public static class Bean1 {
        
        private ManualBean bean2;
        
        private NonSingletonBean nonSingleton;
        
        private ManualBean nonInjected;
        
        @BeanConstructor
        public Bean1(ManualBean bean2, NonSingletonBean nonSingleton) {
            this.bean2 = bean2;
            this.nonSingleton = nonSingleton;
        }
    }
    
    public static class ManualBean {
        
    }
    
    @Bean(singleton = false)
    public static class NonSingletonBean {
        
        @BeanConstructor
        public NonSingletonBean() {
        }
        
    }
}
