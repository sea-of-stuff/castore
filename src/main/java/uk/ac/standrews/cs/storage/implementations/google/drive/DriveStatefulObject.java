package uk.ac.standrews.cs.storage.implementations.google.drive;

import com.google.api.services.drive.Drive;
import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.exceptions.StorageException;
import uk.ac.standrews.cs.storage.implementations.CommonStatefulObject;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.StatefulObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public abstract class DriveStatefulObject extends CommonStatefulObject implements StatefulObject {

    protected Drive drive;

    protected IDirectory logicalParent;
    protected String objectPath;
    protected Data data;

    protected HashMap<String, String> index;

    public DriveStatefulObject(Drive drive, HashMap<String, String> index, IDirectory parent, String name) throws StorageException  {
        super(name);

        this.drive = drive;
        this.index = index;
        this.logicalParent = parent;
        this.objectPath = getPathname();
    }

    public DriveStatefulObject(Drive drive, HashMap<String, String> index, String name) throws StorageException {
        super(name);

        this.drive = drive;
        this.index = index;
        this.objectPath = getPathname();
    }

    @Override
    public IDirectory getLogicalParent() {
        return logicalParent;
    }

    @Override
    public boolean exists() {

        try {
            String fileId = getId();
            if (fileId == null) return false;

            drive.files().get(fileId).execute(); // Need to check drive to avoid any inconsistencies with the index

        } catch (IOException e) {
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long lastModified() {
        return 0;
    }

    @Override
    public File toFile() throws IOException {
        return null;
    }

    @Override
    public long getSize() {
        return 0;
    }

    protected String getId() {
        return index.get(objectPath);
    }

}
