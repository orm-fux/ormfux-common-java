package org.blue.bunny.ledger.customlibs.db.exception;

/**
 * Exception to be thrown when a query yields more than one result is expected and 
 * the query yields more than one.
 */
public class NonUniqueResultException extends SQLException {

    /**
     * Serial version uid.
     */
    private static final long serialVersionUID = 1L;
    
    /** {@inheritDoc} */
    public NonUniqueResultException(final String message) {
        super(message);
    }
    
    /** {@inheritDoc} */
    public NonUniqueResultException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
}
