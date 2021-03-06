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
public class AWSS3 {

    public static void main(String[] args) throws StorageException {

        CastoreBuilder builder = new CastoreBuilder()
                .setType(CastoreType.AWS_S3)
                .setRoot("test-bucket");

        IStorage storage = CastoreFactory.createStorage(builder);

        IDirectory root = storage.getRoot();
        IFile file = storage.createFile(root, "exampleFile");
        file.setData(new StringData("Example Data"));

        file.persist();

        System.out.println("Just created a file named " + file.getName() + " at the following path " + file.getPathname());
        System.out.println("Directory path: " + root.getPath());
        System.out.println("File path: " + file.getPath());
    }

}

