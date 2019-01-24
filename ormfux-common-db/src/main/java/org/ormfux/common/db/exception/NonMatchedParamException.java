package org.ormfux.common.db.exception;

/**
 * Exception to be thrown when a paerameter value is not defined for a query.
 */
public class NonMatchedParamException extends SQLException {

    /**
     * Serial version uid.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * @param message Exception message. 
     */
    public NonMatchedParamException(final String message) {
        super(message);
    }
    
    /**
     * @param message Exception message.
     * @param cause nested Exception. 
     */
    public NonMatchedParamException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
}
