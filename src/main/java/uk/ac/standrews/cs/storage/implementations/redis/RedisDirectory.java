package uk.ac.standrews.cs.storage.implementations.redis;

import redis.clients.jedis.Jedis;
import uk.ac.standrews.cs.storage.exceptions.BindingAbsentException;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.NameObjectBinding;
import uk.ac.standrews.cs.storage.interfaces.StatefulObject;

import java.util.Iterator;

import static uk.ac.standrews.cs.storage.CastoreConstants.FOLDER_DELIMITER;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class RedisDirectory extends RedisStatefulObject implements IDirectory {

    public RedisDirectory(Jedis jedis, String name) {
        super(jedis, name);
    }

    public RedisDirectory(Jedis jedis, IDirectory parent, String name) {
        super(jedis, parent, name);
    }

    @Override
    public StatefulObject get(String name) throws BindingAbsentException {

        boolean hasObject = jedis.exists(name + ":type");
        if (hasObject) {
            String type = jedis.get(name + ":type");
            if (type.equals("directory")) {

            } else {

            }
        } else {
            throw new BindingAbsentException("Object with name " + name + " was not found");
        }

        return null;
    }

    @Override
    public String getPathname() {
        if (logicalParent == null) {
            return  "";
        } else if (name == null || name.isEmpty()) {
            return logicalParent.getPathname() + FOLDER_DELIMITER;
        } else {
            return logicalParent.getPathname() + name + FOLDER_DELIMITER;
        }
    }

    @Override
    public boolean exists() {
        // TODO - check it has files/folders
        return false;
    }

    @Override
    public long getSize() {
        return 0;
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
