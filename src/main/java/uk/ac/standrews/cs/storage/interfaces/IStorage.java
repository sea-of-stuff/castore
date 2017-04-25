package uk.ac.standrews.cs.storage.interfaces;

import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.exceptions.DestroyException;
import uk.ac.standrews.cs.storage.exceptions.StorageException;

/**
 *
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public interface IStorage {

    /**
     * Get the root directory of this storage
     * @return
     */
    IDirectory getRoot();

    /**
     * Create a directory within the specified logicalParent and with the given name
     * @param parent
     * @param name
     * @return
     */
    IDirectory createDirectory(IDirectory parent, String name) throws StorageException;

    /**
     * Create a directory at the root of this storage with the given name
     * @param name
     * @return
     */
    IDirectory createDirectory(String name) throws StorageException;

    /**
     * Create a file at the specified logicalParent directory
     * @param parent
     * @param filename
     * @return
     */
    IFile createFile(IDirectory parent, String filename) throws StorageException;

    /**
     * Create a file with some given data at the specified directory
     * @param parent
     * @param filename
     * @param data
     * @return
     * @throws StorageException
     */
    IFile createFile(IDirectory parent, String filename, Data data) throws StorageException;

    default void persist() { /* NEEDED ONLY FOR IN-MEMORY BASED STORAGES */ }

    /**
     * Destroy this storage
     * @throws DestroyException
     */
    void destroy() throws DestroyException;
}
