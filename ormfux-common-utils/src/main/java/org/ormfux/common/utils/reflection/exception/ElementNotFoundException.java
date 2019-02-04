package org.ormfux.common.utils.reflection.exception;

/**
 * An exception to be thrown when some reflective element is not found.
 */
public class ElementNotFoundException extends RuntimeException {

    /**
     * Serial version uid.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * @param message Message.
     */
    public ElementNotFoundException(final String message) {
        super(message);
    }
    
    /**
     * @param message Message.
     * @param cause Cause.
     */
    public ElementNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
