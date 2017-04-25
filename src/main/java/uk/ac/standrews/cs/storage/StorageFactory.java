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

import static uk.ac.standrews.cs.storage.StorageBuilder.NOT_SET;

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

        switch(builder.getType()) {
            case LOCAL:
                storage = new FileBasedStorage(new File(builder.getRoot()));
                break;
            case NETWORK:
                storage = new NetworkBasedStorage(builder.getMountPoint(), builder.getRoot());
                break;
            case AWS_S3:
                storage = new AWSStorage(builder.getRoot());
                break;
            case REDIS:
                int port = builder.getPort();
                if (port == NOT_SET) {
                    storage = new RedisStorage(builder.getHostname());
                } else {
                    storage = new RedisStorage(builder.getHostname(), builder.getPort());
                }
                break;
            default:
                log.log(Level.SEVERE, "Storage type: " + builder.getType() + " is unknown. Impossible to create a storage.");
                throw new StorageException();
        }

        return storage;
    }

}
