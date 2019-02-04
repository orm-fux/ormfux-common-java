package org.ormfux.common.utils.object.testequalsbuilder;

import org.junit.Test;
import org.ormfux.common.utils.object.EqualsBuilder;

public class AppendTest extends AbstractEqualsBuilderTest {
    
    @Test
    public void testSameType() {
        MockType1 mock1 = new MockType1();
        MockType1 mock2 = new MockType1();
        
        EqualsBuilder<MockType1> builder = new EqualsBuilder<>(mock1, mock2);
        checkBuilderState(builder, "EQUAL");
        
        builder.append(MockType1::getProperty);
        checkBuilderState(builder, "EQUAL");
        
        mock1.setProperty(mock1);
        mock2.setProperty(mock1);
        builder.append(MockType1::getProperty);
        checkBuilderState(builder, "EQUAL");
        
        mock1.setProperty(mock1);
        mock2.setProperty(mock2);
        builder.append(MockType1::getProperty);
        checkBuilderState(builder, "NOT_EQUAL");
        
        mock1.setProperty(mock1);
        mock2.setProperty(mock1); //going back won't "heal" the builder.
        builder.append(MockType1::getProperty);
        checkBuilderState(builder, "NOT_EQUAL");
        
    }
    
    @Test
    public void testNullSafe() {
        MockType1 mock1 = new MockType1();
        MockType1 mock2 = new MockType1();
        
        EqualsBuilder<MockType1> builder = new EqualsBuilder<>(mock1, mock2);
        checkBuilderState(builder, "EQUAL");
        
        mock1.setProperty(mock1);
        mock2.setProperty(null);
        builder.append(MockType1::getProperty);
        checkBuilderState(builder, "NOT_EQUAL");
        
        builder = new EqualsBuilder<>(mock1, mock2);
        checkBuilderState(builder, "EQUAL");
        
        mock1.setProperty(null);
        mock2.setProperty(mock1);
        builder.append(MockType1::getProperty);
        checkBuilderState(builder, "NOT_EQUAL");
        
    }
    
    @Test
    public void testInheritanceRelation() {
        MockType1 mock1 = new MockType1();
        MockType2 mock2 = new MockType2();
        
        EqualsBuilder<MockType1> builder = new EqualsBuilder<>(mock1, mock2);
        checkBuilderState(builder, "EQUAL");
        
        builder.append(MockType1::getProperty);
        checkBuilderState(builder, "EQUAL");
        
        mock1.setProperty(mock1);
        mock2.setProperty(mock1);
        builder.append(MockType1::getProperty);
        checkBuilderState(builder, "EQUAL");
        
        mock1.setProperty(mock1);
        mock2.setProperty(mock2);
        builder.append(MockType1::getProperty);
        checkBuilderState(builder, "NOT_EQUAL");
        
    }
    
    @Test
    public void testUnrelatedTypes() {
        MockType1 mock1 = new MockType1();
        
        EqualsBuilder<MockType1> builder = new EqualsBuilder<>(mock1, new MockType3());
        checkBuilderState(builder, "NEVER_EQUAL");
        
        mock1.setProperty(mock1);
        builder.append(MockType1::getProperty);
        checkBuilderState(builder, "NEVER_EQUAL");
        
    }
    
    @Test
    public void testNonEqualIsFinal() {
        MockType1 mock1 = new MockType1();
        MockType1 mock2 = new MockType1();
        
        EqualsBuilder<MockType1> builder = new EqualsBuilder<>(mock1, mock2);
        checkBuilderState(builder, "EQUAL");
        
        mock1.setProperty(mock1);
        mock2.setProperty(mock1);
        builder.append(MockType1::getProperty);
        checkBuilderState(builder, "EQUAL");
        
        mock1.setProperty(mock1);
        mock2.setProperty(mock2);
        builder.append(MockType1::getProperty);
        checkBuilderState(builder, "NOT_EQUAL");
        
        mock1.setProperty(mock1);
        mock2.setProperty(mock1); //going back won't "heal" the builder.
        builder.append(MockType1::getProperty);
        checkBuilderState(builder, "NOT_EQUAL");
        
    }
    
}
