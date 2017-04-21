package uk.ac.standrews.cs.storage.implementations.redis;

import redis.clients.jedis.Jedis;
import uk.ac.standrews.cs.storage.exceptions.PersistenceException;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.StatefulObject;

import java.io.File;
import java.io.IOException;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class RedisStatefulObject implements StatefulObject {

    protected Jedis jedis;

    public RedisStatefulObject(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public IDirectory getParent() {
        return null;
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getPathname() {
        return null;
    }

    @Override
    public long lastModified() {
        return 0;
    }

    @Override
    public File toFile() throws IOException {
        return null;
    }

    @Override
    public void persist() throws PersistenceException {

    }

    @Override
    public long getSize() {
        return 0;
    }
}
