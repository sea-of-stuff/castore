package uk.ac.standrews.cs.storage.implementations.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.exceptions.DestroyException;
import uk.ac.standrews.cs.storage.exceptions.StorageException;
import uk.ac.standrews.cs.storage.implementations.CommonStorage;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.IFile;
import uk.ac.standrews.cs.storage.interfaces.IStorage;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class RedisStorage extends CommonStorage implements IStorage {

    private JedisPool pool;

    /**
     * You should make sure that a redis instance is available
     *
     * @param hostname
     */
    public RedisStorage(String hostname, int port) {
        pool = new JedisPool(new JedisPoolConfig(), hostname, port);

        try(Jedis jedis = pool.getResource()) {
            String response = jedis.ping();
            // TODO - check if redis instance is on
        }
    }

    @Override
    public IDirectory createDirectory(IDirectory parent, String name) throws StorageException {
        return null;
    }

    @Override
    public IDirectory createDirectory(String name) throws StorageException {
        return null;
    }

    @Override
    public IFile createFile(IDirectory parent, String filename) throws StorageException {
        return null;
    }

    @Override
    public IFile createFile(IDirectory parent, String filename, Data data) throws StorageException {
        return null;
    }

    @Override
    public void destroy() throws DestroyException {
        pool.destroy();
    }
}
