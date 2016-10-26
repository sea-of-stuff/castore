package uk.ac.standrews.cs.storage.implementations.filesystem;

import uk.ac.standrews.cs.storage.exceptions.PersistenceException;
import uk.ac.standrews.cs.storage.interfaces.Directory;
import uk.ac.standrews.cs.storage.interfaces.StatefulObject;

import java.io.File;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public abstract class FileBasedStatefulObject implements StatefulObject {

    protected Directory logicalParent;
    protected String name;
    protected File realFile;

    public FileBasedStatefulObject(Directory parent, String name) {
        this.logicalParent = parent;
        this.name = name;
    }

    public FileBasedStatefulObject() {
        this.name = "";
    }

    @Override
    public Directory getParent() {
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
    public abstract String getPathname();

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
