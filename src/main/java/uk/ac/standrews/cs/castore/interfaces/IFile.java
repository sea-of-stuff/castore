package uk.ac.standrews.cs.castore.interfaces;

import uk.ac.standrews.cs.castore.data.Data;
import uk.ac.standrews.cs.castore.exceptions.DataException;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public interface IFile extends StatefulObject {

    void setData(Data data) throws DataException;

    Data getData() throws DataException;
}
