package org.ormfux.common.ioc.exception;

/**
 * An exception to be thrown when a bean cannot be looked up.
 */
public class BeanLookupException extends RuntimeException {

    /**
     * Serial version uid.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * @param message Message.
     */
    public BeanLookupException(final String message) {
        super(message);
    }
    
    /**
     * @param message Message.
     * @param cause Cause.
     */
    public BeanLookupException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
