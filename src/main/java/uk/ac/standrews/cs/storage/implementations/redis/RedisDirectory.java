package uk.ac.standrews.cs.storage.implementations.redis;

import redis.clients.jedis.Jedis;
import uk.ac.standrews.cs.storage.exceptions.BindingAbsentException;
import uk.ac.standrews.cs.storage.exceptions.PersistenceException;
import uk.ac.standrews.cs.storage.implementations.NameObjectBindingImpl;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.NameObjectBinding;
import uk.ac.standrews.cs.storage.interfaces.StatefulObject;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import static uk.ac.standrews.cs.storage.CastoreConstants.FOLDER_DELIMITER;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class RedisDirectory extends RedisStatefulObject implements IDirectory {

    private static final Logger log = Logger.getLogger(RedisDirectory.class.getName());

    public RedisDirectory(Jedis jedis, String name) {
        super(jedis, name);
    }

    public RedisDirectory(Jedis jedis, IDirectory parent, String name) {
        super(jedis, parent, name);
    }

    @Override
    public StatefulObject get(String name) throws BindingAbsentException {

        boolean hasObject = jedis.exists(objectPath + name + REDIS_KEY_TYPE_TAG);
        if (hasObject) {

            String type = jedis.get(objectPath + name + REDIS_KEY_TYPE_TAG);
            switch (type) {
                case DIRECTORY_TYPE:
                    return new RedisDirectory(jedis, this, name);
                case FILE_TYPE:
                    return new RedisFile(jedis, this, name);
                default:
                    throw new BindingAbsentException("Type for stateful object unknown. Cannot create a proper object");
            }

        } else {
            throw new BindingAbsentException("Object with name " + name + " was not found");
        }

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
    public void persist() throws PersistenceException {
        jedis.set(objectPath + REDIS_KEY_TYPE_TAG, DIRECTORY_TYPE);

        if (logicalParent != null) {
            jedis.sadd(logicalParent.getPathname(), name + "/");
        }

        if (logicalParent != null) {
            logicalParent.persist();
        } else {
            jedis.set("" + REDIS_KEY_TYPE_TAG, DIRECTORY_TYPE);
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
        return jedis.sismember(getPathname(), name);
    }

    @Override
    public void remove(String name) throws BindingAbsentException {

        if (contains(name)) {
            jedis.srem(objectPath, name);
        } else {
            throw new BindingAbsentException("Element with name " + name + " does not exist and thus it cannot be removed");
        }
    }

    @Override
    public Iterator<NameObjectBinding> getIterator() {
        return new DirectoryIterator();
    }

    private class DirectoryIterator implements Iterator<NameObjectBinding>  {

        Iterator<String> elements;

        DirectoryIterator() {
            elements = jedis.smembers(objectPath).iterator();
        }

        @Override
        public boolean hasNext() {
            return elements.hasNext();
        }

        @Override
        public NameObjectBinding next() {

            if (!hasNext()) {
                return null;
            }

            try {
                String name = elements.next();
                StatefulObject obj = get(name);

                return new NameObjectBindingImpl(name, obj);
            } catch (BindingAbsentException e) {
                log.log(Level.SEVERE, "Unable to create a Binding Object for the next element from the iterator");
            }

            return null;
        }
    }
}
