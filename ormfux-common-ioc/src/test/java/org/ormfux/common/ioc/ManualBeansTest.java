package org.ormfux.common.ioc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.ormfux.common.ioc.annotations.Bean;
import org.ormfux.common.ioc.annotations.Inject;
import org.ormfux.common.ioc.exception.BeanLookupException;
import org.ormfux.common.utils.object.Objects;

public class ManualBeansTest extends AbstractInjectionContextTest {
    
    @Test
    public void testAddBeanDefinition() {
        List<BeanDescriptor> beanDescriptors = getBeanDescriptors();
        assertEquals(0, beanDescriptors.size());
        
        InjectionContext.addBeanDefinition(ManualBean.class, true);
        assertEquals(1, beanDescriptors.size());
        assertEquals(ManualBean.class, beanDescriptors.get(0).getBeanType());
        assertTrue(beanDescriptors.get(0).isSingleton());
        
        beanDescriptors.clear();
        
        InjectionContext.addBeanDefinition(ManualBean.class, false);
        assertEquals(1, beanDescriptors.size());
        assertEquals(ManualBean.class, beanDescriptors.get(0).getBeanType());
        assertFalse(beanDescriptors.get(0).isSingleton());
        
        beanDescriptors.clear();
        
        BeanDescriptor beanDescriptor = new BeanDescriptor(ManualBean.class, true);
        InjectionContext.addBeanDefinition(beanDescriptor);
        assertEquals(1, beanDescriptors.size());
        assertTrue(Objects.isSame(beanDescriptor, beanDescriptors.get(0))); 
    }
    
    @Test
    public void testNoDuplicateBeanDescriptors() {
        List<BeanDescriptor> beanDescriptors = getBeanDescriptors();
        assertEquals(0, beanDescriptors.size());
        
        InjectionContext.addBeanDefinition(ManualBean.class, true);
        assertEquals(1, beanDescriptors.size());
        assertEquals(ManualBean.class, beanDescriptors.get(0).getBeanType());
        assertTrue(beanDescriptors.get(0).isSingleton());
        
        InjectionContext.addBeanDefinition(ManualBean.class, false);
        assertEquals(1, beanDescriptors.size());
        assertEquals(ManualBean.class, beanDescriptors.get(0).getBeanType());
        assertTrue(beanDescriptors.get(0).isSingleton());
        
        BeanDescriptor beanDescriptor = new BeanDescriptor(ManualBean.class, true);
        InjectionContext.addBeanDefinition(beanDescriptor);
        assertEquals(1, beanDescriptors.size());
        assertFalse(Objects.isSame(beanDescriptor, beanDescriptors.get(0))); 
        assertEquals(ManualBean.class, beanDescriptors.get(0).getBeanType());
        assertTrue(beanDescriptors.get(0).isSingleton());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNotAManualBean() {
        InjectionContext.addBeanDefinition(Bean1.class, false);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNotAManualBeanDescriptor() {
        InjectionContext.addBeanDefinition(new BeanDescriptor(Bean1.class, false));
    }
    
    @Test
    public void testGetBeanWithManual() {
        InjectionContext.addBeanDefinition(ManualBean.class, true);
        
        Bean1 bean = InjectionContext.getBean(Bean1.class);
        assertNotNull(bean);
        
        assertNull(bean.nonInjected);
        
        assertNotNull(bean.selfReference);
        assertTrue(Objects.isSame(bean, bean.selfReference)); //same object
        
        assertNotNull(bean.bean2);
        assertNotNull(bean.nonSingleton);
        
        assertTrue(Objects.isSame(bean, bean.bean2.circularReference)); //same object
        assertNotNull(bean.bean2.nonSingleton);
        
        assertTrue(bean.nonSingleton != bean.bean2.nonSingleton); //not the same object
        
        assertNotNull(bean.nonSingleton.bean1);
        assertTrue(Objects.isSame(bean, bean.nonSingleton.bean1));
        
        assertNotNull(bean.bean2.nonSingleton.bean1);
        assertTrue(Objects.isSame(bean, bean.bean2.nonSingleton.bean1));
        
        Map<Class<?>, Object> beansCache = getBeansCache();
        assertEquals(2, beansCache.size());
        assertTrue(Objects.isSame(beansCache.get(Bean1.class), bean));
        assertTrue(Objects.isSame(beansCache.get(ManualBean.class), bean.bean2));
    }
    
    @Test(expected = BeanLookupException.class)
    public void testManualBeanNotDefined() {
        InjectionContext.getBean(Bean1.class);
    }
    
    @Bean
    public static class Bean1 {
        
        @Inject
        private Bean1 selfReference;
        
        @Inject
        private ManualBean bean2;
        
        @Inject
        private NonSingletonBean nonSingleton;
        
        private ManualBean nonInjected;
    }
    
    public static class ManualBean {
        
        @Inject
        private Bean1 circularReference;
        
        @Inject
        private NonSingletonBean nonSingleton;
    }
    
    @Bean(singleton = false)
    public static class NonSingletonBean {
        
        @Inject
        private Bean1 bean1;
        
    }
}
