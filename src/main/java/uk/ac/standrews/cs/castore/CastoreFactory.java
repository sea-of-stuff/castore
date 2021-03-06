package uk.ac.standrews.cs.castore;

import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.implementations.aws.s3.AWSStorage;
import uk.ac.standrews.cs.castore.implementations.dropbox.DropboxStorage;
import uk.ac.standrews.cs.castore.implementations.filesystem.FileBasedStorage;
import uk.ac.standrews.cs.castore.implementations.google.drive.DriveStorage;
import uk.ac.standrews.cs.castore.implementations.redis.RedisStorage;
import uk.ac.standrews.cs.castore.interfaces.IStorage;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import static uk.ac.standrews.cs.castore.CastoreBuilder.NOT_SET;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class CastoreFactory {

    private static final Logger log = Logger.getLogger(CastoreFactory.class.getName());

    /**
     * @param builder containing all the necessary info to create a storage
     * @return the storage
     * @throws StorageException if the storage object could not be created
     */
    public static IStorage createStorage(CastoreBuilder builder) throws StorageException {
        IStorage storage;

        switch(builder.getType()) {

            case LOCAL:

                storage = new FileBasedStorage(new File(builder.getRoot()));
                break;

            case AWS_S3:

                String accessKey = builder.getAccessKey();
                String secretAccessKey = builder.getSecretAccessKey();

                if (accessKey != null && secretAccessKey != null) {
                    storage = new AWSStorage(accessKey, secretAccessKey, builder.getRoot());
                } else {
                    storage = new AWSStorage(builder.getRoot());
                }

                break;

            case REDIS:

                int port = builder.getPort();
                if (port == NOT_SET) {
                    storage = new RedisStorage(builder.getHostname());
                } else {
                    storage = new RedisStorage(builder.getHostname(), builder.getPort());
                }

                break;

            case DROPBOX:

            {
                String token = builder.getToken();
                if (token == null || token.isEmpty()) {
                    storage = new DropboxStorage(builder.getRoot());
                } else {
                    storage = new DropboxStorage(builder.getToken(), builder.getRoot());
                }
            }

                break;

            case GOOGLE_DRIVE:

            {
                String credentialsPath = builder.getCredentialsPath();
                if (credentialsPath == null || credentialsPath.isEmpty()) {
                    storage = new DriveStorage(builder.getRoot());
                } else {
                    storage = new DriveStorage(builder.getCredentialsPath(), builder.getRoot());
                }
            }

                break;

            default:
                log.log(Level.SEVERE, "Storage type: " + builder.getType() + " is unknown. Impossible to create a storage.");
                throw new StorageException();
        }

        return storage;
    }

}
