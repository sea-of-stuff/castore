package uk.ac.standrews.cs.storage.implementations;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import uk.ac.standrews.cs.storage.CastoreType;
import uk.ac.standrews.cs.storage.exceptions.DestroyException;
import uk.ac.standrews.cs.storage.exceptions.StorageException;
import uk.ac.standrews.cs.storage.implementations.aws.s3.AWSStorage;
import uk.ac.standrews.cs.storage.implementations.dropbox.DropboxStorage;
import uk.ac.standrews.cs.storage.implementations.filesystem.FileBasedStorage;
import uk.ac.standrews.cs.storage.implementations.redis.RedisStorage;
import uk.ac.standrews.cs.storage.interfaces.IStorage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import static uk.ac.standrews.cs.storage.CastoreType.DROPBOX;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public abstract class StorageBaseTest {

    private static final String AWS_S3_TEST_BUCKET = "sos-simone-test";
    private static final File ROOT_TEST_DIRECTORY = new File("/tmp/storage/");

    private static final int TEST_DELAY = 800; // Needed to allow any background ops (e.g. s3 needs some time to create buckets and so on)

    protected abstract CastoreType getStorageType();
    protected IStorage storage;

    @BeforeMethod
    public void setUp(Method method) throws StorageException, InterruptedException {
        CastoreType type = getStorageType();
        System.out.println(type.toString() + " :: " + method.getName());
        storage = new StorageFactory().getStorage(type);
    }

    @AfterMethod
    public void tearDown() throws InterruptedException, DestroyException {
        storage.destroy();

        Thread.sleep(TEST_DELAY);
    }

    @DataProvider(name = "storage-manager-provider")
    public static Object[][] indexProvider() throws IOException {
        return new Object[][] {
//                {LOCAL},
//                {REDIS},
                {DROPBOX} /*, {AWS} */
        };
    }

    public class StorageFactory {

        public IStorage getStorage(CastoreType type) throws StorageException {
            switch(type) {
                case LOCAL:
                    return new FileBasedStorage(ROOT_TEST_DIRECTORY);
                case AWS_S3:
                    return new AWSStorage(AWS_S3_TEST_BUCKET);
                case REDIS:
                    return new RedisStorage("localhost");
                case DROPBOX:
                    return new DropboxStorage("/Apps/castore");
            }
            return null;
        }
    }
}
