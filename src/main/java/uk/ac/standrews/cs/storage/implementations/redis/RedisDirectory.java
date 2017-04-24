package uk.ac.standrews.cs.storage.implementations.redis;

import redis.clients.jedis.Jedis;
import uk.ac.standrews.cs.storage.exceptions.BindingAbsentException;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.NameObjectBinding;
import uk.ac.standrews.cs.storage.interfaces.StatefulObject;

import java.util.Iterator;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class RedisDirectory extends RedisStatefulObject implements IDirectory {

    public RedisDirectory(Jedis jedis, String name) {
        super(jedis, name);
    }

    @Override
    public StatefulObject get(String name) throws BindingAbsentException {


        String type = jedis.get(name + ":type");
        // TODO - check existence
        if (type.equals("directory")) {

        } else {

        }
        return null;
    }

    @Override
    public boolean contains(String name) {
        return false;
    }

    @Override
    public void remove(String name) throws BindingAbsentException {

    }

    @Override
    public Iterator<NameObjectBinding> getIterator() {
        return null;
    }
}
