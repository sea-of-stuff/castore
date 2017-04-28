package uk.ac.standrews.cs.storage.implementations.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.Metadata;
import uk.ac.standrews.cs.storage.CommonStatefulObject;
import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.StatefulObject;

import java.io.File;
import java.io.IOException;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public abstract class DropboxStatefulObject extends CommonStatefulObject implements StatefulObject {

    protected DbxClientV2 client;

    protected IDirectory logicalParent;
    protected String name;
    protected String objectPath;
    protected Data data;

    public DropboxStatefulObject(DbxClientV2 client, IDirectory parent, String name) {
        this.client = client;
        this.logicalParent = parent;
        this.name = name;
        this.objectPath = getPathname();
    }

    public DropboxStatefulObject(DbxClientV2 client, String name) {
        this.client = client;
        this.name = name;
        this.objectPath = getPathname();
    }

    @Override
    public IDirectory getLogicalParent() {
        return logicalParent;
    }

    @Override
    public boolean exists() {

        try {
            String path = getPathname();

            if (path.charAt(path.length() -1 ) == '/') {
                path = path.substring(0, path.length() - 1); // Removing last slash
            }

            Metadata metadata = client.files().getMetadata(path);
        } catch (DbxException e) {
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long lastModified() {
        return 0;
    }

    @Override
    public File toFile() throws IOException {
        // TODO - download and make a file
        return null;
    }

}
