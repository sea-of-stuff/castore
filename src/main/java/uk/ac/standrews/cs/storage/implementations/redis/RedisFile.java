package uk.ac.standrews.cs.storage.implementations.redis;

import redis.clients.jedis.Jedis;
import uk.ac.standrews.cs.GUIDFactory;
import uk.ac.standrews.cs.IGUID;
import uk.ac.standrews.cs.exceptions.GUIDGenerationException;
import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.data.StringData;
import uk.ac.standrews.cs.storage.exceptions.DataException;
import uk.ac.standrews.cs.storage.exceptions.PersistenceException;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.IFile;
import uk.ac.standrews.cs.storage.utils.IO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class RedisFile extends RedisStatefulObject implements IFile {

    public RedisFile(Jedis jedis, IDirectory parent, String name) {
        super(jedis, parent, name);

        if (exists()) retrieveAndUpdateData();
    }

    private void retrieveAndUpdateData() {

        String guid = jedis.get(objectPath);
        data = new StringData(jedis.get(guid));
    }

    public RedisFile(Jedis jedis, IDirectory parent, String name, Data data) {
        super(jedis, parent, name);

        this.data = data;
    }

    @Override
    public boolean exists() {
        return jedis.exists(getPathname());
    }

    @Override
    public String getPathname() {
        return logicalParent.getPathname() + name;
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

            String retrievedGUID = jedis.get(objectPath);
            if (!guid.toString().equals(retrievedGUID)) {

                jedis.set(objectPath, guid.toString());
                jedis.set(objectPath + REDIS_KEY_TYPE_TAG, FILE_TYPE);

                boolean exists = jedis.exists(guid.toString());
                if (!exists) {
                    String dataString = IO.InputStreamToString(dataSecondClone);

                    jedis.set(guid.toString(), dataString);
                    jedis.set(guid.toString() + REDIS_REF_TAG, "1");
                } else {
                    jedis.incr(guid.toString() + REDIS_REF_TAG);
                }

                // Add element to parent
                jedis.sadd(logicalParent.getPathname(), name);
            }

        } catch (IOException | GUIDGenerationException e) {
            throw new PersistenceException("Unable to persist data to Redis storage");
        }

        // Make sure that the parent path is persisted
        if (logicalParent != null) {
            logicalParent.persist();
        }

    }
}
