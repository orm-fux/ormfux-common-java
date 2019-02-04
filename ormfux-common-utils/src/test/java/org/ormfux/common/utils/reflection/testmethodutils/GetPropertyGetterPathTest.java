package org.ormfux.common.utils.reflection.testmethodutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;
import org.ormfux.common.utils.reflection.MethodUtils;

public class GetPropertyGetterPathTest {
    
    @Test
    public void testLegalPath() throws NoSuchMethodException {
        List<Method> getterPath = MethodUtils.getPropertyGetterPath(Root.class, "step1.step2.step3", Step3.class);
        assertNotNull(getterPath);
        assertEquals(3, getterPath.size());
        assertEquals(Root.class.getMethod("getStep1"), getterPath.get(0));
        assertEquals(Step1.class.getMethod("getStep2"), getterPath.get(1));
        assertEquals(Step2.class.getMethod("getStep3"), getterPath.get(2));
        
        getterPath = MethodUtils.getPropertyGetterPath(Root.class, "step1.step2", Step2.class);
        assertNotNull(getterPath);
        assertEquals(2, getterPath.size());
        assertEquals(Root.class.getMethod("getStep1"), getterPath.get(0));
        assertEquals(Step1.class.getMethod("getStep2"), getterPath.get(1));
        
        getterPath = MethodUtils.getPropertyGetterPath(Root.class, "step1", Step1.class);
        assertNotNull(getterPath);
        assertEquals(1, getterPath.size());
        assertEquals(Root.class.getMethod("getStep1"), getterPath.get(0));
        
    }
    
    @Test
    public void testNullFinalType() throws NoSuchMethodException {
        List<Method> getterPath = MethodUtils.getPropertyGetterPath(Root.class, "step1.step2.step3", null);
        assertNotNull(getterPath);
        assertEquals(3, getterPath.size());
        assertEquals(Root.class.getMethod("getStep1"), getterPath.get(0));
        assertEquals(Step1.class.getMethod("getStep2"), getterPath.get(1));
        assertEquals(Step2.class.getMethod("getStep3"), getterPath.get(2));
        
        getterPath = MethodUtils.getPropertyGetterPath(Root.class, "step1.step2", null);
        assertNotNull(getterPath);
        assertEquals(2, getterPath.size());
        assertEquals(Root.class.getMethod("getStep1"), getterPath.get(0));
        assertEquals(Step1.class.getMethod("getStep2"), getterPath.get(1));
        
        getterPath = MethodUtils.getPropertyGetterPath(Root.class, "step1", null);
        assertNotNull(getterPath);
        assertEquals(1, getterPath.size());
        assertEquals(Root.class.getMethod("getStep1"), getterPath.get(0));
        
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testEmptyPath() throws NoSuchMethodException {
        MethodUtils.getPropertyGetterPath(Root.class, "", null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullRootClass() throws NoSuchMethodException {
        MethodUtils.getPropertyGetterPath(null, "", null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullPath() throws NoSuchMethodException {
        MethodUtils.getPropertyGetterPath(Root.class, null, null);
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testInvalidPath() throws NoSuchMethodException {
        MethodUtils.getPropertyGetterPath(Root.class, "step1.nonexisting.step3", null);
    }
    
    public static class Root {
        public Step1 getStep1() {
            return null;
        }
    }
    
    public static class Step1 {
        public Step2 getStep2() {
            return null;
        }
    }
    
    public static class Step2 {
        public Step3 getStep3() {
            return null;
        }
    }
    
    public static class Step3 {
        
    }
}
