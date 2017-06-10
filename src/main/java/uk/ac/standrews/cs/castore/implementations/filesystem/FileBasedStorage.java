package uk.ac.standrews.cs.castore.implementations.filesystem;

import org.apache.commons.io.FileUtils;
import uk.ac.standrews.cs.castore.data.Data;
import uk.ac.standrews.cs.castore.exceptions.DestroyException;
import uk.ac.standrews.cs.castore.exceptions.PersistenceException;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.implementations.CommonStorage;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.IFile;
import uk.ac.standrews.cs.castore.interfaces.IStorage;

import java.io.File;
import java.io.IOException;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class FileBasedStorage extends CommonStorage implements IStorage {

    public FileBasedStorage(File rootDirectory) throws StorageException {

        root = new FileBasedDirectory(rootDirectory);
        try {
            root.persist();
        } catch (PersistenceException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public IDirectory getRoot() {
        return root;
    }

    @Override
    public IDirectory createDirectory(IDirectory parent, String name) throws StorageException {
        return new FileBasedDirectory(parent, name);
    }

    @Override
    public IFile createFile(IDirectory parent, String filename) throws StorageException {
        return new FileBasedFile(parent, filename);
    }

    @Override
    public IFile createFile(IDirectory parent, String filename, Data data) throws StorageException {
        return new FileBasedFile(parent, filename, data);
    }

    @Override
    public void destroy() throws DestroyException {

        try {
            FileUtils.deleteDirectory(root.toFile());
        } catch (IOException e) {
            throw new DestroyException("Unable to destroy root directory");
        }

        root = null;
    }

}
