package org.blue.bunny.ledger.customlibs.db.exception;

/**
 * General exception to be thrown in case of SQL errors.
 */
public class SQLException extends RuntimeException {

    /**
     * Serial version uid.
     */
    private static final long serialVersionUID = 1L;
    
    /** {@inheritDoc} */
    public SQLException(final String message) {
        super(message);
    }
    
    /** {@inheritDoc} */
    public SQLException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
}
