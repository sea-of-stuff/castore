package uk.ac.standrews.cs.castore.implementations.google.drive;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import uk.ac.standrews.cs.castore.exceptions.BindingAbsentException;
import uk.ac.standrews.cs.castore.exceptions.PersistenceException;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.NameObjectBinding;
import uk.ac.standrews.cs.castore.interfaces.StatefulObject;

import java.io.IOException;
import java.util.Iterator;

import static uk.ac.standrews.cs.castore.CastoreConstants.FOLDER_DELIMITER;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class DriveDirectory extends DriveStatefulObject implements IDirectory {

    private static final String DRIVE_MIME_TYPE = "application/vnd.google-apps.folder";

    public DriveDirectory(Drive drive, Index index, IDirectory parent, String name) throws StorageException {
        super(drive, index, parent, name);
    }

    public DriveDirectory(Drive drive, Index index, String name) throws StorageException {
        super(drive, index, name);
    }

    @Override
    public String getPathname() {
        if (logicalParent == null) {
            return name + FOLDER_DELIMITER;
        } else if (name == null || name.isEmpty()) {
            return logicalParent.getPathname() + FOLDER_DELIMITER;
        } else {
            return logicalParent.getPathname() + name + FOLDER_DELIMITER;
        }
    }

    @Override
    public StatefulObject get(String name) throws BindingAbsentException {
        return null;
    }

    @Override
    public boolean contains(String name) {
        return false;
    }

    @Override
    public void remove(String name) throws BindingAbsentException {

    }

    @Override
    public Iterator<NameObjectBinding> getIterator() {
        return null;
    }

    @Override
    public void persist() throws PersistenceException {

        try {
            File fileMetadata = new File()
                    .setName(name)
                    .setMimeType(DRIVE_MIME_TYPE);

            File file = drive.files().create(fileMetadata)
                    .setFields("id")
                    .execute();
            System.out.println("Folder ID: " + file.getId() + " name " + name);

            index.setPathId(objectPath, file.getId());
        } catch (IOException e) {
            throw new PersistenceException("Unable to create folder with name " + name);
        }
    }
}
