package uk.ac.standrews.cs.castore.examples;

import uk.ac.standrews.cs.castore.CastoreBuilder;
import uk.ac.standrews.cs.castore.CastoreFactory;
import uk.ac.standrews.cs.castore.CastoreType;
import uk.ac.standrews.cs.castore.data.StringData;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.IFile;
import uk.ac.standrews.cs.castore.interfaces.IStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class FileBased {

    public static void main(String[] args) throws IOException, StorageException {

        Path path = Files.createTempDirectory(null);

        CastoreBuilder builder = new CastoreBuilder()
                .setType(CastoreType.LOCAL)
                .setRoot(path.toString());

        IStorage storage = CastoreFactory.createStorage(builder);

        IDirectory root = storage.getRoot();
        IFile file = storage.createFile(root, "exampleFile");
        file.setData(new StringData("Example Data"));

        System.out.println("Just created a file named " + file.getName() + " at the following path " + file.getPathname());
    }
}
