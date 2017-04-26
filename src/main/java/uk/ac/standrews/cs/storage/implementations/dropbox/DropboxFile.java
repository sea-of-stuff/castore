package uk.ac.standrews.cs.storage.implementations.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.exceptions.DataException;
import uk.ac.standrews.cs.storage.exceptions.PersistenceException;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.IFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class DropboxFile extends DropboxStatefulObject implements IFile {

    public DropboxFile(DbxClientV2 client, IDirectory parent, String name) {
        super(client, parent, name);
    }

    public DropboxFile(DbxClientV2 client, IDirectory parent, String name, Data data) {
        super(client, parent, name);

        this.data = data;
    }

    @Override
    public String getPathname() {
        return logicalParent.getPathname() + name;
    }

    @Override
    public void setData(Data data) throws DataException {
        this.data = data;
    }

    @Override
    public Data getData() throws DataException {
        return data;
    }

    @Override
    public void persist() throws PersistenceException {
        try (InputStream inputStream = data.getInputStream()){
            String path = getPathname();
            uploadFile(client, inputStream, path);
        } catch (DbxException | IOException e) {
            throw new PersistenceException("Unable to persist data to Dropbox", e);
        }
    }

    /**
     * Uploads a file in a single request. This approach is preferred for small files since it
     * eliminates unnecessary round-trips to the servers.
     *
     * @param dbxClient Dropbox user authenticated client
     * @param inputStream data to upload
     * @param dropboxPath Where to upload the file to within Dropbox
     */
    private static void uploadFile(DbxClientV2 dbxClient, InputStream inputStream, String dropboxPath) throws IOException, DbxException {

        FileMetadata metadata = dbxClient.files()
                .uploadBuilder(dropboxPath)
                .withMode(WriteMode.ADD)
                .uploadAndFinish(inputStream);

    }

}
