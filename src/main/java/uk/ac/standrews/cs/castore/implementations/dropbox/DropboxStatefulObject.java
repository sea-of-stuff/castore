package uk.ac.standrews.cs.castore.implementations.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.Metadata;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.implementations.CommonStatefulObject;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.StatefulObject;

import java.io.File;
import java.io.IOException;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public abstract class DropboxStatefulObject extends CommonStatefulObject implements StatefulObject {

    protected DbxClientV2 client;

    protected IDirectory logicalParent;
    protected String objectPath;

    public DropboxStatefulObject(DbxClientV2 client, IDirectory parent, String name) throws StorageException {
        super(name);

        this.client = client;
        this.logicalParent = parent;
        this.objectPath = getPathname();
    }

    public DropboxStatefulObject(DbxClientV2 client, String name) throws StorageException {
        super(name);

        this.client = client;
        this.objectPath = getPathname();
    }

    @Override
    public IDirectory getParent() {
        return logicalParent;
    }

    @Override
    public boolean exists() {

        try {
            getMetadata();
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
        throw new IOException("Unable to make file");
    }

    protected Metadata getMetadata() throws DbxException {

        return getMetadata(objectPath);
    }

    protected Metadata getMetadata(String path) throws DbxException {

        path = normalise(path);
        return client.files().getMetadata(path);
    }

}
