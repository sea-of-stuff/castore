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
public class Dropbox {

    /**
     * java Dropbox <APP ACCESS TOKEN>
     *
     * @param args the app access token
     * @throws StorageException if the Dropbox Storage could not be created or another error occurred
     */
    public static void main(String[] args) throws StorageException {

        String token = args[0];

        CastoreBuilder builder = new CastoreBuilder()
                .setType(CastoreType.DROPBOX)
                .setToken(token)
                .setRoot("/Apps/castore"); // This is the APP folder created at the time the Dropbox app was created

        IStorage storage = CastoreFactory.createStorage(builder);

        IDirectory root = storage.getRoot();
        IFile file = storage.createFile(root, "exampleFile");
        file.setData(new StringData("Example Data"));

        file.persist();

        System.out.println("Just created a file named " + file.getName() + " at the following path " + file.getPathname());

        IDirectory directory = storage.createDirectory("exampleDir");
        directory.persist();

        directory.lastModified();
        System.out.println("Just created a dir");
        System.out.println("Directory path: " + root.getPath());
        System.out.println("File path: " + file.getPath());
    }
}
