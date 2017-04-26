package uk.ac.standrews.cs.storage.implementations.dropbox;

import com.dropbox.core.v2.DbxClientV2;
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
        return false;
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
        return null;
    }

    @Override
    public long getSize() {
        return 0;
    }
}
