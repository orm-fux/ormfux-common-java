package org.ormfux.common.ioc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.ormfux.common.ioc.annotations.Bean;
import org.ormfux.common.ioc.annotations.Inject;

public class AnnotatedBeansTest extends AbstractInjectionContextTest {
    
    @Test
    public void testGetBean() {
        Bean1 bean = InjectionContext.getBean(Bean1.class);
        assertNotNull(bean);
        
        assertNull(bean.nonInjected);
        
        assertNotNull(bean.selfReference);
        assertTrue(bean == bean.selfReference); //same object
        
        assertNotNull(bean.bean2);
        assertNotNull(bean.nonSingleton);
        
        assertTrue(bean == bean.bean2.circularReference); //same object
        assertNotNull(bean.bean2.nonSingleton);
        
        assertTrue(bean.nonSingleton != bean.bean2.nonSingleton); //not the same object
        
        assertNotNull(bean.nonSingleton.bean1);
        assertTrue(bean == bean.nonSingleton.bean1);
        
        assertNotNull(bean.bean2.nonSingleton.bean1);
        assertTrue(bean == bean.bean2.nonSingleton.bean1);
        
        Map<Class<?>, Object> beansCache = getBeansCache();
        assertEquals(2, beansCache.size());
        assertTrue(beansCache.get(Bean1.class) == bean);
        assertTrue(beansCache.get(Bean2.class) == bean.bean2);
    }
    
    @Bean
    public static class Bean1 {
        
        @Inject
        private Bean1 selfReference;
        
        @Inject
        private Bean2 bean2;
        
        @Inject
        private NonSingletonBean nonSingleton;
        
        private Bean2 nonInjected;
    }
    
    @Bean
    public static class Bean2 {
        
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
