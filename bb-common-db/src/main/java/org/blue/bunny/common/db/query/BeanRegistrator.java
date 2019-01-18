package org.blue.bunny.common.db.query;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Registrator to add a type as bean definition to "{@code org.blue.bunny.common.ioc.InjectionContext}".
 * Implemented in such a way that there will be no error when the injection framework is not available.
 * In such a case simply no bean is registered.
 * 
 * Added to encapsulate the registration, so that we can keep the injection dependency in
 * optional scope.
 */
final class BeanRegistrator {
    
    /**
     * The class representing the injection framework.
     */
    private static final String INJECTION_CONTEXT_TYPE = "org.blue.bunny.common.ioc.InjectionContext";
    
    /**
     * Name of the method with which to register a bean.
     */
    private static final String INJECTION_CONTEXT_ADD_DEFINITION_METHOD = "addBeanDefinition";
    
    /**
     * Registers the type as a bean. Does nothing, when the injection framework is not available.
     * 
     * @param beanType The type of the bean.
     * @param singleton If the bean should be a singleton.
     * @return {@code true} when the bean has been registered.
     */
    public static boolean registerAsBean(final Class<?> beanType, final boolean singleton) {
        if (isInjectionFrameworkAvailable()) {
            try {
                final Class<?> injectionContextType = Class.forName(INJECTION_CONTEXT_TYPE);
                
                try {
                    final Method beanRegisterMethod = injectionContextType.getMethod(INJECTION_CONTEXT_ADD_DEFINITION_METHOD, Class.class, boolean.class);
                    beanRegisterMethod.invoke(null, beanType, singleton);
                    
                    return true;
                    
                } catch (final NoSuchMethodException | SecurityException 
                                | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    throw new RuntimeException("Injection framework is available. But cannot register bean.", e);
                }
                
                
            } catch (final ClassNotFoundException e) {
                //Never should get here!
                throw new RuntimeException("The injection framework should be available.", e);
            }
        } else {
            return false;
        }
    }
    
    /**
     * Checks, if the injection framework is available on the class path.
     * 
     * @return {@code true} when available.
     */
    public static boolean isInjectionFrameworkAvailable() {
        try {
            Class.forName(INJECTION_CONTEXT_TYPE);
            
            return true;
        } catch (final ClassNotFoundException e) {
            return false;
        }
    }
    
}
