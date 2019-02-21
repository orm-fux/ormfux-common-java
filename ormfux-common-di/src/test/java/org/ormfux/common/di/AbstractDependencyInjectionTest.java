package org.ormfux.common.di;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.ormfux.common.di.BeanDescriptor;
import org.ormfux.common.di.ConfigValueContext;
import org.ormfux.common.di.InjectionContext;

public abstract class AbstractDependencyInjectionTest {
    
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
        
        Field configValuesField = ConfigValueContext.class.getDeclaredField("CONFIG_VALUE_SETS");
        configValuesField.setAccessible(true);
        Map<?, ?> configValues = (Map<?, ?>) configValuesField.get(null);
        assertNotNull(configValues);
        configValues.clear();
        
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
    protected Map<String, Properties> getConfigValueSets() {
        try {
            Field beansCacheField = ConfigValueContext.class.getDeclaredField("CONFIG_VALUE_SETS");
            beansCacheField.setAccessible(true);
            
            return (Map<String, Properties>) beansCacheField.get(null);
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
