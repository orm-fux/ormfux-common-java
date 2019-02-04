package org.ormfux.common.utils.object.testequalsbuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;

import org.ormfux.common.utils.object.EqualsBuilder;
import org.ormfux.common.utils.object.Objects;

public abstract class AbstractEqualsBuilderTest {
    
    protected void checkBuilderState(final EqualsBuilder<?> builder, final String expectedState) {
        try {
            final Field stateField = EqualsBuilder.class.getDeclaredField("state");
            stateField.setAccessible(true);
            
            final Object state = stateField.get(builder);
            assertNotNull(state);
            assertEquals(expectedState, state.toString());
            
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException("Cannot read builder state.");
        }
    }
    
    public static class MockType1 {
        private MockType1 property;
        
        public MockType1 getProperty() {
            return property;
        }
        
        public void setProperty(MockType1 property) {
            this.property = property;
        }
        
    }
    
    public static class MockType2 extends MockType1 {
        
    }
    
    public static class MockType3 {
        
        private int property;
        
        public int getProperty() {
            return property;
        }
        
        public void setProperty(int property) {
            this.property = property;
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            } else if (Objects.isSame(this, obj)) {
                return true;
            } else if (!this.getClass().isAssignableFrom(obj.getClass())) {
                return false;
            } else {
                MockType3 other = (MockType3) obj;
                
                return property == other.property;
            }
            
        }
        
    }
}
