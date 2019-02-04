package org.ormfux.common.db.exception;

/**
 * Exception to be thrown when the changes of an entity, that need to be persisted,
 * represent a state that is older than the state of the entity in the database.
 */
public class StaleEntityException extends SQLException {

    /**
     * Serial version uid.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * @param message Exception message.
     */
    public StaleEntityException(final String message) {
        super(message);
    }
    
    /**
     * @param message Exception message.
     * @param cause nested Exception. 
     */
    public StaleEntityException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
}
