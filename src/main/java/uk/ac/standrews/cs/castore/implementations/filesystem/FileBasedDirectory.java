package uk.ac.standrews.cs.castore.implementations.filesystem;

import org.apache.commons.io.FileUtils;
import uk.ac.standrews.cs.castore.exceptions.BindingAbsentException;
import uk.ac.standrews.cs.castore.exceptions.PersistenceException;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.implementations.NameObjectBindingImpl;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.NameObjectBinding;
import uk.ac.standrews.cs.castore.interfaces.StatefulObject;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import static uk.ac.standrews.cs.castore.CastoreConstants.FOLDER_DELIMITER;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class FileBasedDirectory extends FileBasedStatefulObject implements IDirectory {

    private static final Logger log = Logger.getLogger(FileBasedDirectory.class.getName());

    FileBasedDirectory( IDirectory parent,String name) throws StorageException {
        super(parent, name);

        try {
            realFile = new File(parent.toFile(), name);
        } catch (IOException e) {
            throw new StorageException("Unable to create directory " + name, e);
        }
    }

    FileBasedDirectory(File directory) {
        super();
        realFile = directory;
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
    public String getPathname() {
        if (parent == null) {
            return realFile.getAbsolutePath() + FOLDER_DELIMITER;
        } else if (name == null || name.isEmpty()) {
            return parent.getPathname() + FOLDER_DELIMITER;
        } else {
            return parent.getPathname() + name + FOLDER_DELIMITER;
        }
    }

    @Override
    public void persist() throws PersistenceException {
        if (realFile.exists() && !realFile.isDirectory()) {
            throw new PersistenceException(realFile.getAbsolutePath() + " is not a directory");
        } else if (!realFile.exists()) {
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
        File candidate = new File(realFile, name);
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
        File candidate = new File(realFile, name);
        return candidate.exists();
    }

    @Override
    public void remove(String name) throws BindingAbsentException {
        File candidate = new File(realFile, name);
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
    public void rename(String oldName, String newName) throws BindingAbsentException {
        // DO NOTHING
    }

    @Override
    public Iterator<NameObjectBinding> getIterator() {
        return new DirectoryIterator(realFile);
    }

    private class DirectoryIterator implements Iterator<NameObjectBinding>  {

        private String[] names;
        private int index;

        DirectoryIterator(File realFile) {
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
                String name = names[index]; index++;
                StatefulObject obj = get(name);

                return new NameObjectBindingImpl(name, obj);
            } catch (BindingAbsentException e) {
                log.log(Level.SEVERE, "Unable to create a Binding Object for the next element from the iterator");
            }

            return null;
        }
    }
}
