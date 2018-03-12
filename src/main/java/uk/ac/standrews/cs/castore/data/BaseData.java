package uk.ac.standrews.cs.castore.data;

import java.util.Arrays;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public abstract class BaseData implements Data {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Data)) return false;
        Data data = (Data) o;
        return Arrays.equals(getState(), data.getState());
    }

    @Override
    public int hashCode() {

        return Arrays.hashCode(getState());
    }
}
