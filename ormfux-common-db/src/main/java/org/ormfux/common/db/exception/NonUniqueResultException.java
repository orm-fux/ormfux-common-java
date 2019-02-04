package org.ormfux.common.db.exception;

/**
 * Exception to be thrown when a query yields more than one result is expected and 
 * the query yields more than one.
 */
public class NonUniqueResultException extends SQLException {

    /**
     * Serial version uid.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * @param message Exception message.
     */
    public NonUniqueResultException(final String message) {
        super(message);
    }
    
    /**
     * @param message Exception message.
     * @param cause nested Exception. 
     */
    public NonUniqueResultException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
}
