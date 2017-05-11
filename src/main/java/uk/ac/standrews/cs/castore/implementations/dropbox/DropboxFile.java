package uk.ac.standrews.cs.castore.implementations.dropbox;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.NullInputStream;
import uk.ac.standrews.cs.castore.data.Data;
import uk.ac.standrews.cs.castore.data.InputStreamData;
import uk.ac.standrews.cs.castore.exceptions.DataException;
import uk.ac.standrews.cs.castore.exceptions.PersistenceException;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.IFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class DropboxFile extends DropboxStatefulObject implements IFile {

    private static final Logger log = Logger.getLogger(DropboxFile.class.getName());

    private static final String TMP_FILE_PREFIX = "dropbox";
    private static final String TMP_FILE_SUFFIX = ".tmp";

    protected Data data;

    DropboxFile(DbxClientV2 client, IDirectory parent, String name) throws StorageException {
        super(client, parent, name);

        if (exists()) {
            retrieveAndUpdateData();
        } else {
            data = new InputStreamData(new NullInputStream(0));
        }
    }

    DropboxFile(DbxClientV2 client, IDirectory parent, String name, Data data) throws StorageException {
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

        try (InputStream inputStream = data.getInputStream()) {

            if (!exists()) {
                uploadFile(client, inputStream, objectPath);
            } else {
                updateFile(client, inputStream, objectPath);
            }

        } catch (DbxException | IOException e) {
            throw new PersistenceException("Unable to persist data to Dropbox", e);
        }
    }

    @Override
    public long getSize() {

        try {
            FileMetadata metadata = (FileMetadata) getMetadata();
            return metadata.getSize();

        } catch (DbxException e) {
            log.log(Level.WARNING, "Unable to retrieve the size info about the file with path " + getPathname());
        }

        return 0;
    }

    @Override
    public long lastModified() {

        long retval = 0;

        try {
            FileMetadata metadata = (FileMetadata) getMetadata();
            retval = metadata.getClientModified().getTime();

        } catch (DbxException e) {
            log.log(Level.WARNING, "Unable to retrieve the last modified time about the file with path " + getPathname());
        }

        return retval;
    }

    @Override
    public File toFile() throws IOException {


        final File tempFile = File.createTempFile(TMP_FILE_PREFIX, TMP_FILE_SUFFIX);
        tempFile.deleteOnExit();

        try (FileOutputStream output = new FileOutputStream(tempFile);
             InputStream input = downloadStream()) {

            IOUtils.copy(input, output);
        } catch (DbxException e) {
            throw new IOException("Unable to fetch data");
        }

        return tempFile;

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

        dbxClient.files()
                .uploadBuilder(dropboxPath)
                .withMode(WriteMode.ADD)
                .uploadAndFinish(inputStream);
    }

    private static void updateFile(DbxClientV2 dbxClient, InputStream inputStream, String dropboxPath) throws IOException, DbxException {

        dbxClient.files()
                .uploadBuilder(dropboxPath)
                .withMode(WriteMode.OVERWRITE)
                .uploadAndFinish(inputStream);
    }


    private InputStream downloadStream() throws DbxException {

        DbxDownloader<FileMetadata> downloadedContent = client.files().download(objectPath);
        return downloadedContent.getInputStream();
    }

    private void retrieveAndUpdateData() {

        try (InputStream inputStream = downloadStream()) {
            data = new InputStreamData(inputStream);

        } catch (DbxException | IOException e) {
            log.log(Level.SEVERE, "Unable to retrieve and update data");
        }
    }

}
