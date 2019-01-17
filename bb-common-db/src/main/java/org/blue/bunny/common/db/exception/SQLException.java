package org.blue.bunny.common.db.exception;

/**
 * General exception to be thrown in case of SQL errors.
 */
public class SQLException extends RuntimeException {

    /**
     * Serial version uid.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * @param message Exception message.
     */
    public SQLException(final String message) {
        super(message);
    }
    
    /**
     * @param message Exception message.
     * @param cause nested Exception. 
     */
    public SQLException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
}
