package uk.ac.standrews.cs.storage.implementations.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import uk.ac.standrews.cs.storage.exceptions.BindingAbsentException;
import uk.ac.standrews.cs.storage.exceptions.PersistenceException;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.NameObjectBinding;
import uk.ac.standrews.cs.storage.interfaces.StatefulObject;

import java.util.Iterator;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class DropboxDirectory extends DropboxStatefulObject implements IDirectory {

    public DropboxDirectory(DbxClientV2 client, IDirectory parent, String name) {
        super(client, parent, name);
    }

    public DropboxDirectory(DbxClientV2 client, String name) {
        super(client, name);
    }

    @Override
    public StatefulObject get(String name) throws BindingAbsentException {
        return null;
    }

    @Override
    public boolean contains(String name) {
        return false;
    }

    @Override
    public void remove(String name) throws BindingAbsentException {

    }

    @Override
    public Iterator<NameObjectBinding> getIterator() {
        return null;
    }

    @Override
    public void persist() throws PersistenceException {
        try {
            client.files().createFolder("/Apps/castore/folder"); // FIXME - do not hardcode
        } catch (DbxException e) {
            throw new PersistenceException("Unable to persist directory", e);
        }
    }
}
