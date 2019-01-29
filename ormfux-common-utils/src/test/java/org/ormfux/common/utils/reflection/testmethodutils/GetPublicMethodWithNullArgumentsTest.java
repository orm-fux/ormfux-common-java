package org.ormfux.common.utils.reflection.testmethodutils;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.ormfux.common.utils.reflection.MethodUtils;

public class GetPublicMethodWithNullArgumentsTest {
    
    @Test
    public void testMethodWithoutAnything() throws NoSuchMethodException, SecurityException {
        assertEquals(MethodProvider.class.getMethod("methodWithoutParams"), 
                     MethodUtils.getPublicMethodWithNullArguments(MethodProvider.class, "methodWithoutParams", void.class, null, null));
        assertEquals(MethodProvider.class.getMethod("methodWithoutParams"), 
                     MethodUtils.getPublicMethodWithNullArguments(InheritedMethod.class, "methodWithoutParams", void.class, null, null));
        assertEquals(OverriddenMethod.class.getMethod("methodWithoutParams"), 
                     MethodUtils.getPublicMethodWithNullArguments(OverriddenMethod.class, "methodWithoutParams", void.class, null, null));
        assertEquals(MethodProvider.class.getMethod("methodWithoutParams"), 
                     MethodUtils.getPublicMethodWithNullArguments(MethodProvider.class, "methodWithoutParams", void.class, new Class[0], new int[0]));
        assertEquals(MethodProvider.class.getMethod("methodWithoutParams"), 
                     MethodUtils.getPublicMethodWithNullArguments(InheritedMethod.class, "methodWithoutParams", void.class, new Class[0], new int[0]));
        assertEquals(OverriddenMethod.class.getMethod("methodWithoutParams"), 
                     MethodUtils.getPublicMethodWithNullArguments(OverriddenMethod.class, "methodWithoutParams", void.class, new Class[0], new int[0]));
    }
    
    @Test
    public void testMethodWithReturn() throws NoSuchMethodException, SecurityException {
        assertEquals(MethodProvider.class.getMethod("methodWithReturn"), 
                     MethodUtils.getPublicMethodWithNullArguments(MethodProvider.class, "methodWithReturn", String.class, null, null));
        assertEquals(MethodProvider.class.getMethod("methodWithReturn"), 
                     MethodUtils.getPublicMethodWithNullArguments(InheritedMethod.class, "methodWithReturn", String.class, null, null));
        assertEquals(OverriddenMethod.class.getMethod("methodWithReturn"), 
                     MethodUtils.getPublicMethodWithNullArguments(OverriddenMethod.class, "methodWithReturn", String.class, null, null));
        assertEquals(MethodProvider.class.getMethod("methodWithReturn"), 
                     MethodUtils.getPublicMethodWithNullArguments(MethodProvider.class, "methodWithReturn", String.class, new Class[0], new int[0]));
        assertEquals(MethodProvider.class.getMethod("methodWithReturn"), 
                     MethodUtils.getPublicMethodWithNullArguments(InheritedMethod.class, "methodWithReturn", String.class, new Class[0], new int[0]));
        assertEquals(OverriddenMethod.class.getMethod("methodWithReturn"), 
                     MethodUtils.getPublicMethodWithNullArguments(OverriddenMethod.class, "methodWithReturn", String.class, new Class[0], new int[0]));
    }
    
