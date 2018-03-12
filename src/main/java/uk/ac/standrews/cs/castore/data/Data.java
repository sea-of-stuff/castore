package uk.ac.standrews.cs.castore.data;

import org.apache.commons.io.input.NullInputStream;

import java.io.Closeable;
import java.io.InputStream;

/**
 * Representation of an object's state.
 *
 * @author al, graham
 */
public interface Data extends Closeable {

    /**
     * Gets the object's state.
     *
     * @return the underlying data
     */
    byte[] getState();

    /**
     * Gets the size of the object in bytes.
     *
     * @return the size of the data
     */
    long getSize();

    /**
     * Creates an input stream reading from the object's state.
     *
     * @return an input stream reading from the object's state
     */
    default InputStream getInputStream() {
       return new NullInputStream(0);
    }

    /**
     * Tests equality with another instance.
     *
     * @param o the instance to be compared
     * @return true if the object's contents are equivalent to those of the given object
     * @see Object#equals(Object)
     */
    boolean equals(Object o);
}

