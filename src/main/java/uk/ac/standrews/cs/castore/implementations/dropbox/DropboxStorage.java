package uk.ac.standrews.cs.castore.implementations.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import uk.ac.standrews.cs.castore.data.Data;
import uk.ac.standrews.cs.castore.exceptions.DestroyException;
import uk.ac.standrews.cs.castore.exceptions.PersistenceException;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.implementations.CommonStorage;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.IFile;
import uk.ac.standrews.cs.castore.interfaces.IStorage;

import java.util.Locale;

/**
 * Dropbox CORE API available here - https://www.dropbox.com/developers/documentation/java
 * Github project - https://github.com/dropbox/dropbox-sdk-java
 * Useful example - https://github.com/dropbox/dropbox-sdk-java/blob/master/examples/upload-file/src/main/java/com/dropbox/core/examples/upload_file/Main.java
 *
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class DropboxStorage extends CommonStorage implements IStorage {

    private DbxClientV2 client;
    private String rootPath;

    public DropboxStorage(final String accessToken, String rootPath) throws StorageException {

        DbxRequestConfig config = DbxRequestConfig.newBuilder("castore/1.0")
                .withUserLocaleFrom(Locale.UK)
                .build();
        client = new DbxClientV2(config, accessToken);

        this.rootPath = rootPath;
        createRoot();
    }

    /**
     * Create a DropboxStorage handle at the given root.
     *
     * This constructor assumes that the token app has been set at the environment variable DROPBOX_TOKEN
     *
     * @param root
     */
    public DropboxStorage(String root) throws StorageException {
        this(System.getenv().get("DROPBOX_TOKEN"), root);
    }

    @Override
    public IDirectory createDirectory(IDirectory parent, String name) throws StorageException {
        return new DropboxDirectory(client, parent, name);
    }

    @Override
    public IFile createFile(IDirectory parent, String filename) throws StorageException {
        return new DropboxFile(client, parent, filename);
    }

    @Override
    public IFile createFile(IDirectory parent, String filename, Data data) throws StorageException {
        return new DropboxFile(client, parent, filename, data);
    }

    @Override
    public void destroy() throws DestroyException {

        try {
            // Dropbox accepts deletion of folders with content only
            if (root.getIterator().hasNext()) {
                client.files().delete(rootPath);
            }
        } catch (DbxException e) {
            throw new DestroyException("Unable to destroy storage with root path " + rootPath);
        }

        root = null;
    }

    private void createRoot() throws StorageException {
        try {
            root = new DropboxDirectory(client, rootPath);
            root.persist();
        } catch (PersistenceException e) {
            throw new StorageException(e);
        }
    }
}
