package uk.ac.standrews.cs.storage.examples;

import uk.ac.standrews.cs.storage.CastoreBuilder;
import uk.ac.standrews.cs.storage.CastoreFactory;
import uk.ac.standrews.cs.storage.CastoreType;
import uk.ac.standrews.cs.storage.data.StringData;
import uk.ac.standrews.cs.storage.exceptions.StorageException;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.IFile;
import uk.ac.standrews.cs.storage.interfaces.IStorage;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class Dropbox {

    /**
     * java Dropbox <APP TOKEN>
     *
     * @param args
     * @throws StorageException
     */
    public static void main(String[] args) throws StorageException {

        String token = args[0];

        CastoreBuilder builder = new CastoreBuilder()
                .setType(CastoreType.DROPBOX)
                .setToken(token)
                .setRoot("/Apps/castore");

        IStorage storage = CastoreFactory.createStorage(builder);

        IDirectory root = storage.getRoot();
        IFile file = storage.createFile(root, "test", new StringData("Example Data"));
        file.persist();

        System.out.println("dropbox example finished");

        IDirectory directory = storage.createDirectory("simone");
        directory.persist();
    }
}
