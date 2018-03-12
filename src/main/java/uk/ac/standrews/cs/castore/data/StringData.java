package uk.ac.standrews.cs.castore.data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

/**
 * IData implementation using a string.
 *
 * @author al
 */
public class StringData extends BaseData {

    private String state;

    /**
     * Creates an instance using a given string.
     *
     * @param state a string containing the underlying data
     */
    public StringData(String state) {
        this.state = state;
    }

    /**
     * Gets the data.
     *
     * @return the underlying data
     */
    public byte[] getState() {
        return Arrays.copyOf(state.getBytes(), state.getBytes().length);
    }

    /**
     * Gets the size of the data in bytes.
     *
     * @return the size of the data
     */
    public long getSize() {
        return state.length();
    }

    /**
     * Creates an input stream reading from the string.
     *
     * @return an input stream reading from the string
     */
    public InputStream getInputStream() {
        return new ByteArrayInputStream(state.getBytes());
    }

    public String toString() {
        return state;
    }

    @Override
    public void close() {}
}

