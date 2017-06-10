package uk.ac.standrews.cs.castore.interfaces;

import uk.ac.standrews.cs.castore.exceptions.PersistenceException;
import uk.ac.standrews.cs.castore.exceptions.RenameException;

import java.io.File;
import java.io.IOException;

/**
 * The stateful object interface defines the set of common operations for Files and Directories
 *
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public interface StatefulObject {

    /**
     * Returns the parent of an object
     *
     * @return
     */
    IDirectory getParent();

    /**
     * Check if the object exists in the storage.
     * The object must be persisted in the storage first.
     * @see #persist()
     *
     * @return true if the object exists in the storage
     */
    boolean exists();

    /**
     * The name of the object
     *
     * @return
     */
    String getName();

    void rename(String newName) throws RenameException;

    /**
     * The path name for the object
     * @return
     */
    String getPathname();

    /**
     * The time when the object was last modified
     *
     * @return
     */
    long lastModified();

    // TODO - is this method necessary?
    File toFile() throws IOException;

    /**
     * Persist the object into the storage
     *
     * @throws PersistenceException
     */
    void persist() throws PersistenceException;

    /**
     * Get the size of the object in bytes
     *
     * @return size of the object in bytes
     */
    long getSize();

}
