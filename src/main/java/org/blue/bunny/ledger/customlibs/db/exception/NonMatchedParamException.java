package org.blue.bunny.ledger.customlibs.db.exception;

/**
 * Exception to be thrown when a paerameter value is not defined for a query.
 */
public class NonMatchedParamException extends SQLException {

    /**
     * Serial version uid.
     */
    private static final long serialVersionUID = 1L;
    
    /** {@inheritDoc} */
    public NonMatchedParamException(final String message) {
        super(message);
    }
    
    /** {@inheritDoc} */
    public NonMatchedParamException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
}
