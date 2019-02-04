package org.ormfux.common.utils.reflection.testmethodutils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.ormfux.common.utils.reflection.MethodUtils;

public class GetPublicMethodTest {
    
    @Test
    public void testMethodWithoutAnything() throws NoSuchMethodException, SecurityException {
        assertEquals(MethodProvider.class.getMethod("methodWithoutParams"), 
                     MethodUtils.getPublicMethod(MethodProvider.class, "methodWithoutParams", void.class));
        assertEquals(MethodProvider.class.getMethod("methodWithoutParams"), 
                     MethodUtils.getPublicMethod(InheritedMethod.class, "methodWithoutParams", void.class));
        assertEquals(OverriddenMethod.class.getMethod("methodWithoutParams"), 
                     MethodUtils.getPublicMethod(OverriddenMethod.class, "methodWithoutParams", void.class));
    }
    
    @Test
    public void testMethodWithReturn() throws NoSuchMethodException, SecurityException {
        assertEquals(MethodProvider.class.getMethod("methodWithReturn"), 
                     MethodUtils.getPublicMethod(MethodProvider.class, "methodWithReturn", String.class));
        assertEquals(MethodProvider.class.getMethod("methodWithReturn"), 
                     MethodUtils.getPublicMethod(InheritedMethod.class, "methodWithReturn", String.class));
        assertEquals(OverriddenMethod.class.getMethod("methodWithReturn"), 
                     MethodUtils.getPublicMethod(OverriddenMethod.class, "methodWithReturn", String.class));
    }
    
    @Test
    public void testMethodWithReturnAndParams() throws NoSuchMethodException, SecurityException {
        assertEquals(MethodProvider.class.getMethod("methodWithReturnAndParams", String.class, int.class), 
                     MethodUtils.getPublicMethod(MethodProvider.class, "methodWithReturnAndParams", String.class, String.class, int.class));
        assertEquals(MethodProvider.class.getMethod("methodWithReturnAndParams", String.class, int.class), 
                     MethodUtils.getPublicMethod(InheritedMethod.class, "methodWithReturnAndParams", String.class, String.class, int.class));
        assertEquals(OverriddenMethod.class.getMethod("methodWithReturnAndParams", String.class, int.class), 
                     MethodUtils.getPublicMethod(OverriddenMethod.class, "methodWithReturnAndParams", String.class, String.class, int.class));
    }
    
    @Test
    public void testMethodWithParams() throws NoSuchMethodException, SecurityException {
        assertEquals(MethodProvider.class.getMethod("methodWithParams", String.class, int.class), 
                     MethodUtils.getPublicMethod(MethodProvider.class, "methodWithParams", void.class, String.class, int.class));
        assertEquals(MethodProvider.class.getMethod("methodWithParams", String.class, int.class), 
                     MethodUtils.getPublicMethod(InheritedMethod.class, "methodWithParams", void.class, String.class, int.class));
        assertEquals(OverriddenMethod.class.getMethod("methodWithParams", String.class, int.class), 
                     MethodUtils.getPublicMethod(OverriddenMethod.class, "methodWithParams", void.class, String.class, int.class));
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testWrongParamTypes() throws NoSuchMethodException, SecurityException {
        assertEquals(MethodProvider.class.getMethod("methodWithParams", String.class, int.class), 
                     MethodUtils.getPublicMethod(MethodProvider.class, "methodWithParams", void.class, String.class, String.class));
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testWrongNumberOfParams() throws NoSuchMethodException, SecurityException {
        assertEquals(MethodProvider.class.getMethod("methodWithParams", String.class, int.class), 
                     MethodUtils.getPublicMethod(MethodProvider.class, "methodWithParams", void.class, String.class));
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testProtectedMethod() throws NoSuchMethodException {
         MethodUtils.getPublicMethod(OverriddenMethod.class, "protectedMethod", void.class);
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testPackageMethod() throws NoSuchMethodException {
         MethodUtils.getPublicMethod(OverriddenMethod.class, "packageMethod", void.class);
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testNonExistingMethod() throws NoSuchMethodException {
         MethodUtils.getPublicMethod(OverriddenMethod.class, "nonexisting", void.class);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullClass() throws NoSuchMethodException {
         MethodUtils.getPublicMethod(null, "packageMethod", void.class);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullMethodName() throws NoSuchMethodException {
         MethodUtils.getPublicMethod(OverriddenMethod.class, null, void.class);
    }
    
    @Test
    public void testNullReturnType() throws NoSuchMethodException, SecurityException {
        assertEquals(MethodProvider.class.getMethod("methodWithReturnAndParams", String.class, int.class), 
                     MethodUtils.getPublicMethod(MethodProvider.class, "methodWithReturnAndParams", null, String.class, int.class));
        assertEquals(MethodProvider.class.getMethod("methodWithReturnAndParams", String.class, int.class), 
                     MethodUtils.getPublicMethod(InheritedMethod.class, "methodWithReturnAndParams", null, String.class, int.class));
        assertEquals(OverriddenMethod.class.getMethod("methodWithReturnAndParams", String.class, int.class), 
                     MethodUtils.getPublicMethod(OverriddenMethod.class, "methodWithReturnAndParams", null, String.class, int.class));
        assertEquals(MethodProvider.class.getMethod("methodWithParams", String.class, int.class), 
                     MethodUtils.getPublicMethod(MethodProvider.class, "methodWithParams", null, String.class, int.class));
        assertEquals(MethodProvider.class.getMethod("methodWithParams", String.class, int.class), 
                     MethodUtils.getPublicMethod(InheritedMethod.class, "methodWithParams", null, String.class, int.class));
        assertEquals(OverriddenMethod.class.getMethod("methodWithParams", String.class, int.class), 
                     MethodUtils.getPublicMethod(OverriddenMethod.class, "methodWithParams", null, String.class, int.class));
    }
    
    public static class MethodProvider {
        
        public void methodWithoutParams() {
            
        }
        
        public String methodWithReturn() {
            return null;
        }
        
        public String methodWithReturnAndParams(final String param1, final int param2) {
            return null;
        }
        
        public void methodWithParams(final String param1, final int param2) {
        }
        
        protected void protectedMethod() {
            
        }
        
        @SuppressWarnings("unused")
        private void privateMethod() {
            
        }
        
        void packageMethod() {
            
        }
    }
    
    public static class InheritedMethod extends MethodProvider {
        
    }
    
    public static class OverriddenMethod extends MethodProvider {
        
        @Override
        public void methodWithoutParams() {
            
        }
        
        @Override
        public String methodWithReturn() {
            return null;
        }
        
        @Override
        public String methodWithReturnAndParams(final String param1, final int param2) {
            return null;
        }
        
        @Override
        public void methodWithParams(final String param1, final int param2) {
        }
        
        @Override
        protected void protectedMethod() {
            
        }
        
        @Override
        void packageMethod() {
            
        }
    }
    
}
