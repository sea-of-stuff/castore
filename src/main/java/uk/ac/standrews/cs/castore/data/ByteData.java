/*
 * Created on May 23, 2005 at 10:51:17 AM.
 */
package uk.ac.standrews.cs.castore.data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

/**
 * IData implementation using a byte array.
 *
 * @author al
 */
public class ByteData extends BaseData  {
	
    private byte[] state;
    
    /**
     * Creates an instance using a given byte array.
     * 
     * @param state a byte array containing the underlying data
     */
    public ByteData(byte[] state) {
        this.state = Arrays.copyOf(state, state.length);
    }

    /**
     * Gets the data.
     *
     * @return the underlying data
     */
    public byte[] getState() {
        return Arrays.copyOf(state, state.length);
    }
    
    /**
     * Gets the size of the data in bytes.
     * 
     * @return the size of the data
     */
    public long getSize() {
        return state.length;
    }

    /**
     * Creates an input stream reading from the byte array.
     * 
     * @return an input stream reading from the byte array
     */
    public InputStream getInputStream() {
        return new ByteArrayInputStream(state);
    }

    public String toString() {
    	return new String(state);
    }

    @Override
    public void close() {}

}
