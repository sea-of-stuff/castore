package uk.ac.standrews.cs.castore.interfaces;

import uk.ac.standrews.cs.castore.data.Data;
import uk.ac.standrews.cs.castore.exceptions.DataException;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public interface IFile extends StatefulObject {

    /**
     * Explicitly set the data for this file
     *
     * @param data
     * @throws DataException
     */
    void setData(Data data) throws DataException;

    /**
     * Get the data for this file
     *
     * @return
     * @throws DataException
     */
    Data getData() throws DataException;
}
