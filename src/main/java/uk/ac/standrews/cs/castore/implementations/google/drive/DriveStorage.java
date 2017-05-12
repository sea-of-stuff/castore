package uk.ac.standrews.cs.castore.implementations.google.drive;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import uk.ac.standrews.cs.castore.data.Data;
import uk.ac.standrews.cs.castore.exceptions.DestroyException;
import uk.ac.standrews.cs.castore.exceptions.PersistenceException;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.implementations.CommonStorage;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.IFile;
import uk.ac.standrews.cs.castore.interfaces.IStorage;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class DriveStorage extends CommonStorage implements IStorage {

    private static final Logger log = Logger.getLogger(DriveStorage.class.getName());
    private static final String CREDENTIALS_DEFAULT_PATH = System.getenv().get("HOME") + "/.drive/credentials.json";

    private Drive drive;
    private String rootPath;

    public DriveStorage(String credentialFile, String path) throws StorageException {
        this.rootPath = path;

        try {
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(new java.io.File(credentialFile)))
                    .createScoped(Collections.singleton(DriveScopes.DRIVE_FILE));

            drive = new Drive.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName("castore/1.0")
                    .build();

            createRoot();
        } catch (GeneralSecurityException | IOException e) {
            throw new StorageException("Unable to create Google Drive storage object", e);
        }
    }

    public DriveStorage(String path) throws StorageException {
        this(CREDENTIALS_DEFAULT_PATH, path);
    }

    @Override
    public IDirectory createDirectory(IDirectory parent, String name) throws StorageException {
        return new DriveDirectory(drive, parent, name);
    }

    @Override
    public IDirectory createDirectory(String name) throws StorageException {
        return new DriveDirectory(drive, root, name);
    }

    @Override
    public IFile createFile(IDirectory parent, String filename) throws StorageException {
        return new DriveFile(drive, parent, filename);
    }

    @Override
    public IFile createFile(IDirectory parent, String filename, Data data) throws StorageException {
        return new DriveFile(drive, parent, filename, data);
    }

    /**
     * Destroy all the content for the drive app
     *
     * @throws DestroyException
     */
    public void destroy() throws DestroyException {

        try {
            String token;
            FileList contents = drive.files().list().execute();
            while(true) {

                for (File file : contents.getFiles()) {
                    // log.log(Level.INFO, "Deleting file/folder with name " + file.getName() + " with id " + file.getId());
                    drive.files()
                            .delete(file.getId())
                            .execute();
                }

                token = contents.getNextPageToken();
                if (token == null) break;

                contents = drive.files()
                        .list()
                        .setPageToken(token)
                        .execute();
            }

        } catch (IOException e) {
            throw new DestroyException("Unable to destroy the Google Drive storage");
        }
    }

    private void createRoot() throws StorageException {

        try {
            root = new DriveDirectory(drive, rootPath);
            root.persist();
        } catch (PersistenceException e) {
            throw new StorageException(e);
        }
    }

}
