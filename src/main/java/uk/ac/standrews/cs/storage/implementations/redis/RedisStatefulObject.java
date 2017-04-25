package uk.ac.standrews.cs.storage.implementations.redis;

import com.amazonaws.util.StringInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.NullInputStream;
import redis.clients.jedis.Jedis;
import uk.ac.standrews.cs.GUIDFactory;
import uk.ac.standrews.cs.IGUID;
import uk.ac.standrews.cs.exceptions.GUIDGenerationException;
import uk.ac.standrews.cs.storage.CommonStatefulObject;
import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.exceptions.PersistenceException;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.StatefulObject;
import uk.ac.standrews.cs.storage.utils.IO;

import java.io.*;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public abstract class RedisStatefulObject extends CommonStatefulObject implements StatefulObject {

    private static final String TMP_FILE_PREFIX = "redis";
    private static final String TMP_FILE_SUFFIX = ".tmp";

    protected Jedis jedis;

    protected IDirectory logicalParent;
    protected String name;
    protected String objectPath;
    protected Data data;

    public RedisStatefulObject(Jedis jedis, IDirectory parent, String name) {
        this.jedis = jedis;
        this.logicalParent = parent;
        this.name = name;
        this.objectPath = getPathname();
    }

    public RedisStatefulObject(Jedis jedis, String name) {
        this.jedis = jedis;
        this.name = name;
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
    public long lastModified() {
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

    @Override
    public void persist() throws PersistenceException {

        try (final InputStream inputStream = getInputStream();
             final ByteArrayOutputStream baos = IO.InputStreamToByteArrayOutputStream(inputStream);
             InputStream dataFirstClone = new ByteArrayInputStream(baos.toByteArray());
             InputStream dataSecondClone = new ByteArrayInputStream(baos.toByteArray())) {

            String objectPath = getPathname();
            IGUID guid = GUIDFactory.generateGUID(dataFirstClone);

            jedis.set(objectPath, guid.toString());
            boolean exists = jedis.exists(guid.toString());
            if (!exists) {
                String dataString = IO.InputStreamToString(dataSecondClone);
                jedis.set(guid.toString(), dataString);
            }

        } catch (IOException | GUIDGenerationException e) {
            throw new PersistenceException("Unable to persist data to Redis storage");
        }
    }

    private InputStream getInputStream() throws IOException {
        return data != null ? data.getInputStream() : new NullInputStream(0);
    }

}
