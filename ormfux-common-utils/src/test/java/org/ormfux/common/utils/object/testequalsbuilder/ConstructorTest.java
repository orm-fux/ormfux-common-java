package org.ormfux.common.utils.object.testequalsbuilder;

import org.junit.Test;
import org.ormfux.common.utils.object.EqualsBuilder;

public class ConstructorTest extends AbstractEqualsBuilderTest {
    
    @Test
    public void testInheritanceRelation() {
        MockType1 mock = new MockType1();
        MockType2 mock2 = new MockType2();
        
        EqualsBuilder<?> builder = new EqualsBuilder<>(mock, mock2);
        checkBuilderState(builder, "EQUAL");
        
        builder = new EqualsBuilder<>(mock, mock2, true);
        checkBuilderState(builder, "EQUAL");
        
        builder = new EqualsBuilder<>(mock, mock2, false);
        checkBuilderState(builder, "NOT_EQUAL");
        
        builder = new EqualsBuilder<>(mock2, mock);
        checkBuilderState(builder, "NEVER_EQUAL");
        
        builder = new EqualsBuilder<>(mock2, mock, true);
        checkBuilderState(builder, "NEVER_EQUAL");
        
        builder = new EqualsBuilder<>(mock2, mock, false);
        checkBuilderState(builder, "NEVER_EQUAL");
    }
    
    @Test
    public void testSameType() {
        MockType1 mock = new MockType1();
        MockType1 mock2 = new MockType1();
        
        EqualsBuilder<?> builder =  new EqualsBuilder<>(mock, mock2);
        checkBuilderState(builder, "EQUAL");
        
        builder =  new EqualsBuilder<>(mock, mock2, true);
        checkBuilderState(builder, "EQUAL");
        
        builder =  new EqualsBuilder<>(mock, mock2, false);
        checkBuilderState(builder, "NOT_EQUAL");
        
    }
    
    @Test
    public void testSameObject() {
        MockType1 mock = new MockType1();
        
        EqualsBuilder<?> builder = new EqualsBuilder<>(mock, mock);
        checkBuilderState(builder, "ALWAYS_EQUAL");
        
        builder = new EqualsBuilder<>(mock, mock, true);
        checkBuilderState(builder, "ALWAYS_EQUAL");
        
        builder = new EqualsBuilder<>(mock, mock, false);
        checkBuilderState(builder, "ALWAYS_EQUAL");
        
    }
    
    @Test
    public void testUnrelatedTypes() {
        MockType1 mock = new MockType1();
        MockType3 mock3 = new MockType3();
        
        EqualsBuilder<?> builder = new EqualsBuilder<>(mock, mock3);
        checkBuilderState(builder, "NEVER_EQUAL");
        
        builder = new EqualsBuilder<>(mock, mock3, true);
        checkBuilderState(builder, "NEVER_EQUAL");
        
        builder = new EqualsBuilder<>(mock, mock3, false);
        checkBuilderState(builder, "NEVER_EQUAL");
        
    }
    
    @Test
    public void testNullComparee() {
        MockType1 mock = new MockType1();
        
        EqualsBuilder<?> builder = new EqualsBuilder<>(mock, null);
        checkBuilderState(builder, "NEVER_EQUAL");
        
        builder = new EqualsBuilder<>(mock, null, true);
        checkBuilderState(builder, "NEVER_EQUAL");
        
        builder = new EqualsBuilder<>(mock, null, false);
        checkBuilderState(builder, "NEVER_EQUAL");
        
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullSourceValue() {
        new EqualsBuilder<>(null, new MockType1());
    }
    
}
