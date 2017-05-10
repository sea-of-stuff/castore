package uk.ac.standrews.cs.castore.implementations.filesystem;

import uk.ac.standrews.cs.castore.exceptions.PersistenceException;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.implementations.CommonStatefulObject;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.StatefulObject;

import java.io.File;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public abstract class FileBasedStatefulObject extends CommonStatefulObject implements StatefulObject {

    protected IDirectory logicalParent;
    protected File realFile;

    public FileBasedStatefulObject(IDirectory parent, String name) throws StorageException {
        super(name);

        this.logicalParent = parent;
    }

    public FileBasedStatefulObject() {
        this.name = "";
    }

    @Override
    public IDirectory getLogicalParent() {
        return logicalParent;
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