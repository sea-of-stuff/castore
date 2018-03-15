package uk.ac.standrews.cs.castore.interfaces;

import uk.ac.standrews.cs.castore.data.Data;
import uk.ac.standrews.cs.castore.exceptions.DataException;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public interface IFile extends StatefulObject {

    /**
     * Explicitly set the data for this file
     *
     * @param data for the file
     * @throws DataException if the data could not be set
     */
    void setData(Data data) throws DataException;

    /**
     * Get the data for this file
     *
     * @return the data of the file
     * @throws DataException if the data could not be retrieved
     */
    Data getData() throws DataException;

    /**
     *
     * @return
     * @throws IOException
     */
    OutputStream getOutputStream() throws IOException;
}
