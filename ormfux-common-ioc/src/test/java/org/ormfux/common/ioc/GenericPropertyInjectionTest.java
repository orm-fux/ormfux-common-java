package org.ormfux.common.ioc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.ormfux.common.ioc.annotations.Bean;
import org.ormfux.common.ioc.annotations.Inject;

public class GenericPropertyInjectionTest extends AbstractInjectionContextTest {
    
    @Test
    public void testGenericInject() {
        GenericDeclaringBean bean = InjectionContext.getBean(GenericDeclaringBean.class);
        assertNotNull(bean);
        assertNotNull(bean.getGenericProperty());
        assertEquals(InjectedBeanType.class, bean.getGenericProperty().getClass());
    }
    
    @Bean
    public static abstract class GenericPropertyHolder<T> {
        
        @Inject
        private T genericProperty;
        
        
        public T getGenericProperty() {
            return genericProperty;
        }
    }
    
    @Bean
    public static class GenericDeclaringBean extends GenericPropertyHolder<InjectedBeanType> {
        
    }
    
    @Bean
    public static class InjectedBeanType {
        
    }
}
