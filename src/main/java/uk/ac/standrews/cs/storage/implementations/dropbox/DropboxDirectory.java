package uk.ac.standrews.cs.storage.implementations.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import uk.ac.standrews.cs.storage.exceptions.BindingAbsentException;
import uk.ac.standrews.cs.storage.exceptions.PersistenceException;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.NameObjectBinding;
import uk.ac.standrews.cs.storage.interfaces.StatefulObject;

import java.util.Iterator;

import static uk.ac.standrews.cs.storage.CastoreConstants.FOLDER_DELIMITER;

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
    public String getPathname() {
        if (logicalParent == null) {
            return name + FOLDER_DELIMITER;
        } else if (name == null || name.isEmpty()) {
            return logicalParent.getPathname() + FOLDER_DELIMITER;
        } else {
            return logicalParent.getPathname() + name + FOLDER_DELIMITER;
        }
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
            String path = getPathname();
            path = path.substring(0, path.length() - 1); // Removing last slash
            client.files().createFolder(path);
        } catch (DbxException e) {
            throw new PersistenceException("Unable to persist directory", e);
        }
    }
}
