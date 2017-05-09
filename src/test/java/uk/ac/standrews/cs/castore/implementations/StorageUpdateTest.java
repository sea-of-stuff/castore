package uk.ac.standrews.cs.castore.implementations;

import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import uk.ac.standrews.cs.castore.CastoreType;
import uk.ac.standrews.cs.castore.data.Data;
import uk.ac.standrews.cs.castore.data.StringData;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.interfaces.IFile;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class StorageUpdateTest extends StorageBaseTest {

    private static final Data TEST_DATA = new StringData("hello world");
    private static final Data TEST_UPDATE_DATA = new StringData("hello new world");
    private final CastoreType storageType;

    @Factory(dataProvider = "storage-manager-provider")
    public StorageUpdateTest(CastoreType storageType) {
        this.storageType = storageType;
    }

    @Override
    protected CastoreType getStorageType() {
        return storageType;
    }

    @Test
    public void basicUpdate() throws StorageException {
        IFile file = storage.createFile(storage.getRoot(), "0-update-test.txt", TEST_DATA);
        file.persist();
        assertTrue(file.exists());

        file.setData(TEST_UPDATE_DATA);
        file.persist();
        assertTrue(file.exists());

        IFile retrievedFile = storage.createFile(storage.getRoot(), "0-update-test.txt");
        assertEquals(retrievedFile.getData(), TEST_UPDATE_DATA);
    }
}
