package uk.ac.standrews.cs.storage.interfaces;

import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.exceptions.DataException;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public interface File extends StatefulObject {

    void setData(Data data) throws DataException;

    Data getData() throws DataException;
}
