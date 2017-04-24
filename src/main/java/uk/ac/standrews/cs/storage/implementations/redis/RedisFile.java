package uk.ac.standrews.cs.storage.implementations.redis;

import redis.clients.jedis.Jedis;
import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.exceptions.DataException;
import uk.ac.standrews.cs.storage.interfaces.IFile;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class RedisFile extends RedisStatefulObject implements IFile {


    public RedisFile(Jedis jedis, String name) {
        super(jedis, name);
    }

    @Override
    public void setData(Data data) throws DataException {
        this.data = data;
    }

    @Override
    public Data getData() throws DataException {
        return data;
    }
}
