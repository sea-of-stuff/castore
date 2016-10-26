package uk.ac.standrews.cs.storage.implementations.filesystem;

import org.apache.commons.io.FileUtils;
import uk.ac.standrews.cs.storage.exceptions.BindingAbsentException;
import uk.ac.standrews.cs.storage.exceptions.PersistenceException;
import uk.ac.standrews.cs.storage.exceptions.StorageException;
import uk.ac.standrews.cs.storage.implementations.NameObjectBindingImpl;
import uk.ac.standrews.cs.storage.interfaces.Directory;
import uk.ac.standrews.cs.storage.interfaces.NameObjectBinding;
import uk.ac.standrews.cs.storage.interfaces.StatefulObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class FileBasedDirectory extends FileBasedStatefulObject implements Directory {

    private static final Logger log = Logger.getLogger(FileBasedDirectory.class.getName());

    public FileBasedDirectory(Directory parent, String name) throws StorageException {
        super(parent, name);

        try {
            realFile = new java.io.File(parent.toFile(), name);
        } catch (IOException e) {
            throw new StorageException("Unable to create directory " + name, e);
        }
    }

    public FileBasedDirectory(java.io.File directory) {
        super();
        realFile = directory;
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
    public String getPathname() {
        if (logicalParent == null) {
            return realFile.getAbsolutePath() + "/";
        } else if (name == null || name.isEmpty()) {
            return logicalParent.getPathname() + "/";
        } else {
            return logicalParent.getPathname() + name + "/";
        }
    }

    @Override
    public void persist() throws PersistenceException {
        if (realFile.exists()) {
            if (!realFile.isDirectory()) {
                throw new PersistenceException(realFile.getAbsolutePath() + " is not a directory");
            }
        } else {
            createDirectory();
        }
    }

    private void createDirectory() throws PersistenceException {
        if (!realFile.mkdirs()) {
            throw new PersistenceException("Could not create directory " + realFile.getAbsolutePath());
        }
    }

    @Override
    public StatefulObject get(String name) throws BindingAbsentException {
        java.io.File candidate = new java.io.File(realFile, name);
        if (!candidate.exists()) {
            throw new BindingAbsentException("Object " + name + " is not present");
        }

        try {
            if (candidate.isFile()) {
                return new FileBasedFile(this, name);
            } else if (candidate.isDirectory()) {
                return new FileBasedDirectory(this, name);
            }
        } catch (StorageException e) {
            throw new BindingAbsentException("Unable to get file/directory " + name + " at " + getPathname());
        }

        return null;
    }

    @Override
    public boolean contains(String name) {
        java.io.File candidate = new java.io.File(realFile, name);
        return candidate.exists();
    }

    @Override
    public void remove(String name) throws BindingAbsentException {
        java.io.File candidate = new java.io.File(realFile, name);
        if (!candidate.exists()) {
            throw new BindingAbsentException("File/directory " + name + " not present");
        }

        try {
            if (candidate.isDirectory()) {
                FileUtils.deleteDirectory(candidate);
            } else {
                candidate.delete(); // Ignore result - nothing to do with it.
            }
        } catch (IOException e) {
            throw new BindingAbsentException("Unable to delete file/directory " + name);
        }

    }

    @Override
    public Iterator<NameObjectBinding> getIterator() {
        return new DirectoryIterator(realFile);
    }

    private class DirectoryIterator implements Iterator<NameObjectBinding>  {

        private String[] names;
        private int index;

        public DirectoryIterator(java.io.File realFile) {
            names = realFile.list();
            if (names == null) {
                log.log(Level.FINE, "File " + realFile.getPath() + " is not a directory");
            }
            index = 0;
        }

        public boolean hasNext() {
            return names != null && index < names.length;
        }

        public NameObjectBinding next() {

            if (!hasNext()) {
                return null;
            }

            try {
                String name = names[index];
                StatefulObject obj = get(name);
                index++;

                return new NameObjectBindingImpl(name, obj);
            } catch (BindingAbsentException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
