package org.blue.bunny.ledger.customlibs.db.exception;

/**
 * Exception to be thrown when a parameter is passed multipe times (same name)
 * to a query.
 */
public class DuplicateParamException extends SQLException {

    /**
     * Serial version uid.
     */
    private static final long serialVersionUID = 1L;
    
    /** {@inheritDoc} */
    public DuplicateParamException(final String message) {
        super(message);
    }
    
    /** {@inheritDoc} */
    public DuplicateParamException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
}
