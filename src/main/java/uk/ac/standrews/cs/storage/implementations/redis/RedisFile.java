package uk.ac.standrews.cs.storage.implementations.redis;

import redis.clients.jedis.Jedis;
import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.exceptions.DataException;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.IFile;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class RedisFile extends RedisStatefulObject implements IFile {

    public RedisFile(Jedis jedis, IDirectory parent, String name) {
        super(jedis, parent, name);
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
}
