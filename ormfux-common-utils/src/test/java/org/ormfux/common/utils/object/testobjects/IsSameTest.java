package org.ormfux.common.utils.object.testobjects;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.ormfux.common.utils.object.Objects;

public class IsSameTest {
    
    @Test
    public void testSame() {
        Mock mock = new Mock();
        assertTrue(Objects.isSame(mock, mock));
        
        Mock4 mock2 = new Mock4();
        assertTrue(Objects.isSame(mock2, mock2));
    }
    
    @Test
    public void testNotSame() {
        assertFalse(Objects.isSame(new Mock(), new Mock()));
        assertFalse(Objects.isSame(new Mock(), new Mock2()));
        assertFalse(Objects.isSame(new Mock(), new Mock3()));
    }
    
    private static class Mock {
        
        @Override
        public boolean equals(Object obj) {
            return true;
        }
        
    }
    
    private static class Mock2 extends Mock {}
    
    private static class Mock3 {
        
        @Override
        public boolean equals(Object obj) {
            return true;
        }
        
    }
    
    private static class Mock4 {
        
        @Override
        public boolean equals(Object obj) {
            return false;
        }
        
    }
    
}
