package uk.ac.standrews.cs.castore.implementations.redis;

import redis.clients.jedis.Jedis;
import uk.ac.standrews.cs.castore.data.Data;
import uk.ac.standrews.cs.castore.data.StringData;
import uk.ac.standrews.cs.castore.exceptions.DataException;
import uk.ac.standrews.cs.castore.exceptions.PersistenceException;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.IFile;
import uk.ac.standrews.cs.castore.utils.IO;
import uk.ac.standrews.cs.guid.GUIDFactory;
import uk.ac.standrews.cs.guid.IGUID;
import uk.ac.standrews.cs.guid.exceptions.GUIDGenerationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class RedisFile extends RedisStatefulObject implements IFile {

    RedisFile(Jedis jedis, IDirectory parent, String name) throws StorageException {
        super(jedis, parent, name);

        if (exists()) retrieveAndUpdateData();
    }

    RedisFile(Jedis jedis, IDirectory parent, String name, Data data) throws StorageException {
        super(jedis, parent, name);

        this.data = data;
    }

    @Override
    public String getPathname() {
        return parent.getPathname() + name;
    }

    @Override
    public void setData(Data data) throws DataException {
        this.data = data;
    }

    @Override
    public Data getData() throws DataException {
        return data;
    }

    @Override
    public long getSize() {
        return data.getSize();
    }

    /**
     * path --> guid
     * path:type --> FILE_TYPE
     * guid --> data
     * parent_path --> [name]
     *
     * This will give us de-duplication over data
     *
     * @throws PersistenceException
     */
    @Override
    public void persist() throws PersistenceException {

        try (final InputStream inputStream = getInputStream();
             final ByteArrayOutputStream baos = IO.InputStreamToByteArrayOutputStream(inputStream);
             InputStream dataFirstClone = new ByteArrayInputStream(baos.toByteArray());
             InputStream dataSecondClone = new ByteArrayInputStream(baos.toByteArray())) {

            IGUID guid = GUIDFactory.generateGUID(dataFirstClone);

            // Check if first time adding the data or if data has changed, thus it is an update
            String retrievedGUID = jedis.get(objectPath);
            if (retrievedGUID == null || !guid.toString().equals(retrievedGUID)) {

                jedis.set(objectPath, guid.toString());
                jedis.set(objectPath + REDIS_KEY_TYPE_TAG, FILE_TYPE);

                // Check if the data is already stored or not
                boolean exists = jedis.exists(guid.toString());
                if (!exists) {
                    String dataString = IO.InputStreamToString(dataSecondClone);

                    jedis.set(guid.toString(), dataString);
                    jedis.set(guid.toString() + REDIS_REF_TAG, "1");
                } else {
                    jedis.incr(guid.toString() + REDIS_REF_TAG);
                }

                decreaseDataRef(retrievedGUID);

                // Add element to parent
                jedis.sadd(parent.getPathname(), name);
            }

        } catch (IOException | GUIDGenerationException e) {
            throw new PersistenceException("Unable to persist data to Redis storage");
        }

        // Make sure that the parent path is persisted
        if (parent != null) {
            parent.persist();
        }

    }

    private void retrieveAndUpdateData() {

        String guid = jedis.get(objectPath);
        data = new StringData(jedis.get(guid));
    }
}
