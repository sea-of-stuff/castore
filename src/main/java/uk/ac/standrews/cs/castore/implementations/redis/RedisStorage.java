package uk.ac.standrews.cs.castore.implementations.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import uk.ac.standrews.cs.castore.data.Data;
import uk.ac.standrews.cs.castore.exceptions.DestroyException;
import uk.ac.standrews.cs.castore.exceptions.PersistenceException;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.implementations.CommonStorage;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.IFile;
import uk.ac.standrews.cs.castore.interfaces.IStorage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class RedisStorage extends CommonStorage implements IStorage {

    private static Logger log = Logger.getLogger(RedisStorage.class.getName());

    private final static int DEFAULT_PORT = 6379;
    private final static String PING_RESPONSE = "PONG";

    private JedisPool pool;
    private String hostname;

    public RedisStorage(String hostname) throws StorageException {
        this(hostname, DEFAULT_PORT);
    }

    /**
     * Note: A Redis server instance should be available
     *
     * @param hostname
     */
    public RedisStorage(String hostname, int port) throws StorageException {
        this.hostname = hostname;
        pool = new JedisPool(new JedisPoolConfig(), hostname, port);

        try(Jedis jedis = pool.getResource()) {

            String response = jedis.ping();
            if (response.equals(PING_RESPONSE)) {
                createRoot(jedis);
            } else {
                log.log(Level.SEVERE, "Redis Storage could not be created because there is not an available or reachable Redis server");
            }

        }
    }

    @Override
    public IDirectory createDirectory(IDirectory parent, String name) throws StorageException {
        try(Jedis jedis = pool.getResource()) {
            return new RedisDirectory(jedis, parent, name);
        }
    }

    @Override
    public IDirectory createDirectory(String name) throws StorageException {
        return createDirectory(root, name);
    }

    @Override
    public IFile createFile(IDirectory parent, String filename) throws StorageException {
        try(Jedis jedis = pool.getResource()) {
            return new RedisFile(jedis, parent, filename);
        }
    }

    @Override
    public IFile createFile(IDirectory parent, String filename, Data data) throws StorageException {
        try(Jedis jedis = pool.getResource()) {
            return new RedisFile(jedis, parent, filename, data);
        }
    }

    @Override
    public void persist() {
        try(Jedis jedis = pool.getResource()) {
            jedis.bgsave();
        }
    }

    @Override
    public void destroy() throws DestroyException {

        root = null;

        try(Jedis jedis = pool.getResource()) {
            jedis.flushDB();
        }

        pool.destroy();
    }

    private void createRoot(Jedis jedis) throws StorageException {
        try {
            root = new RedisDirectory(jedis, hostname);
            root.persist();
        } catch (PersistenceException e) {
            throw new StorageException(e);
        }
    }
}
