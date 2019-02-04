package org.ormfux.common.ioc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.ormfux.common.ioc.annotations.Bean;
import org.ormfux.common.ioc.annotations.Inject;
import org.ormfux.common.ioc.exception.BeanLookupException;
import org.ormfux.common.utils.object.Objects;

public class AntiContextPollutionTest extends AbstractInjectionContextTest {
    
    @Test
    public void testCleanContextOnError() {
        try {
            InjectionContext.getBean(Bean1.class);
            fail("Expecting BeanLookupException.");
        } catch (BeanLookupException e) {
            assertEquals(0, getBeansCache().size());
        }
    }
    
    @Test
    public void testUnalteredContextOnError() {
        Bean2 bean = InjectionContext.getBean(Bean2.class);
        
        try {
            InjectionContext.getBean(Bean1.class);
            fail("Expecting BeanLookupException.");
        } catch (BeanLookupException e) {
            assertEquals(1, getBeansCache().size());
            assertTrue(Objects.isSame(bean, getBeansCache().get(Bean2.class)));
        }
    }
    
    @Bean
    public static class Bean1 {
        
        @Inject
        private ManualBean manualBean;
    }
    
    @Bean
    public static class Bean2 {
        
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
