package org.ormfux.common.utils.reflection.testmethodutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;
import org.ormfux.common.utils.reflection.MethodUtils;

public class GetPathToMethodWithNullArgumentsTest {
    
    @Test
    public void testLegalPath() throws NoSuchMethodException {
        List<Method> methodPath = MethodUtils.getPathToMethodWithNullArguments(Root.class, "step1.step2.doSomething", void.class, new Class[] {Step3.class, String.class}, null);
        assertNotNull(methodPath);
        assertEquals(3, methodPath.size());
        assertEquals(Root.class.getMethod("getStep1"), methodPath.get(0));
        assertEquals(Step1.class.getMethod("getStep2"), methodPath.get(1));
        assertEquals(Step2.class.getMethod("doSomething", Step3.class, String.class), methodPath.get(2));
        
        methodPath = MethodUtils.getPathToMethodWithNullArguments(Root.class, "step1.step2.doSomething", void.class, new Class[] {Step3.class, String.class}, new int[0]);
        assertNotNull(methodPath);
        assertEquals(3, methodPath.size());
        assertEquals(Root.class.getMethod("getStep1"), methodPath.get(0));
        assertEquals(Step1.class.getMethod("getStep2"), methodPath.get(1));
        assertEquals(Step2.class.getMethod("doSomething", Step3.class, String.class), methodPath.get(2));
        
        methodPath = MethodUtils.getPathToMethodWithNullArguments(Root.class, "step1.step2.doSomething", void.class, new Class[0], new int[] {0,1});
        assertNotNull(methodPath);
        assertEquals(3, methodPath.size());
        assertEquals(Root.class.getMethod("getStep1"), methodPath.get(0));
        assertEquals(Step1.class.getMethod("getStep2"), methodPath.get(1));
        assertEquals(Step2.class.getMethod("doSomething", Step3.class, String.class), methodPath.get(2));
        
        methodPath = MethodUtils.getPathToMethodWithNullArguments(Root.class, "step1.step2.doSomething", void.class, null, new int[] {0,1});
        assertNotNull(methodPath);
        assertEquals(3, methodPath.size());
        assertEquals(Root.class.getMethod("getStep1"), methodPath.get(0));
        assertEquals(Step1.class.getMethod("getStep2"), methodPath.get(1));
        assertEquals(Step2.class.getMethod("doSomething", Step3.class, String.class), methodPath.get(2));
        
        methodPath = MethodUtils.getPathToMethodWithNullArguments(Root.class, "step1.getStep2", Step2.class, new Class[0], null);
        assertNotNull(methodPath);
        assertEquals(2, methodPath.size());
        assertEquals(Root.class.getMethod("getStep1"), methodPath.get(0));
        assertEquals(Step1.class.getMethod("getStep2"), methodPath.get(1));
    }
    
    @Test
    public void testNonLimitedReturnType() throws NoSuchMethodException {
        List<Method> methodPath = MethodUtils.getPathToMethodWithNullArguments(Root.class, "step1.step2.doSomething", null, new Class[] {Step3.class, String.class}, null);
        assertNotNull(methodPath);
        assertEquals(3, methodPath.size());
        assertEquals(Root.class.getMethod("getStep1"), methodPath.get(0));
        assertEquals(Step1.class.getMethod("getStep2"), methodPath.get(1));
        assertEquals(Step2.class.getMethod("doSomething", Step3.class, String.class), methodPath.get(2));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPath() throws NoSuchMethodException {
        MethodUtils.getPathToMethodWithNullArguments(Root.class, "", null, null, null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullRootClass() throws NoSuchMethodException {
        MethodUtils.getPathToMethodWithNullArguments(null, "path", null, null, null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullPath() throws NoSuchMethodException {
        MethodUtils.getPathToMethodWithNullArguments(Root.class, null, null, null, null);
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testInvalidPath() throws NoSuchMethodException {
        MethodUtils.getPathToMethodWithNullArguments(Root.class, "step1.nonexisting.step3", null, new Class[0], new int[0]);
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testNoFinalMethodMatch() throws NoSuchMethodException {
        MethodUtils.getPathToMethodWithNullArguments(Root.class, "step1.step2.nosetter", null, new Class[0], new int[0]);
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testFinalMethodParamsNoMatch() throws NoSuchMethodException {
        MethodUtils.getPathToMethodWithNullArguments(Root.class, "step1.step2.doSomething", void.class, new Class[] {String.class, String.class}, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPathStart() throws NoSuchMethodException {
        MethodUtils.getPathToMethodWithNullArguments(Root.class, ".step1.step2.doSomething", void.class, new Class[] {String.class, String.class}, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPathEnd() throws NoSuchMethodException {
        MethodUtils.getPathToMethodWithNullArguments(Root.class, "step1.step2.doSomething.", void.class, new Class[] {String.class, String.class}, null);
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
