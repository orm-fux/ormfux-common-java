package org.ormfux.common.utils.reflection.testmethodutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.ormfux.common.utils.reflection.MethodUtils;

public class InvokePropertySetterPathTest {
    
    @Test
    public void testLegalPath() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        Root root = new Root();
        Step1 step1 = new Step1();
        root.setStep1(step1);
        Step2 step2 = new Step2();
        step1.setStep2(step2);
        Step3 step3 = new Step3();
        step2.setStep3(step3);
        assertEquals(42, MethodUtils.invokePropertySetterPath(root, "step1.step2.step3.property", "value", false));
        assertEquals("value", step3.getProperty());
        
        root = new Root();
        assertEquals(42, MethodUtils.invokePropertySetterPath(root, "step1.step2.step3.property", "value", true));
        assertNotNull(root.getStep1());
        assertNotNull(root.getStep1().getStep2());
        assertNotNull(root.getStep1().getStep2().getStep3());
        assertEquals("value", root.getStep1().getStep2().getStep3().getProperty());
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullIntermediateProperty() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, 
                                                      InvocationTargetException, InstantiationException {
        Root root = new Root();
        MethodUtils.invokePropertySetterPath(root, "step1.step2.step3.property", "value", false);
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testWithWrongValueType() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, 
                                                InvocationTargetException, InstantiationException {
        Root root = new Root();
        Step1 step1 = new Step1();
        root.setStep1(step1);
        Step2 step2 = new Step2();
        step1.setStep2(step2);
        Step3 step3 = new Step3();
        step2.setStep3(step3);
        MethodUtils.invokePropertySetterPath(root, "step1.step2.step3.property", new Step1(), false);
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testEmptyPath() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, 
                                       InvocationTargetException, InstantiationException {
        MethodUtils.invokePropertySetterPath(Root.class, "", "value", false);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullRootClass() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, 
                                           InvocationTargetException, InstantiationException {
        MethodUtils.invokePropertySetterPath(null, "", "value", false);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullPath() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, 
                                      InvocationTargetException, InstantiationException {
        MethodUtils.invokePropertySetterPath(Root.class, null, "value", false);
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testInvalidPath() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, 
                                         InvocationTargetException, InstantiationException {
        MethodUtils.invokePropertySetterPath(Root.class, "step1.nonexisting.step3", "value", false);
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
        
        private String property;
        
        public String getProperty() {
            return property;
        }
        
        public int setProperty(String property) {
            this.property = property;
            
            return 42;
        }
    }
}
