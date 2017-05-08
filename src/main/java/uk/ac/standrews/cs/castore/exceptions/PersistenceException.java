package uk.ac.standrews.cs.castore.exceptions;

/**
 * Indicates a problem with persistence.
 *
 * @author graham
 */
public class PersistenceException extends StorageException {

    public PersistenceException(String msg) {
        super(msg);
    }

    public PersistenceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
