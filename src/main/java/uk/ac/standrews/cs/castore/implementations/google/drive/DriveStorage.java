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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class DriveStorage extends CommonStorage implements IStorage {

    private static final Logger log = Logger.getLogger(DriveStorage.class.getName());

    private static final String DEFAULT_INDEX_PATH = "./drive.index";

    private Drive drive;
    private String rootPath;
    private Index index;

    public DriveStorage(java.io.File credentialFile, String path) throws StorageException {
        this.rootPath = path;
        loadOrCreateIndex(DEFAULT_INDEX_PATH);

        try {
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(credentialFile))
                    .createScoped(Collections.singleton(DriveScopes.DRIVE_FILE));

            drive = new Drive.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName("castore/1.0")
                    .build();

            createRoot();
        } catch (GeneralSecurityException | IOException e) {
            throw new StorageException("Unable to create Google Drive storage object", e);
        }
    }

    @Override
    public IDirectory createDirectory(IDirectory parent, String name) throws StorageException {
        return new DriveDirectory(drive, index, parent, name);
    }

    @Override
    public IDirectory createDirectory(String name) throws StorageException {
        return new DriveDirectory(drive, index, root, name);
    }

    @Override
    public IFile createFile(IDirectory parent, String filename) throws StorageException {
        return new DriveFile(drive, index, parent, filename);
    }

    @Override
    public IFile createFile(IDirectory parent, String filename, Data data) throws StorageException {
        return new DriveFile(drive, index, parent, filename, data);
    }

    @Override
    public void persist() {
        try {
            index.persist(DEFAULT_INDEX_PATH);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Unable to persist index for Drive Storage");
        }
    }

    @Override
    public void destroy() throws DestroyException {

        root = null;

        try {
            String token;
            FileList contents = drive.files().list().execute();
            while(true) {

                for (File file : contents.getFiles()) {
                    log.log(Level.INFO, "Deleting file/folder with name " + file.getName() + " with id " + file.getId());
                    drive.files().delete(file.getId()).execute();
                }

                token = contents.getNextPageToken();
                if (token == null) break;

                contents = drive.files().list().setPageToken(token).execute();
            }

        } catch (IOException e) {
            throw new DestroyException("Unable to destroy the Google Drive storage");
        }

        index.empty();
        index.delete(DEFAULT_INDEX_PATH);
    }

    private void createRoot() throws StorageException {

        try {
            root = new DriveDirectory(drive, index, rootPath);
            root.persist();
        } catch (PersistenceException e) {
            throw new StorageException(e);
        }
    }

    private void loadOrCreateIndex(String path) {
        try {
            java.io.File file = new java.io.File(path);
            if (file.exists()) {
                index = Index.load(file);
            }
        } catch (ClassNotFoundException | IOException e) {
            log.log(Level.WARNING, "Unable to load index");
        }

        if (index == null) {
            index = new Index();
        }
    }

}
