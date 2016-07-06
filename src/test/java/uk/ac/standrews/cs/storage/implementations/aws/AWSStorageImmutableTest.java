package uk.ac.standrews.cs.storage.implementations.aws;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.data.StringData;
import uk.ac.standrews.cs.storage.exceptions.DestroyException;
import uk.ac.standrews.cs.storage.exceptions.PersistenceException;
import uk.ac.standrews.cs.storage.exceptions.StorageException;
import uk.ac.standrews.cs.storage.interfaces.File;
import uk.ac.standrews.cs.storage.interfaces.IStorage;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class AWSStorageImmutableTest {

    private static final String AWS_S3_TEST_BUCKET = "sos-simone-test";
    private static final Data TEST_DATA = new StringData("hello world");

    private static final int TEST_DELAY = 1000; // Needed to allow any background ops

    private IStorage storage;

    @BeforeMethod
    public void setUp() throws StorageException {
        storage = new AWSStorage(AWS_S3_TEST_BUCKET, true);
    }

    @AfterMethod
    public void tearDown() throws DestroyException, InterruptedException {
        storage.destroy();

        Thread.sleep(TEST_DELAY);
    }

    @Test
    public void createFile() throws PersistenceException, IOException {
        File file = storage.createFile(storage.getTestDirectory(), "test-immutable.txt", TEST_DATA);
        file.persist();

        long lastModified = file.lastModified();

        File cloneFile = storage.createFile(storage.getTestDirectory(), "test-immutable.txt", TEST_DATA);
        cloneFile.persist();
        long lastModifiedClone = cloneFile.lastModified();

        assertEquals(lastModified, lastModifiedClone);
    }
}
