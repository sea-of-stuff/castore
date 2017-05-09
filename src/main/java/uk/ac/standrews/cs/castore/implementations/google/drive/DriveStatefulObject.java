package uk.ac.standrews.cs.castore.implementations.google.drive;

import com.google.api.services.drive.Drive;
import uk.ac.standrews.cs.castore.data.Data;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.implementations.CommonStatefulObject;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.StatefulObject;

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
            return getFile() != null;
        } catch (IOException e) {
            return false;
        }

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long lastModified() {

        try {
            return getFile().getModifiedTime().getValue();
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    public File toFile() throws IOException {
        return null;
    }

    @Override
    public long getSize() {

        try {
            com.google.api.services.drive.model.File file = getFile();
            if (file != null) {
                return file.getSize();
            }
        } catch (IOException e) {
            return 0;
        }

        return 0;
    }

    protected String getId() {
        return index.get(objectPath);
    }

    // See fields - https://developers.google.com/drive/v3/reference/files#resource
    protected com.google.api.services.drive.model.File getFile() throws IOException {

        String fileId = getId();
        return drive.files()
                .get(fileId)
                .setFields("id, kind, name, modifiedTime, size")
                .execute();
    }

}
