package uk.ac.standrews.cs.storage.examples;

import uk.ac.standrews.cs.storage.CastoreBuilder;
import uk.ac.standrews.cs.storage.CastoreFactory;
import uk.ac.standrews.cs.storage.CastoreType;
import uk.ac.standrews.cs.storage.data.StringData;
import uk.ac.standrews.cs.storage.exceptions.StorageException;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.IFile;
import uk.ac.standrews.cs.storage.interfaces.IStorage;

import java.io.IOException;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class AWSS3 {

    public static void main(String[] args) throws IOException, StorageException {

        CastoreBuilder builder = new CastoreBuilder()
                .setType(CastoreType.AWS_S3)
                .setRoot("test-bucket");

        IStorage storage = CastoreFactory.createStorage(builder);

        IDirectory root = storage.getRoot();
        IFile file = storage.createFile(root, "exampleFile");
        file.setData(new StringData("Example Data"));

        file.persist();

        System.out.println("Just created a file named " + file.getName() + " at the following path " + file.getPathname());
    }

}

