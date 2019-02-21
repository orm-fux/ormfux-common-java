package org.ormfux.common.ioc;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ormfux.common.ioc.annotations.ConfigValue;

public class ConfigValueGetTest extends AbstractDependencyInjectionTest {
    
    @Before
    public void beforeTest() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        super.beforeTest();
        
        ConfigValueContext.addConfigValueSet("propertiesSet", "/config/configvalues2.properties");
    }
    
    @Test
    public void testFoundWithSet() {
        assertThat(ConfigValueContext.getConfigValue("propertiesSet", "stringValue", String.class))
                        .isExactlyInstanceOf(String.class)
                        .isEqualTo("value1");
        
        assertThat(ConfigValueContext.getConfigValue("propertiesSet", "classValue", Class.class))
                        .isExactlyInstanceOf(Class.class)
                        .isEqualTo(Assert.class);
        
        assertThat(ConfigValueContext.getConfigValue("propertiesSet", "numberValue", double.class))
                        .isExactlyInstanceOf(Double.class)
                        .isEqualTo(1.4d);
        
    }
    
    @Test
    public void testNotFoundWithSet() {
        assertThat(ConfigValueContext.getConfigValue("propertiesSet", "NONEXISTING", String.class)).isNull();
        assertThat(ConfigValueContext.getConfigValue("NONEXISTING", "stringValue", String.class)).isNull();
    }
    
    @Test
    public void testFoundWithoutSet() {
        assertThat(ConfigValueContext.getConfigValue("", "stringValue", String.class))
                        .isExactlyInstanceOf(String.class)
                        .isEqualTo("value1");
        
        assertThat(ConfigValueContext.getConfigValue("", "classValue", Class.class))
                        .isExactlyInstanceOf(Class.class)
                        .isEqualTo(Assert.class);
        
        assertThat(ConfigValueContext.getConfigValue("", "numberValue", double.class))
                        .isExactlyInstanceOf(Double.class)
                        .isEqualTo(1.4d);
        
        assertThat(ConfigValueContext.getConfigValue(null, "stringValue", String.class))
                        .isExactlyInstanceOf(String.class)
                        .isEqualTo("value1");
        
        assertThat(ConfigValueContext.getConfigValue(null, "classValue", Class.class))
                        .isExactlyInstanceOf(Class.class)
                        .isEqualTo(Assert.class);
        
        assertThat(ConfigValueContext.getConfigValue(null, "numberValue", double.class))
                        .isExactlyInstanceOf(Double.class)
                        .isEqualTo(1.4d);
        
    }
    
    @Test
    public void testNotFoundWithoutSet() {
        assertThat(ConfigValueContext.getConfigValue(null, "NONEXISTING", String.class)).isNull();
        assertThat(ConfigValueContext.getConfigValue("", "NONEXISTING", String.class)).isNull();
    }
    
    @Test
    public void testFoundInSystemProperties() {
        try {
            System.getProperties().put("sysprop", "syspropvalue");
            
            assertThat(ConfigValueContext.getConfigValue("", "sysprop", String.class))
                            .isExactlyInstanceOf(String.class)
                            .isEqualTo("syspropvalue");
            
        } finally {
            System.getProperties().remove("sysprop");
        }
        
        assertThat(ConfigValueContext.getConfigValue("", "user.dir", String.class))
                        .isExactlyInstanceOf(String.class)
                        .isEqualTo(System.getProperty("user.dir"));
    }
    
    @Test
    public void testSystemPropertyOverruled() {
        try {
            System.getProperties().put("stringValue", "syspropvalue");
            
            assertThat(ConfigValueContext.getConfigValue("", "stringValue", String.class))
                            .isExactlyInstanceOf(String.class)
                            .isEqualTo("value1");
            
        } finally {
            System.getProperties().remove("stringValue");
        }
    }
    
    @Test
    public void testEvaluateConfigValueAnnotation() {
        assertThat(ConfigValueContext.getConfigValue(initConfigValueAnnotation("stringValue", "", ""), String.class))
                .isExactlyInstanceOf(String.class)
                .isEqualTo("value1");
        
        assertThat(ConfigValueContext.getConfigValue(initConfigValueAnnotation("", "stringValue", ""), String.class))
                .isExactlyInstanceOf(String.class)
                .isEqualTo("value1");
        
        assertThat(ConfigValueContext.getConfigValue(initConfigValueAnnotation("stringValue", "", "propertiesSet"), String.class))
                .isExactlyInstanceOf(String.class)
                .isEqualTo("value1");
        
        assertThat(ConfigValueContext.getConfigValue(initConfigValueAnnotation("stringValue", "", "NONEXISTING"), String.class))
                .isNull();
        
        assertThat(ConfigValueContext.getConfigValue(initConfigValueAnnotation("NONEXISTING", "stringValue", ""), String.class))
                .isExactlyInstanceOf(String.class)
                .isEqualTo("value1");
    }
    
    private ConfigValue initConfigValueAnnotation(String value, String key, String set) {
        return new ConfigValue() {
            
            @Override
            public Class<? extends Annotation> annotationType() {
                return ConfigValue.class;
            }
            
            @Override
            public String value() {
                return value;
            }
            
            @Override
            public String set() {
                return set;
            }
            
            @Override
            public String key() {
                return key;
            }
        };
    }
}
