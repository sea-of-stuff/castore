package uk.ac.standrews.cs.storage.examples;

import uk.ac.standrews.cs.storage.CastoreBuilder;
import uk.ac.standrews.cs.storage.CastoreFactory;
import uk.ac.standrews.cs.storage.CastoreType;
import uk.ac.standrews.cs.storage.data.StringData;
import uk.ac.standrews.cs.storage.exceptions.StorageException;
import uk.ac.standrews.cs.storage.interfaces.IFile;
import uk.ac.standrews.cs.storage.interfaces.IStorage;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class Drive {

    /**
     * java Dropbox <APP TOKEN>
     *
     * @param args
     * @throws StorageException
     */
    public static void main(String[] args) throws StorageException {

        CastoreBuilder builder = new CastoreBuilder()
                .setType(CastoreType.GOOGLE_DRIVE)
                .setCredentialsPath("src/main/resources/drive.json")
                .setRoot("test_folder"); // This is the APP folder created at the time the Dropbox app was created

        IStorage storage = CastoreFactory.createStorage(builder);

        IFile file = storage.createFile(storage.getRoot(), "test file", new StringData("Hello World"));
        file.persist();
        file.exists();

        IFile retrieved = storage.createFile(storage.getRoot(), "test file");

        storage.destroy();
    }
}
