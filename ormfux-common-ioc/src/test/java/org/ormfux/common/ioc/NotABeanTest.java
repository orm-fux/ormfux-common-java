package org.ormfux.common.ioc;

import org.junit.Test;

public class NotABeanTest extends AbstractInjectionContextTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testNotABean() {
        InjectionContext.getBean(NotABeanTest.class);
    }
    
}
