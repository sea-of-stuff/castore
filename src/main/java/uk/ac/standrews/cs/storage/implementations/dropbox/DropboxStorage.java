package uk.ac.standrews.cs.storage.implementations.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.exceptions.DestroyException;
import uk.ac.standrews.cs.storage.exceptions.StorageException;
import uk.ac.standrews.cs.storage.implementations.CommonStorage;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.IFile;
import uk.ac.standrews.cs.storage.interfaces.IStorage;

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

    public DropboxStorage(final String accessToken, String root) {

        DbxRequestConfig config = DbxRequestConfig.newBuilder("castore/1.0")
                .withUserLocaleFrom(Locale.UK)
                .build();
        client = new DbxClientV2(config, accessToken);

        this.rootPath = root;
        createRoot();
    }

    @Override
    public IDirectory createDirectory(IDirectory parent, String name) throws StorageException {
        return new DropboxDirectory(client, parent, name);
    }

    @Override
    public IDirectory createDirectory(String name) throws StorageException {
        return createDirectory(root, name);
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

    }

    private void createRoot() {
        root = new DropboxDirectory(client, rootPath);
    }
}
