package uk.ac.standrews.cs.castore.implementations;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import uk.ac.standrews.cs.castore.CastoreBuilder;
import uk.ac.standrews.cs.castore.CastoreFactory;
import uk.ac.standrews.cs.castore.CastoreType;
import uk.ac.standrews.cs.castore.exceptions.DestroyException;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.interfaces.IStorage;

import java.lang.reflect.Method;

import static uk.ac.standrews.cs.castore.CastoreType.LOCAL;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public abstract class StorageBaseTest {

    private static final String ROOT_TEST_DIRECTORY = "/tmp/storage/";
    private static final String AWS_S3_TEST_BUCKET = "sos-simone-test";
    private static final String REDIS_HOST = "localhost";
    private static final String DROPBOX_PATH = "/Apps/castore";
    private static final String DRIVE_PATH = "test_folder";

    private static final int TEST_DELAY = 100; // Needed to allow any background ops (e.g. s3 needs some time to create buckets and so on)

    protected abstract CastoreType getStorageType();
    IStorage storage;

    @BeforeMethod
    public void setUp(Method method) throws StorageException {

        storage = makeStorage(); // Make it, but could have previous status from other tests
        storage.destroy();
        storage = makeStorage(); // Remake it clean

        System.out.println(getStorageType().toString() + " :: " + method.getName());
    }

    @AfterMethod
    public void tearDown() throws InterruptedException, DestroyException {
        storage.destroy();

        Thread.sleep(TEST_DELAY);
    }

    @DataProvider(name = "storage-manager-provider")
    public static Object[][] indexProvider() {
        return new Object[][] {
                {LOCAL},
                // {REDIS},
                // {DROPBOX},
                // {GOOGLE_DRIVE},
                // {AWS}
        };
    }

    IStorage makeStorage() throws StorageException {
        CastoreType type = getStorageType();
        CastoreBuilder builder = makeBuilder(type);
        return CastoreFactory.createStorage(builder);
    }

    private CastoreBuilder makeBuilder(CastoreType type) {

        CastoreBuilder builder = new CastoreBuilder().setType(type);

        switch(type) {
            case LOCAL:
                builder.setRoot(ROOT_TEST_DIRECTORY);
                break;
            case AWS_S3:
                builder.setRoot(AWS_S3_TEST_BUCKET);
                break;
            case REDIS:
                builder.setHostname(REDIS_HOST);
                break;
            case DROPBOX:
                builder.setRoot(DROPBOX_PATH);
                break;
            case GOOGLE_DRIVE:
                builder.setRoot(DRIVE_PATH);
                break;
        }

        return builder;
    }

}
