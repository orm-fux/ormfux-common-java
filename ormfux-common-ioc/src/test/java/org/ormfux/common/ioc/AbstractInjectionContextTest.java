package org.ormfux.common.ioc;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.junit.Before;

public abstract class AbstractInjectionContextTest {
    
    @Before
    public void beforeTest() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        Field beansCacheField = InjectionContext.class.getDeclaredField("BEANS");
        beansCacheField.setAccessible(true);
        Map<?, ?> beansCache = (Map<?, ?>) beansCacheField.get(null);
        assertNotNull(beansCache);
        beansCache.clear();
        
        Field beanDescriptorsField = InjectionContext.class.getDeclaredField("BEAN_DESCRIPTORS");
        beanDescriptorsField.setAccessible(true);
        List<?> beanDescriptors = (List<?>) beanDescriptorsField.get(null);
        assertNotNull(beanDescriptors);
        beanDescriptors.clear();
        
    }
    
    @SuppressWarnings("unchecked")
    protected Map<Class<?>, Object> getBeansCache() {
        try {
            Field beansCacheField = InjectionContext.class.getDeclaredField("BEANS");
            beansCacheField.setAccessible(true);
            
            return (Map<Class<?>, Object>) beansCacheField.get(null);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new RuntimeException("Cannot read beans cache.", e);
        }
    }
    
    @SuppressWarnings("unchecked")
    protected List<BeanDescriptor> getBeanDescriptors() {
        try {
            Field beansCacheField = InjectionContext.class.getDeclaredField("BEAN_DESCRIPTORS");
            beansCacheField.setAccessible(true);
            
            return (List<BeanDescriptor>) beansCacheField.get(null);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new RuntimeException("Cannot read beans cache.", e);
        }
    }
}
