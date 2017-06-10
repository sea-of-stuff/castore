package uk.ac.standrews.cs.castore.implementations;

import uk.ac.standrews.cs.castore.data.Data;
import uk.ac.standrews.cs.castore.exceptions.BindingAbsentException;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.IFile;
import uk.ac.standrews.cs.castore.interfaces.IStorage;
import uk.ac.standrews.cs.castore.interfaces.StatefulObject;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public abstract class CommonStorage implements IStorage {

    protected IDirectory root;

    @Override
    public IDirectory getRoot() {
        return root;
    }

    @Override
    public IDirectory createDirectory(String name) throws StorageException {
        return createDirectory(root, name);
    }

    @Override
    public IFile createFile(String filename) throws StorageException {
        return createFile(root, filename);
    }

    @Override
    public IFile createFile(String filename, Data data) throws StorageException {
        return createFile(root, filename, data);
    }

    @Override
    public void delete(StatefulObject object) throws StorageException {

        if (object == null) throw new StorageException();

        try {

            IDirectory parent = object.getParent();
            if (parent != null) {
                parent.remove(object.getName());
            } else {
                throw new StorageException();
            }

        } catch (BindingAbsentException e) {
            throw new StorageException();
        }

    }

}
