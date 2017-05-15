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
import java.util.Collections;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class DriveFile extends DriveStatefulObject implements IFile {

    protected Data data;

    DriveFile(Drive drive, IDirectory parent, String name) throws StorageException {
        super(drive, parent, name);

        if (exists()) {
            retrieveAndUpdateData();
        } else {
            data = new InputStreamData(new NullInputStream(0));
        }
    }

    DriveFile(Drive drive, IDirectory parent, String name, Data data) throws StorageException {
        super(drive, parent, name);

        this.data = data;
    }

    @Override
    public String getPathname() {
        return parent.getPathname() + name;
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
    public void persist() throws PersistenceException {

        if (parent != null) {
            // Make sure that the parent folder is persisted
            parent.persist();
        }

        try {
            if (!exists()) {
                createFile();
            } else {
                updateFile();
            }
        } catch (IOException | DataException | DriveException e) {
            throw new PersistenceException("Unable to persist file with name " + name + " Cause: " + e.getMessage());
        }
    }

    private void createFile() throws IOException, DataException, DriveException {

        String parentId = getId(getParent().getPathname());
        File file = new File()
                .setParents(Collections.singletonList(parentId))
                .setName(name);
        InputStreamContent mediaContent = new InputStreamContent(null, new BufferedInputStream(getData().getInputStream()));

        DriveWrapper.Execute(drive.files()
                .create(file, mediaContent));
    }

    private void updateFile() throws IOException, DataException, DriveException {
        File existingFile = getFile();
        InputStreamContent mediaContent = new InputStreamContent(null, new BufferedInputStream(getData().getInputStream()));

        // @see http://stackoverflow.com/a/35143284/2467938
        File newFile = new File();
        newFile.setTrashed(true);

        DriveWrapper.Execute(drive.files()
                .update(existingFile.getId(), newFile, mediaContent));
    }

    private void retrieveAndUpdateData() throws StorageException {

        String fileId = getId();
        try (InputStream stream = drive.files().get(fileId).executeMediaAsInputStream()) { // NOTE: No retry here

            data = new InputStreamData(stream);

        } catch (IOException e) {
            throw new StorageException("Unable to retrieve data from Drive");
        }
    }
}
