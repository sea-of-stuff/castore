package uk.ac.standrews.cs.storage.examples;

import uk.ac.standrews.cs.storage.CastoreBuilder;
import uk.ac.standrews.cs.storage.CastoreFactory;
import uk.ac.standrews.cs.storage.CastoreType;
import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.data.StringData;
import uk.ac.standrews.cs.storage.exceptions.StorageException;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.IFile;
import uk.ac.standrews.cs.storage.interfaces.IStorage;
import uk.ac.standrews.cs.storage.interfaces.NameObjectBinding;

import java.util.Iterator;

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
                .setRoot("/Apps/castore"); // This is the APP folder created at the time the Dropbox app was created

        IStorage storage = CastoreFactory.createStorage(builder);

        IDirectory root = storage.getRoot();
        IFile file = storage.createFile(root, "test", new StringData("Example Data"));
        file.persist();

        IDirectory directory = storage.createDirectory("simone");
        directory.persist();

        IDirectory dir = (IDirectory) root.get("simone");
        System.out.println(dir.getPathname());

        Iterator<NameObjectBinding> it = root.getIterator();
        while(it.hasNext()) {
            NameObjectBinding obj = it.next();
            System.out.println(obj.getName());
            System.out.println(obj.getObject().getSize());

            if (obj.getObject() instanceof IFile) {
                Data data = ((IFile) obj.getObject()).getData();
                System.out.println("Data " + data.toString());
            }
        }

    }
}
