package org.ormfux.common.ioc.exception;

/**
 * An exception to be thrown when a bean is not properly defined.
 */
public class BeanDefinitionException extends RuntimeException {

    /**
     * Serial version uid.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * @param message Message.
     */
    public BeanDefinitionException(final String message) {
        super(message);
    }
    
    /**
     * @param message Message.
     * @param cause Cause.
     */
    public BeanDefinitionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
