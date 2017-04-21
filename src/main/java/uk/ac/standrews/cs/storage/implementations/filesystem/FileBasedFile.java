package uk.ac.standrews.cs.storage.implementations.filesystem;

import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.data.FileData;
import uk.ac.standrews.cs.storage.exceptions.DataException;
import uk.ac.standrews.cs.storage.exceptions.PersistenceException;
import uk.ac.standrews.cs.storage.exceptions.StorageException;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.IFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class FileBasedFile extends FileBasedStatefulObject implements IFile {

    private Data data;

    public FileBasedFile(IDirectory parent, String name) throws StorageException {
        super(parent, name);

        try {
            realFile = new File(parent.toFile(), name);
        } catch (IOException e) {
            throw new StorageException("Unable to create file " + name, e);
        }

        this.data = new FileData(realFile); // FIXME - read data on demand?
    }

    public FileBasedFile(IDirectory parent, String name, Data data) throws StorageException {
        super(parent, name);

        try {
            realFile = new File(parent.toFile(), name);
        } catch (IOException e) {
            throw new StorageException("Unable to create file " + name, e);
        }

        this.data = data;
    }

    @Override
    public String getPathname() {
        return logicalParent.getPathname() + name;
    }

    @Override
    public void persist() throws PersistenceException {
        createParentFolderIfNone();
        createFile();
        writeData();
    }

    @Override
    public long getSize() {
        return data.getSize();
    }

    private void createParentFolderIfNone() throws PersistenceException {
        if (!logicalParent.exists()) {
            logicalParent.persist();
        }
    }

    private void createFile() throws PersistenceException {
        if (realFile.exists()) {
            if (!realFile.isFile()) {
                throw new PersistenceException("The following " + realFile.getAbsolutePath() + " is not a file");
            }
        } else {
            try {
                boolean newFileCreated = realFile.createNewFile();
                if (!newFileCreated) {
                    throw new PersistenceException("Could not create the file at " + realFile.getAbsolutePath());
                }
            } catch (IOException e) {
                throw new PersistenceException("IO Exception while creating the file at " + realFile.getAbsolutePath(), e);
            }
        }
    }

    private void writeData() throws PersistenceException {
        // Write the data to the file.
        byte[] bytes = data.getState();

        try {
            FileOutputStream output_stream = new FileOutputStream(realFile);
            output_stream.write(bytes);
            output_stream.close();
        } catch (IOException e) {
            throw new PersistenceException("IO Exception while writing to the file at " + realFile.getAbsolutePath(), e);
        }
    }

    @Override
    public void setData(Data data) throws DataException {
        this.data = data;
    }

    @Override
    public Data getData() throws DataException {
        if (data == null) {
            throw new DataException("The file " + getPathname() + " does not have any data");
        }

        return data;
    }
}
