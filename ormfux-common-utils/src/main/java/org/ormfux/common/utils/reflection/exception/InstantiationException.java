package org.ormfux.common.utils.reflection.exception;

/**
 * An exception to be thrown when some object creation fails.
 */
public class InstantiationException extends RuntimeException {

    /**
     * Serial version uid.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * @param message Message.
     */
    public InstantiationException(final String message) {
        super(message);
    }
    
    /**
     * @param message Message.
     * @param cause Cause.
     */
    public InstantiationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
