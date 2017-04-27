package uk.ac.standrews.cs.storage.implementations.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.Metadata;
import com.fasterxml.jackson.databind.JsonNode;
import uk.ac.standrews.cs.storage.exceptions.BindingAbsentException;
import uk.ac.standrews.cs.storage.exceptions.PersistenceException;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.NameObjectBinding;
import uk.ac.standrews.cs.storage.interfaces.StatefulObject;
import uk.ac.standrews.cs.storage.utils.JSON;

import java.io.IOException;
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

        try {

            Metadata metadata = client.files().getMetadata(getPathname() + name);

            JsonNode node = JSON.Mapper().readTree(metadata.toString());
            String type = node.get(".tag").textValue();

            switch (type) {
                case "folder":
                    return new DropboxDirectory(client, this, name);
                case "file":
                    return new DropboxFile(client, this, name);
                default:
                    throw new BindingAbsentException("Object type is unknown");
            }

        } catch (DbxException | IOException e) {
            throw new BindingAbsentException("Unable to get object with name " + name);
        }

    }

    @Override
    public boolean contains(String name) {

        try {
            Metadata metadata = client.files().getMetadata(getPathname() + name);
        } catch (DbxException e) {
            return false;
        }

        return true;
    }

    @Override
    public void remove(String name) throws BindingAbsentException {

        try {
            client.files().delete(getPathname() + name);
        } catch (DbxException e) {
            throw new BindingAbsentException("Unable to delete object with name " + name);
        }
    }

    @Override
    public Iterator<NameObjectBinding> getIterator() {
        // TODO
        return null;
    }

    @Override
    public void persist() throws PersistenceException {
        try {
            if (exists()) return;

            String path = getPathname();
            path = path.substring(0, path.length() - 1); // Removing last slash

            client.files().createFolder(path);
        } catch (DbxException e) {
            throw new PersistenceException("Unable to persist directory", e);
        }
    }
}
