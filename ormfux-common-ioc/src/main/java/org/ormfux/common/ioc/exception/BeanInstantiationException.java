package org.ormfux.common.ioc.exception;

/**
 * An exception to be thrown when a bean cannot be instantiated and/or initialized.
 */
public class BeanInstantiationException extends org.ormfux.common.utils.reflection.exception.InstantiationException {

    /**
     * Serial version uid.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * @param message Message.
     */
    public BeanInstantiationException(final String message) {
        super(message);
    }
    
    /**
     * @param message Message.
     * @param cause Cause.
     */
    public BeanInstantiationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