    @Test
    public void testMethodWithReturnAndParams() throws NoSuchMethodException, SecurityException {
        
        assertEquals(MethodProvider.class.getMethod("methodWithReturnAndParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(MethodProvider.class, "methodWithReturnAndParams", String.class, new Class[] {String.class, int.class}, null));
        assertEquals(MethodProvider.class.getMethod("methodWithReturnAndParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(InheritedMethod.class, "methodWithReturnAndParams", String.class, new Class[] {String.class, int.class}, null));
        assertEquals(OverriddenMethod.class.getMethod("methodWithReturnAndParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(OverriddenMethod.class, "methodWithReturnAndParams", String.class, new Class[] {String.class, int.class}, null));
        assertEquals(MethodProvider.class.getMethod("methodWithReturnAndParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(MethodProvider.class, "methodWithReturnAndParams", String.class, null, new int[] {0,1}));
        assertEquals(MethodProvider.class.getMethod("methodWithReturnAndParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(InheritedMethod.class, "methodWithReturnAndParams", String.class, null, new int[] {0,1}));
        assertEquals(OverriddenMethod.class.getMethod("methodWithReturnAndParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(OverriddenMethod.class, "methodWithReturnAndParams", String.class, null, new int[] {0,1}));
        assertEquals(MethodProvider.class.getMethod("methodWithReturnAndParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(MethodProvider.class, "methodWithReturnAndParams", String.class, new Class[] {String.class}, new int[] {1}));
        assertEquals(MethodProvider.class.getMethod("methodWithReturnAndParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(InheritedMethod.class, "methodWithReturnAndParams", String.class, new Class[] {String.class}, new int[] {1}));
        assertEquals(OverriddenMethod.class.getMethod("methodWithReturnAndParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(OverriddenMethod.class, "methodWithReturnAndParams", String.class, new Class[] {String.class}, new int[] {1}));
    }
    
    @Test
    public void testMethodWithParams() throws NoSuchMethodException, SecurityException {
        assertEquals(MethodProvider.class.getMethod("methodWithParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(MethodProvider.class, "methodWithParams", void.class, new Class[] {String.class, int.class}, null));
        assertEquals(MethodProvider.class.getMethod("methodWithParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(InheritedMethod.class, "methodWithParams", void.class, new Class[] {String.class, int.class}, null));
        assertEquals(OverriddenMethod.class.getMethod("methodWithParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(OverriddenMethod.class, "methodWithParams", void.class, new Class[] {String.class, int.class}, null));
//Result is random
//        assertEquals(MethodProvider.class.getMethod("methodWithParams", String.class, int.class), 
//                     MethodUtils.getPublicMethodWithNullArguments(MethodProvider.class, "methodWithParams", void.class, null, new int[] {0,1}));
//        assertEquals(MethodProvider.class.getMethod("methodWithParams", String.class, int.class), 
//                     MethodUtils.getPublicMethodWithNullArguments(InheritedMethod.class, "methodWithParams", void.class, null, new int[] {0,1}));
//        assertEquals(OverriddenMethod.class.getMethod("methodWithParams", String.class, int.class), 
//                     MethodUtils.getPublicMethodWithNullArguments(OverriddenMethod.class, "methodWithParams", void.class, null, new int[] {0,1}));
        
        assertEquals(MethodProvider.class.getMethod("methodWithParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(MethodProvider.class, "methodWithParams", void.class, new Class[] {int.class}, new int[] {0}));
        assertEquals(MethodProvider.class.getMethod("methodWithParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(InheritedMethod.class, "methodWithParams", void.class, new Class[] {int.class}, new int[] {0}));
        assertEquals(OverriddenMethod.class.getMethod("methodWithParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(OverriddenMethod.class, "methodWithParams", void.class, new Class[] {int.class}, new int[] {0}));
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testWrongParamTypes() throws NoSuchMethodException, SecurityException {
        assertEquals(MethodProvider.class.getMethod("methodWithParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(MethodProvider.class, "methodWithParams", void.class, new Class[] {String.class, BigDecimal.class}, null));
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testWrongNumberOfParams() throws NoSuchMethodException, SecurityException {
        assertEquals(MethodProvider.class.getMethod("methodWithParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(MethodProvider.class, "methodWithParams", void.class, new Class[] {String.class}, null));
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testWrongNumberOfUnknownParams() throws NoSuchMethodException, SecurityException {
        assertEquals(MethodProvider.class.getMethod("methodWithParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(MethodProvider.class, "methodWithParams", void.class, null, new int[] {0,1,2}));
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testProtectedMethod() throws NoSuchMethodException {
         MethodUtils.getPublicMethodWithNullArguments(OverriddenMethod.class, "protectedMethod", void.class, null, null);
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testPackageMethod() throws NoSuchMethodException {
         MethodUtils.getPublicMethodWithNullArguments(OverriddenMethod.class, "packageMethod", void.class, null, null);
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void testNonExistingMethod() throws NoSuchMethodException {
         MethodUtils.getPublicMethodWithNullArguments(OverriddenMethod.class, "nonexisting", void.class, null, null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullClass() throws NoSuchMethodException {
         MethodUtils.getPublicMethodWithNullArguments(null, "packageMethod", void.class, null, null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullMethodName() throws NoSuchMethodException {
         MethodUtils.getPublicMethodWithNullArguments(OverriddenMethod.class, null, void.class, null, null);
    }
    
    @Test
    public void testNullReturnType() throws NoSuchMethodException, SecurityException {
        assertEquals(MethodProvider.class.getMethod("methodWithReturnAndParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(MethodProvider.class, "methodWithReturnAndParams", null, new Class[] {String.class, int.class}, null));
        assertEquals(MethodProvider.class.getMethod("methodWithReturnAndParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(InheritedMethod.class, "methodWithReturnAndParams", null, new Class[] {String.class, int.class}, null));
        assertEquals(OverriddenMethod.class.getMethod("methodWithReturnAndParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(OverriddenMethod.class, "methodWithReturnAndParams", null, new Class[] {String.class, int.class}, null));
        assertEquals(MethodProvider.class.getMethod("methodWithParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(MethodProvider.class, "methodWithParams", null, new Class[] {String.class, int.class}, null));
        assertEquals(MethodProvider.class.getMethod("methodWithParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(InheritedMethod.class, "methodWithParams", null, new Class[] {String.class, int.class}, null));
        assertEquals(OverriddenMethod.class.getMethod("methodWithParams", String.class, int.class), 
                     MethodUtils.getPublicMethodWithNullArguments(OverriddenMethod.class, "methodWithParams", null, new Class[] {String.class, int.class}, null));
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
        
        public void methodWithParams(final String param1, final String param2) {
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
