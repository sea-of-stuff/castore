package uk.ac.standrews.cs.storage;

import uk.ac.standrews.cs.storage.exceptions.StorageException;
import uk.ac.standrews.cs.storage.implementations.aws.s3.AWSStorage;
import uk.ac.standrews.cs.storage.implementations.filesystem.FileBasedStorage;
import uk.ac.standrews.cs.storage.implementations.network.NetworkBasedStorage;
import uk.ac.standrews.cs.storage.implementations.redis.RedisStorage;
import uk.ac.standrews.cs.storage.interfaces.IStorage;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class StorageFactory {

    private static final Logger log = Logger.getLogger( StorageFactory.class.getName() );

    /**
     * @param builder
     * @return
     * @throws StorageException
     */
    public static IStorage createStorage(StorageBuilder builder) throws StorageException {
        IStorage storage;

        switch(builder.type) {
            case LOCAL:
                storage = new FileBasedStorage(new File(builder.root));
                break;
            case NETWORK:
                storage = new NetworkBasedStorage(builder.mountPoint, builder.root);
                break;
            case AWS_S3:
                storage = new AWSStorage(builder.root);
                break;
            case REDIS:
                storage = new RedisStorage(builder.hostname, builder.port);
                break;
            default:
                log.log(Level.SEVERE, "Storage type: " + builder.type + " is unknown. Impossible to create a storage.");
                throw new StorageException();
        }

        return storage;
    }

}
