package uk.ac.standrews.cs.castore.implementations.google.drive;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.apache.commons.io.input.NullInputStream;
import uk.ac.standrews.cs.castore.data.Data;
import uk.ac.standrews.cs.castore.data.InputStreamData;
import uk.ac.standrews.cs.castore.exceptions.DataException;
import uk.ac.standrews.cs.castore.exceptions.PersistenceException;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.IFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class DriveFile extends DriveStatefulObject implements IFile {

    protected Data data;

    public DriveFile(Drive drive, HashMap<String, String> index, IDirectory parent, String name) throws StorageException {
        super(drive, index, parent, name);

        if (exists()) {
            retrieveAndUpdateData();
        } else {
            data = new InputStreamData(new NullInputStream(0));
        }
    }

    public DriveFile(Drive drive, HashMap<String, String> index, IDirectory parent, String name, Data data) throws StorageException {
        super(drive, index, parent, name);

        this.data = data;
    }

    @Override
    public String getPathname() {
        return logicalParent.getPathname() + name;
    }

    @Override
    public void setData(Data data) throws DataException {
        this.data = data;
    }

    @Override
    public Data getData() throws DataException {
        return data;
    }

    @Override
    public long getSize() {
        return data.getSize();
    }

    @Override
    public void persist() throws PersistenceException {

        try {
            File file = new File().setName(name);
            InputStreamContent mediaContent = new InputStreamContent(null, new BufferedInputStream(getData().getInputStream()));

            File storedFile = drive.files().create(file, mediaContent).execute();
            index.put(objectPath, storedFile.getId());
        } catch (IOException | DataException e) {
            throw new PersistenceException("Unable to persist file with name " + name);
        }
    }

    private void retrieveAndUpdateData() throws StorageException {

        String fileId = getId();
        try (InputStream stream = drive.files().get(fileId).executeMediaAsInputStream()) {

            data = new InputStreamData(stream);

        } catch (IOException e) {
            throw new StorageException("Unable to retrieve data from Drive");
        }
    }
}
