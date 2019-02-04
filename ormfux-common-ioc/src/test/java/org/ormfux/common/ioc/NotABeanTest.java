package org.ormfux.common.ioc;

import org.junit.Test;
import org.ormfux.common.ioc.exception.BeanLookupException;

public class NotABeanTest extends AbstractInjectionContextTest {
    
    @Test(expected = BeanLookupException.class)
    public void testNotABean() {
        InjectionContext.getBean(NotABeanTest.class);
    }
    
}
