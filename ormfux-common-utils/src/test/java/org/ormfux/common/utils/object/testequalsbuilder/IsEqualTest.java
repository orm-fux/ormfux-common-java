package org.ormfux.common.utils.object.testequalsbuilder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.ormfux.common.utils.object.EqualsBuilder;

public class IsEqualTest extends AbstractEqualsBuilderTest {
    
    @Test
    public void testStateNotEqual() {
        MockType1 mock1 = new MockType1();
        MockType1 mock2 = new MockType1();
        
        EqualsBuilder<MockType1> builder = new EqualsBuilder<>(mock1, mock2);
        checkBuilderState(builder, "EQUAL");
        
        mock1.setProperty(mock1);
        mock2.setProperty(mock2);
        builder.append(MockType1::getProperty);
        checkBuilderState(builder, "NOT_EQUAL");
        assertFalse(builder.isEquals());
        
    }
    
    @Test
    public void testStateEqual() {
        MockType1 mock = new MockType1();
        
        EqualsBuilder<?> builder = new EqualsBuilder<>(mock, new MockType2());
        checkBuilderState(builder, "EQUAL");
        assertTrue(builder.isEquals());
    }
    
    @Test
    public void testStateAlwaysEqual() {
        MockType1 mock = new MockType1();
        
        EqualsBuilder<?> builder = new EqualsBuilder<>(mock, mock);
        checkBuilderState(builder, "ALWAYS_EQUAL");
        assertTrue(builder.isEquals());
    }
    
    @Test
    public void testNeverEqual() {
        MockType1 mock = new MockType1();
        
        EqualsBuilder<?> builder = new EqualsBuilder<>(mock, null);
        checkBuilderState(builder, "NEVER_EQUAL");
        assertFalse(builder.isEquals());
        
    }
    
}
