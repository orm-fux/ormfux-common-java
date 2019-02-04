package org.ormfux.common.utils.reflection.testmethodutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;
import org.ormfux.common.utils.reflection.MethodUtils;

public class GetPathToMethodTest {
    
    @Test
    public void testLegalPath() throws NoSuchMethodException {
        List<Method> methodPath = MethodUtils.getPathToMethod(Root.class, "step1.step2.doSomething", void.class, Step3.class, String.class);
        assertNotNull(methodPath);
        assertEquals(3, methodPath.size());
        assertEquals(Root.class.getMethod("getStep1"), methodPath.get(0));
        assertEquals(Step1.class.getMethod("getStep2"), methodPath.get(1));
        assertEquals(Step2.class.getMethod("doSomething", Step3.class, String.class), methodPath.get(2));
        
        methodPath = MethodUtils.getPathToMethod(Root.class, "step1.getStep2", Step2.class);
        assertNotNull(methodPath);
        assertEquals(2, methodPath.size());
        assertEquals(Root.class.getMethod("getStep1"), methodPath.get(0));
        assertEquals(Step1.class.getMethod("getStep2"), methodPath.get(1));
    }
    
    @Test
    public void testNonLimitedReturnType() throws NoSuchMethodException {
        List<Method> methodPath = MethodUtils.getPathToMethod(Root.class, "step1.step2.doSomething", null, Step3.class, String.class);
        assertNotNull(methodPath);
        assertEquals(3, methodPath.size());
        assertEquals(Root.class.getMethod("getStep1"), methodPath.get(0));
        assertEquals(Step1.class.getMethod("getStep2"), methodPath.get(1));
        assertEquals(Step2.class.getMethod("doSomething", Step3.class, String.class), methodPath.get(2));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPath() throws NoSuchMethodException {
        MethodUtils.getPathToMethod(Root.class, "", null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullRootClass() throws NoSuchMethodException {
        MethodUtils.getPathToMethod(null, "path", null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullPath() throws NoSuchMethodException {
        MethodUtils.getPathToMethod(Root.class, null, null);
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testInvalidPath() throws NoSuchMethodException {
        MethodUtils.getPathToMethod(Root.class, "step1.nonexisting.step3", null);
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testNoFinalMethodMatch() throws NoSuchMethodException {
        MethodUtils.getPathToMethod(Root.class, "step1.step2.nosetter", null);
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testFinalMethodParamsNoMatch() throws NoSuchMethodException {
        MethodUtils.getPathToMethod(Root.class, "step1.step2.doSomething", void.class, String.class, String.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPathStart() throws NoSuchMethodException {
        MethodUtils.getPathToMethod(Root.class, ".step1.step2.doSomething", void.class, String.class, String.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPathEnd() throws NoSuchMethodException {
        MethodUtils.getPathToMethod(Root.class, "step1.step2.doSomething.", void.class, String.class, String.class);
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
        public void doSomething(Step3 step3, String string) {
        }
    }
    
    public static class Step3 {
        
    }
}
