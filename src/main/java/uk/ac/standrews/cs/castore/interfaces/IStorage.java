package uk.ac.standrews.cs.castore.interfaces;

import uk.ac.standrews.cs.castore.data.Data;
import uk.ac.standrews.cs.castore.exceptions.DestroyException;
import uk.ac.standrews.cs.castore.exceptions.StorageException;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public interface IStorage {

    /**
     * Get the root directory of this storage
     *
     * @return
     */
    IDirectory getRoot();

    /**
     * Create a directory within the specified parent and with the given name
     *
     * @param parent
     * @param name
     * @return
     */
    IDirectory createDirectory(IDirectory parent, String name) throws StorageException;

    /**
     * Create a directory at the root of this storage with the given name
     *
     * @param name
     * @return
     */
    IDirectory createDirectory(String name) throws StorageException;

    /**
     * Create a file at the specified parent directory
     *
     * @param parent
     * @param filename
     * @return
     */
    IFile createFile(IDirectory parent, String filename) throws StorageException;

    /**
     * Create a file at the root directory
     *
     * @param filename
     * @return
     * @throws StorageException
     */
    IFile createFile(String filename) throws StorageException;

    /**
     * Create a file with the given data at the specified directory
     *
     * @param parent
     * @param filename
     * @param data
     * @return
     * @throws StorageException
     */
    IFile createFile(IDirectory parent, String filename, Data data) throws StorageException;

    /**
     * Create a file at the root directory with the given data
     *
     * @param filename
     * @param data
     * @return
     * @throws StorageException
     */
    IFile createFile(String filename, Data data) throws StorageException;

    /**
     * Delete the object
     *
     * @param object
     * @throws StorageException
     */
    void delete(StatefulObject object) throws StorageException;

    /**
     * Persist in-memory based storages to disk
     */
    default void persist() { /* NEEDED ONLY FOR IN-MEMORY BASED STORAGES */ }

    /**
     * Destroy this storage
     *
     * @throws DestroyException if the storage could not be destroyed
     */
    void destroy() throws DestroyException;
}
