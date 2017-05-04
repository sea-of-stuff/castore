package uk.ac.standrews.cs.storage.implementations.redis;

import com.amazonaws.util.StringInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.NullInputStream;
import redis.clients.jedis.Jedis;
import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.exceptions.StorageException;
import uk.ac.standrews.cs.storage.implementations.CommonStatefulObject;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.StatefulObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public abstract class RedisStatefulObject extends CommonStatefulObject implements StatefulObject {

    private static final String TMP_FILE_SUFFIX = ".tmp";
    private static final String TMP_FILE_PREFIX = "redis";

    protected static final String REDIS_KEY_TYPE_TAG = ":type";
    protected static final String FILE_TYPE = "file";
    protected static final String DIRECTORY_TYPE = "directory";
    protected static final String REDIS_REF_TAG = ":ref";

    protected Jedis jedis;

    protected IDirectory logicalParent;
    protected String objectPath;
    protected Data data;

    public RedisStatefulObject(Jedis jedis, IDirectory parent, String name) throws StorageException {
        super(name);

        this.jedis = jedis;
        this.logicalParent = parent;
        this.objectPath = getPathname();
    }

    public RedisStatefulObject(Jedis jedis, String name) throws StorageException {
        super(name);

        this.jedis = jedis;
        this.objectPath = getPathname();
    }

    @Override
    public IDirectory getLogicalParent() {
        return logicalParent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean exists() {
        return jedis.exists(getPathname());
    }

    @Override
    public long lastModified() {
        // TODO - path:lmd --> value
        return 0;
    }

    @Override
    public File toFile() throws IOException {

        final File tempFile = File.createTempFile(TMP_FILE_PREFIX, TMP_FILE_SUFFIX);
        tempFile.deleteOnExit();

        try (FileOutputStream output = new FileOutputStream(tempFile);
             InputStream input = new StringInputStream(jedis.get(objectPath));) {

            IOUtils.copy(input, output);
        }

        return tempFile;
    }



    protected InputStream getInputStream() throws IOException {
        return data != null ? data.getInputStream() : new NullInputStream(0);
    }

}
