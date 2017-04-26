package uk.ac.standrews.cs.storage.implementations.dropbox;

import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.exceptions.DataException;
import uk.ac.standrews.cs.storage.interfaces.IFile;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class DropboxFile extends DropboxStatefulObject implements IFile {

    @Override
    public void setData(Data data) throws DataException {

    }

    @Override
    public Data getData() throws DataException {
        return null;
    }
}
