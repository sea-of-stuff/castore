package uk.ac.standrews.cs.castore.implementations;

import org.testng.annotations.Test;
import uk.ac.standrews.cs.castore.CastoreBuilder;
import uk.ac.standrews.cs.castore.CastoreFactory;
import uk.ac.standrews.cs.castore.CastoreType;
import uk.ac.standrews.cs.castore.data.StringData;
import uk.ac.standrews.cs.castore.exceptions.RenameException;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.IFile;
import uk.ac.standrews.cs.castore.interfaces.IStorage;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class RenameTest {

    @Test
    public void dummyTest() throws StorageException, RenameException {

        IStorage storage = CastoreFactory.createStorage(
                new CastoreBuilder()
                        .setType(CastoreType.LOCAL)
                        .setRoot("."));

        IDirectory directory = storage.createDirectory("test_dir");
        IFile file = storage.createFile(directory, "test_file.txt");
        file.persist();

        file.rename("new_file.txt");

        storage.delete(file);
    }

    @Test
    public void dummyWithDataTest() throws StorageException, RenameException {

        IStorage storage = CastoreFactory.createStorage(
                new CastoreBuilder()
                        .setType(CastoreType.LOCAL)
                        .setRoot("."));

        IDirectory directory = storage.createDirectory("test_dir");
        IFile file = storage.createFile(directory, "test_file.txt", new StringData("aaaaa"));
        file.persist();

        file.rename("new_file.txt");

        storage.delete(file);
    }
}
