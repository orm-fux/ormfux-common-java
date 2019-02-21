package org.ormfux.common.di;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.ormfux.common.di.ConfigValueContext;
import org.ormfux.common.di.exception.ConfigValueLoadException;

public class ConfigValueSetRegistrationTest extends AbstractDependencyInjectionTest {
    
    @Before
    public void beforeTest() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        super.beforeTest();
        assertThat(getConfigValueSets()).isEmpty();
    }
    
    @Test
    public void testLoadPlainFromClasspath() {
        ConfigValueContext.addConfigValueSet("propertiesSet", "/config/configvalues1.properties");
        
        assertThat(getConfigValueSets()).hasSize(1);
        
        Map<String, String> expectedContent = new HashMap<>();
        expectedContent.put("key1", "value1");
        expectedContent.put("key2", "value2");
        
        checkConfigValueContent("propertiesSet", expectedContent);
        
    }
    
    @Test
    public void testLoadXmlFromClasspath() {
        ConfigValueContext.addConfigValueSet("xmlSet", "/config/configvalues2.xml");
        
        assertThat(getConfigValueSets()).hasSize(1);
        
        Map<String, String> expectedContent = new HashMap<>();
        expectedContent.put("xmlkey1", "xmlvalue1");
        expectedContent.put("xmlkey2", "xmlvalue2");
        
        checkConfigValueContent("xmlSet", expectedContent);
        
    }
    
    
    @Test
    public void testLoadPlainFromExternalPath() {
        ConfigValueContext.addExternalConfigValueSet("propertiesSet", getClass().getResource("/config/configvalues1.properties").getPath());
        
        assertThat(getConfigValueSets()).hasSize(1);
        
        Map<String, String> expectedContent = new HashMap<>();
        expectedContent.put("key1", "value1");
        expectedContent.put("key2", "value2");
        
        checkConfigValueContent("propertiesSet", expectedContent);
        
    }
    
    @Test
    public void testLoadXmlFromExternalPath() {
        ConfigValueContext.addExternalConfigValueSet("xmlSet", getClass().getResource("/config/configvalues2.xml").getPath());
        
        assertThat(getConfigValueSets()).hasSize(1);
        
        Map<String, String> expectedContent = new HashMap<>();
        expectedContent.put("xmlkey1", "xmlvalue1");
        expectedContent.put("xmlkey2", "xmlvalue2");
        
        checkConfigValueContent("xmlSet", expectedContent);
        
//        assertThat(getConfigValueSets().get("propertiesSet")).isNotNull()
//                                                             .hasSize(2)
//                                                             
//                                                             .anySatisfy((k,v) -> {
//                                                                 assertThat(k).isEqualTo("xmlkey1");
//                                                                 assertThat(v).isEqualTo("xmlvalue1");
//                                                             })
//                                                             
//                                                             .anySatisfy((k,v) -> {
//                                                                 assertThat(k).isEqualTo("xmlkey2");
//                                                                 assertThat(v).isEqualTo("xmlvalue2");
//                                                             });
//                                                             
        
    }
    
    private void checkConfigValueContent(String setName, Map<String, String> expectedContent) {
        assertThat(getConfigValueSets()).containsKey(setName);
        
        assertThat(getConfigValueSets().get(setName)).isNotNull()
                                                     .hasSize(expectedContent.size())
                                                     .containsAllEntriesOf(expectedContent);
    }
    
    @Test
    public void testLoadNonExisting() {
        assertThatThrownBy(() -> ConfigValueContext.addConfigValueSet("propertiesSet", "NONEXISTING"))
                    .isExactlyInstanceOf(ConfigValueLoadException.class);
        assertThatThrownBy(() -> ConfigValueContext.addExternalConfigValueSet("propertiesSet", "NONEXISTING"))
                    .isExactlyInstanceOf(ConfigValueLoadException.class);
    }
}
