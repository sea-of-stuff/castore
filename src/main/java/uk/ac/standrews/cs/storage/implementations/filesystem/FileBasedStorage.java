package uk.ac.standrews.cs.storage.implementations.filesystem;

import org.apache.commons.io.FileUtils;
import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.exceptions.DestroyException;
import uk.ac.standrews.cs.storage.exceptions.PersistenceException;
import uk.ac.standrews.cs.storage.exceptions.StorageException;
import uk.ac.standrews.cs.storage.implementations.CommonStorage;
import uk.ac.standrews.cs.storage.interfaces.Directory;
import uk.ac.standrews.cs.storage.interfaces.File;
import uk.ac.standrews.cs.storage.interfaces.IStorage;

import java.io.IOException;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class FileBasedStorage extends CommonStorage implements IStorage {

    private Directory root;

    public FileBasedStorage(java.io.File rootDirectory) throws StorageException {

        root = new FileBasedDirectory(rootDirectory);
        try {
            root.persist();
        } catch (PersistenceException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public Directory getRoot() {
        return root;
    }

    @Override
    public Directory createDirectory(Directory parent, String name) throws StorageException {
        return new FileBasedDirectory(parent, name);
    }

    @Override
    public Directory createDirectory(String name) throws StorageException {
        return new FileBasedDirectory(root, name);
    }

    @Override
    public File createFile(Directory parent, String filename) throws StorageException {
        File file = new FileBasedFile(parent, filename);

        return file;
    }

    @Override
    public File createFile(Directory parent, String filename, Data data) throws StorageException {
        File file = new FileBasedFile(parent, filename, data);

        return file;
    }

    @Override
    public void destroy() throws DestroyException {
        try {
            FileUtils.deleteDirectory(root.toFile());
        } catch (IOException e) {
            throw new DestroyException("Unable to destroy root directory");
        }
    }

}
