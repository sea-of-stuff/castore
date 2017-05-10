package uk.ac.standrews.cs.castore.examples;

import uk.ac.standrews.cs.castore.CastoreBuilder;
import uk.ac.standrews.cs.castore.CastoreFactory;
import uk.ac.standrews.cs.castore.CastoreType;
import uk.ac.standrews.cs.castore.data.StringData;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.IFile;
import uk.ac.standrews.cs.castore.interfaces.IStorage;

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

        IFile file = storage.createFile(storage.getRoot(), "test_file", new StringData("Hello World"));
        file.persist();
        System.out.println("Exists : " + file.exists());

        IFile retrieved = storage.createFile(storage.getRoot(), "test_file");

        System.out.println("lmd " + retrieved.lastModified());
        System.out.println("size " + retrieved.getSize());

        IDirectory directory = storage.createDirectory("folder");
        directory.persist();

        storage.persist();

        storage.destroy();
    }
}
