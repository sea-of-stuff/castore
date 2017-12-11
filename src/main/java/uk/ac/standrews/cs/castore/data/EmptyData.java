package uk.ac.standrews.cs.castore.data;

import java.io.IOException;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class EmptyData implements Data {

    @Override
    public byte[] getState() {
        return new byte[0];
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public void close() throws IOException {

    }
}
