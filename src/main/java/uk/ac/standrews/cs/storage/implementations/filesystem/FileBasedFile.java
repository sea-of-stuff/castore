package uk.ac.standrews.cs.storage.implementations.filesystem;

import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.data.FileData;
import uk.ac.standrews.cs.storage.exceptions.DataException;
import uk.ac.standrews.cs.storage.exceptions.PersistenceException;
import uk.ac.standrews.cs.storage.exceptions.StorageException;
import uk.ac.standrews.cs.storage.interfaces.Directory;
import uk.ac.standrews.cs.storage.interfaces.File;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class FileBasedFile extends FileBasedStatefulObject implements File {

    private Data data;
    private boolean persisted;

    public FileBasedFile(Directory parent, String name, boolean isImmutable) throws StorageException {
        super(parent, name, isImmutable);

        try {
            realFile = new java.io.File(parent.toFile(), name);
        } catch (IOException e) {
            throw new StorageException("Unable to create file " + name, e);
        }

        if (isImmutable && exists()) {
            this.persisted = true;
        } else {
            this.persisted = false;
        }

        this.data = new FileData(realFile);
    }

    public FileBasedFile(Directory parent, String name, Data data, boolean isImmutable) throws StorageException {
        super(parent, name, isImmutable);

        try {
            realFile = new java.io.File(parent.toFile(), name);
        } catch (IOException e) {
            throw new StorageException("Unable to create file " + name, e);
        }

        if (isImmutable && exists()) {
            this.persisted = true;
            this.data = new FileData(realFile);
        } else {
            this.persisted = false;
            this.data = data;
        }

    }

    @Override
    public String getPathname() {
        return logicalParent.getPathname() + name;
    }

    @Override
    public void persist() throws PersistenceException {
        if (isImmutable && persisted) {
            return;
        }

        createParentFolderIfNone();
        createFile();
        writeData();
        persisted = true;
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
                if (!realFile.createNewFile()) {
                    throw new PersistenceException("Could not create the file at " + realFile.getAbsolutePath() + " files in dir " + toString(realFile.getParentFile().list()));
                }
            } catch (IOException e) {
                throw new PersistenceException("IO Exception while creating the file at " + realFile.getAbsolutePath(), e);
            }
        }
    }

    private String toString(String[] list ) {
        String retval = "";
        for(String s : list) {
            retval += s + ", ";
        }
        return retval;
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
        if (!persisted) {
            this.data = data;
        } else {
            throw new DataException("Could not set data for file " + getPathname());
        }
    }

    @Override
    public Data getData() throws DataException {
        if (data == null) {
            throw new DataException("The file " + getPathname() + " does not have any data");
        }

        return data;
    }
}
