/*
 * Created on May 23, 2005 at 10:51:17 AM.
 */
package uk.ac.standrews.cs.castore.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * IData implementation using a byte array.
 *
 * @author al
 */
public class ByteData implements Data {
	
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
        return state;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ByteData byteData = (ByteData) o;
        return Arrays.equals(state, byteData.state);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(state);
    }

    public String toString() {
    	return new String(state);
    }

    @Override
    public void close() throws IOException {

    }
}
