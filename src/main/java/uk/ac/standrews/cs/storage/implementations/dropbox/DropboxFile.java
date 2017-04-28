package uk.ac.standrews.cs.storage.implementations.dropbox;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.WriteMode;
import com.fasterxml.jackson.databind.JsonNode;
import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.data.InputStreamData;
import uk.ac.standrews.cs.storage.exceptions.DataException;
import uk.ac.standrews.cs.storage.exceptions.PersistenceException;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.IFile;
import uk.ac.standrews.cs.storage.utils.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class DropboxFile extends DropboxStatefulObject implements IFile {

    private static final Logger log = Logger.getLogger(DropboxFile.class.getName());

    public DropboxFile(DbxClientV2 client, IDirectory parent, String name) {
        super(client, parent, name);

        if (exists()) retrieveAndUpdateData();
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

    @Override
    public long getSize() {

        try {
            Metadata metadata = client.files().getMetadata(getPathname());
            JsonNode node = JSON.Mapper().readTree(metadata.toStringMultiline());

            return node.get("size").asLong();

        } catch (DbxException | IOException e) {
            log.log(Level.WARNING, "Unable to retrieve the size info about the file with path " + getPathname());
        }

        return 0;
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

    private void retrieveAndUpdateData() {
        try {
            DbxDownloader<FileMetadata> downloadedContent = client.files().download(getPathname());
            data = new InputStreamData(downloadedContent.getInputStream());
        } catch (DbxException e) {
            e.printStackTrace();
        }
    }

}
