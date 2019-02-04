package org.ormfux.common.utils.reflection.testmethodutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.ormfux.common.utils.reflection.MethodUtils;

public class InvokePropertyGetterPathTest {
    
    @Test
    public void testLegalPath() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        Root root = new Root();
        assertNull(MethodUtils.invokePropertyGetterPath(root, "step1.step2.step3.property", false));
        Step1 step1 = new Step1();
        root.setStep1(step1);
        assertNull(MethodUtils.invokePropertyGetterPath(root, "step1.step2.step3.property", false));
        Step2 step2 = new Step2();
        step1.setStep2(step2);
        assertNull(MethodUtils.invokePropertyGetterPath(root, "step1.step2.step3.property", false));
        Step3 step3 = new Step3();
        step2.setStep3(step3);
        assertEquals(step3.getProperty(), MethodUtils.invokePropertyGetterPath(root, "step1.step2.step3.property", false));
        
        root = new Root();
        assertEquals(step3.getProperty(), MethodUtils.invokePropertyGetterPath(root, "step1.step2.step3.property", true));
        assertNotNull(root.getStep1());
        assertNotNull(root.getStep1().getStep2());
        assertNotNull(root.getStep1().getStep2().getStep3());
    }
    
    @Test
    public void testWithFinalType() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        Root root = new Root();
        assertNull(MethodUtils.invokePropertyGetterPath(root, "step1.step2.step3.property", String.class, false));
        Step1 step1 = new Step1();
        root.setStep1(step1);
        assertNull(MethodUtils.invokePropertyGetterPath(root, "step1.step2.step3.property", String.class, false));
        Step2 step2 = new Step2();
        step1.setStep2(step2);
        assertNull(MethodUtils.invokePropertyGetterPath(root, "step1.step2.step3.property", String.class, false));
        Step3 step3 = new Step3();
        step2.setStep3(step3);
        assertEquals(step3.getProperty(), MethodUtils.invokePropertyGetterPath(root, "step1.step2.step3.property", String.class, false));
        
        root = new Root();
        assertEquals(step3.getProperty(), MethodUtils.invokePropertyGetterPath(root, "step1.step2.step3.property", String.class, true));
        assertNotNull(root.getStep1());
        assertNotNull(root.getStep1().getStep2());
        assertNotNull(root.getStep1().getStep2().getStep3());
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testWithWrongFinalType() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, 
                                                InvocationTargetException, InstantiationException {
        Root root = new Root();
        Step1 step1 = new Step1();
        root.setStep1(step1);
        Step2 step2 = new Step2();
        step1.setStep2(step2);
        Step3 step3 = new Step3();
        step2.setStep3(step3);
        MethodUtils.invokePropertyGetterPath(root, "step1.step2.step3.property", Integer.class, false);
    }
    
    @Test
    public void testNullFinalType() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, 
                                           InvocationTargetException, InstantiationException {
        Root root = new Root();
        assertNull(MethodUtils.invokePropertyGetterPath(root, "step1.step2.step3.property", null, false));
        Step1 step1 = new Step1();
        root.setStep1(step1);
        assertNull(MethodUtils.invokePropertyGetterPath(root, "step1.step2.step3.property", null, false));
        Step2 step2 = new Step2();
        step1.setStep2(step2);
        assertNull(MethodUtils.invokePropertyGetterPath(root, "step1.step2.step3.property", null, false));
        Step3 step3 = new Step3();
        step2.setStep3(step3);
        assertEquals(step3.getProperty(), MethodUtils.invokePropertyGetterPath(root, "step1.step2.step3.property", null, false));
        
        root = new Root();
        assertEquals(step3.getProperty(), MethodUtils.invokePropertyGetterPath(root, "step1.step2.step3.property", null, true));
        assertNotNull(root.getStep1());
        assertNotNull(root.getStep1().getStep2());
        assertNotNull(root.getStep1().getStep2().getStep3());
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testEmptyPath() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, 
                                       InvocationTargetException, InstantiationException {
        MethodUtils.invokePropertyGetterPath(Root.class, "", false);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullRootClass() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, 
                                           InvocationTargetException, InstantiationException {
        MethodUtils.invokePropertyGetterPath(null, "", false);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullPath() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, 
                                      InvocationTargetException, InstantiationException {
        MethodUtils.invokePropertyGetterPath(Root.class, null, false);
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testInvalidPath() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, 
                                         InvocationTargetException, InstantiationException {
        MethodUtils.invokePropertyGetterPath(Root.class, "step1.nonexisting.step3", false);
    }
    
    public static class Root {
        private Step1 step1;
        
        public Step1 getStep1() {
            return step1;
        }
        
        public void setStep1(Step1 step1) {
            this.step1 = step1;
        }
    }
    
    public static class Step1 {
        private Step2 step2;
        
        public Step2 getStep2() {
            return step2;
        }
        
        public void setStep2(Step2 step2) {
            this.step2 = step2;
        }
    }
    
    public static class Step2 {
        private Step3 step3;
        
        public Step3 getStep3() {
            return step3;
        }
        
        public void setStep3(Step3 step3) {
            this.step3 = step3;
        }
    }
    
    public static class Step3 {
        
        public String getProperty() {
            return "value";
        }
    }
}
