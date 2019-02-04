package org.ormfux.common.utils.reflection.testmethodutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;
import org.ormfux.common.utils.reflection.MethodUtils;

public class GetPropertySetterPathTest {
    
    @Test
    public void testLegalPath() throws NoSuchMethodException {
        List<Method> setterPath = MethodUtils.getPropertySetterPath(Root.class, "step1.step2.step3", Step3.class);
        assertNotNull(setterPath);
        assertEquals(3, setterPath.size());
        assertEquals(Root.class.getMethod("getStep1"), setterPath.get(0));
        assertEquals(Step1.class.getMethod("getStep2"), setterPath.get(1));
        assertEquals(Step2.class.getMethod("setStep3", Step3.class), setterPath.get(2));
    }
    
    @Test
    public void testNullFinalType() throws NoSuchMethodException {
        List<Method> setterPath = MethodUtils.getPropertySetterPath(Root.class, "step1.step2.step3", null);
        assertNotNull(setterPath);
        assertEquals(3, setterPath.size());
        assertEquals(Root.class.getMethod("getStep1"), setterPath.get(0));
        assertEquals(Step1.class.getMethod("getStep2"), setterPath.get(1));
        assertEquals(Step2.class.getMethod("setStep3", Step3.class), setterPath.get(2));
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testEmptyPath() throws NoSuchMethodException {
        MethodUtils.getPropertySetterPath(Root.class, "", null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullRootClass() throws NoSuchMethodException {
        MethodUtils.getPropertySetterPath(null, "", null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullPath() throws NoSuchMethodException {
        MethodUtils.getPropertySetterPath(Root.class, null, null);
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testInvalidPath() throws NoSuchMethodException {
        MethodUtils.getPropertySetterPath(Root.class, "step1.nonexisting.step3", null);
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testNotASetter() throws NoSuchMethodException {
        MethodUtils.getPropertySetterPath(Root.class, "step1.step2.nosetter", null);
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
        public void setStep3(Step3 step3) {
        }
    }
    
    public static class Step3 {
        
    }
}
