package uk.ac.standrews.cs.castore.interfaces;

import uk.ac.standrews.cs.castore.exceptions.PersistenceException;
import uk.ac.standrews.cs.castore.exceptions.RenameException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The stateful object interface defines the set of common operations for Files and Directories
 *
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public interface StatefulObject {

    /**
     * Returns the parent of an object
     *
     * @return parent (as IDirectory) for this object
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
     * @return name
     */
    String getName();

    /**
     *
     * @param newName new name for this object
     * @throws RenameException if object could not be renamed
     */
    void rename(String newName) throws RenameException;

    /**
     * The path name for the object
     *
     * @return pathname
     */
    String getPathname();

    /**
     * Path for this object
     *
     * @return path
     */
    default Path getPath() {
        return Paths.get(getPathname());
    }

    /**
     * The time when the object was last modified
     *
     * @return last modified timestamp
     */
    long lastModified();

    /**
     * Java.io File representation for this current object
     *
     * @return file
     * @throws IOException if the file could not be returned
     */
    File toFile() throws IOException;

    /**
     * Persist the object into the storage
     *
     * @throws PersistenceException if the object could not be persisted
     */
    void persist() throws PersistenceException;

    /**
     * Get the size of the object in bytes
     *
     * @return size of the object in bytes
     */
    long getSize();

}
