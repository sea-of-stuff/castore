package uk.ac.standrews.cs.castore.implementations.filesystem;

import uk.ac.standrews.cs.castore.data.Data;
import uk.ac.standrews.cs.castore.exceptions.PersistenceException;
import uk.ac.standrews.cs.castore.exceptions.RenameException;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.implementations.CommonStatefulObject;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.StatefulObject;

import java.io.File;
import java.io.IOException;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public abstract class FileBasedStatefulObject extends CommonStatefulObject implements StatefulObject {

    protected IDirectory parent;
    File realFile;

    FileBasedStatefulObject(IDirectory parent, String name) throws StorageException {
        super(name);

        this.parent = parent;
    }

    FileBasedStatefulObject(IDirectory parent, String name, Data data) throws StorageException {
        super(name, data);

        this.parent = parent;
    }

    FileBasedStatefulObject() {
        this.name = "";
    }

    @Override
    public IDirectory getParent() {
        return parent;
    }

    @Override
    public boolean exists() {
        return realFile.exists();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void rename(String newName) throws RenameException {

        try {
            boolean renamed = realFile.renameTo(new File(parent.toFile(), newName));
            if (!renamed) throw new RenameException();

            name = newName;

        } catch (IOException e) {
            throw new RenameException();
        }
    }

    @Override
    public long lastModified() {
        return realFile.lastModified();
    }

    @Override
    public File toFile() {
        return realFile;
    }

    @Override
    public abstract void persist() throws PersistenceException;

}
