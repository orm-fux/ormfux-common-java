package org.ormfux.common.utils.reflection.testclassutils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.ormfux.common.utils.reflection.ClassUtils;
import org.ormfux.common.utils.reflection.testclassutils.mock.ComplexEnum;
import org.ormfux.common.utils.reflection.testclassutils.mock.SimpleEnum;

public class GetClassTest {
    
    
    @Test
    public void testTopLevelClass() {
        assertEquals(GetClassTest.class, ClassUtils.getClass(this));
    }
    
    @Test
    public void testNestedClass() {
        assertEquals(NestedPrivateStaticClass.class, ClassUtils.getClass(new NestedPrivateStaticClass()));
        assertEquals(NestedProtectedStaticClass.class, ClassUtils.getClass(new NestedProtectedStaticClass()));
        assertEquals(NestedPackageStaticClass.class, ClassUtils.getClass(new NestedPackageStaticClass()));
        assertEquals(NestedPublicStaticClass.class, ClassUtils.getClass(new NestedPublicStaticClass()));
        
        assertEquals(NestedPrivateClass.class, ClassUtils.getClass(new NestedPrivateClass()));
        assertEquals(NestedProtectedClass.class, ClassUtils.getClass(new NestedProtectedClass()));
        assertEquals(NestedPackageClass.class, ClassUtils.getClass(new NestedPackageClass()));
        assertEquals(NestedPublicClass.class, ClassUtils.getClass(new NestedPublicClass()));
    }
    
    @Test
    public void testTopLevelSimpleEnum() {
        assertEquals(SimpleEnum.class, ClassUtils.getClass(SimpleEnum.VALUE1));
    }
    
    @Test
    public void testTopLevelComplexEnum() {
        assertEquals(ComplexEnum.class, ClassUtils.getClass(ComplexEnum.VALUE1));
    }
    
    @Test
    public void testNestedSimpleEnum() {
        assertEquals(NestedSimpleEnum.class, ClassUtils.getClass(NestedSimpleEnum.VALUE1));
    }
    
    @Test
    public void testNestedComplexEnum() {
        assertEquals(NestedComplexEnum.class, ClassUtils.getClass(NestedComplexEnum.VALUE1));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullArgument() {
        ClassUtils.getClass(null);
    }
    
    private class NestedPrivateStaticClass {}
    
    protected class NestedProtectedStaticClass {}
    
    class NestedPackageStaticClass {}
    
    public class NestedPublicStaticClass {}
    
    private class NestedPrivateClass {}
    
    protected class NestedProtectedClass {}
    
    class NestedPackageClass {}
    
    public class NestedPublicClass {}
    
    public static enum NestedSimpleEnum {
        
        VALUE1,
        
        VALUE2
        
    }
    
    public static enum NestedComplexEnum {
        
        VALUE1("value1") {
            
            @Override
            public void method() {
                System.out.println(property);
            }
        },
        
        VALUE2("value2") {
            
            @Override
            public void method() {
                System.out.println(property);
            }
        };
        
        protected final String property;
        
        
        private NestedComplexEnum(String property) {
            this.property = property;
        }
        
        public abstract void method();
        
    }    
}
