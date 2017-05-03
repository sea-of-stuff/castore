package uk.ac.standrews.cs.storage.implementations.google.drive;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.exceptions.DestroyException;
import uk.ac.standrews.cs.storage.exceptions.StorageException;
import uk.ac.standrews.cs.storage.implementations.CommonStorage;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.IFile;
import uk.ac.standrews.cs.storage.interfaces.IStorage;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class DriveStorage extends CommonStorage implements IStorage {

    private Drive drive;

    public DriveStorage(String token) throws StorageException {

        try {
            GoogleCredential credential = new GoogleCredential().setAccessToken(token);
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

            drive = new Drive.Builder(httpTransport, JacksonFactory.getDefaultInstance(), credential)
                    .setApplicationName("castore/1.0")
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            throw new StorageException("Unable to create Google Drive storage object");
        }
    }

    @Override
    public IDirectory createDirectory(IDirectory parent, String name) throws StorageException {
        return null;
    }

    @Override
    public IDirectory createDirectory(String name) throws StorageException {
        return null;
    }

    @Override
    public IFile createFile(IDirectory parent, String filename) throws StorageException {
        return null;
    }

    @Override
    public IFile createFile(IDirectory parent, String filename, Data data) throws StorageException {
        return null;
    }

    @Override
    public void destroy() throws DestroyException {

        try {
            drive.files().delete("");
        } catch (IOException e) {
            throw new DestroyException("Unable to destroy the Google Drive storage");
        }
    }
}
