package uk.ac.standrews.cs.castore.implementations.filesystem;

import uk.ac.standrews.cs.castore.data.Data;
import uk.ac.standrews.cs.castore.data.FileData;
import uk.ac.standrews.cs.castore.exceptions.DataException;
import uk.ac.standrews.cs.castore.exceptions.PersistenceException;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.IFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class FileBasedFile extends FileBasedStatefulObject implements IFile {

    public FileBasedFile(IDirectory parent, String name) throws StorageException {
        super(parent, name);

        try {
            realFile = new File(parent.toFile(), name);
        } catch (IOException e) {
            throw new StorageException("Unable to create file " + name, e);
        }

        this.data = new FileData(realFile);
    }

    public FileBasedFile(IDirectory parent, String name, Data data) throws StorageException {
        super(parent, name, data);

        try {
            realFile = new File(parent.toFile(), name);
        } catch (IOException e) {
            throw new StorageException("Unable to create file " + name, e);
        }

    }

    @Override
    public String getPathname() {
        return parent.getPathname() + name;
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
        if (!parent.exists()) {
            parent.persist();
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

        try (FileOutputStream output_stream = new FileOutputStream(realFile)){

            output_stream.write(bytes);

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
