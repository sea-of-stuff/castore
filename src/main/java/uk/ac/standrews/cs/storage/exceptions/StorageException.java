package uk.ac.standrews.cs.storage.exceptions;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class StorageException extends Exception {

    public StorageException() {
        super();
    }

    public StorageException(String message) {
        super(message);
    }

    public StorageException(Throwable throwable) {
        super(throwable);
    }

    public StorageException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
