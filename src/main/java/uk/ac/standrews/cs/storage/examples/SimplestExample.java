package uk.ac.standrews.cs.storage.examples;

import uk.ac.standrews.cs.storage.StorageFactory;
import uk.ac.standrews.cs.storage.StorageType;
import uk.ac.standrews.cs.storage.data.StringData;
import uk.ac.standrews.cs.storage.exceptions.StorageException;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.IFile;
import uk.ac.standrews.cs.storage.interfaces.IStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class SimplestExample {

    public static void main(String[] args) throws IOException, StorageException {

        Path path = Files.createTempDirectory(null);
        IStorage storage = StorageFactory.createStorage(StorageType.LOCAL, path.toString());

        IDirectory root = storage.getRoot();
        IFile file = storage.createFile(root, "exampleFile");
        file.setData(new StringData("Example Data"));

        System.out.println("Just created a file named " + file.getName() + " at the following path " + file.getPathname());
    }
}
